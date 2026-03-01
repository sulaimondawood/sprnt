package com.dawood.sprnt.common.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.dawood.sprnt.common.config.KafkaConfig;
import com.dawood.sprnt.driver.api.dto.DriverLocationDTO;
import com.dawood.sprnt.driver.repository.DriverRepository;
import com.dawood.sprnt.identity.api.dto.UserAccountDTO;
import com.dawood.sprnt.rating.api.dto.RatingMessage;
import com.dawood.sprnt.rating.model.RatingSource;
import com.dawood.sprnt.rating.repository.RatingRepository;
import com.dawood.sprnt.ride.event.CreateRideEvent;
import com.dawood.sprnt.ride.exception.LocationException;
import com.dawood.sprnt.ride.exception.RideNotFoundException;
import com.dawood.sprnt.ride.model.Ride;
import com.dawood.sprnt.ride.repository.RideRepository;
import com.dawood.sprnt.ride.service.RideMatchingService;
import com.dawood.sprnt.rider.repository.RiderRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer {

    @Value("${app.client-url}")
    private String clientUrl;

    private final DriverRepository driverRepository;
    private final EmailService emailService;
    private final TemplateEngine templateEngine;
    private final RideMatchingService rideMatchingService;
    private final RideRepository rideRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final RatingRepository ratingRepository;
    private final RiderRepository riderRepository;

    @KafkaListener(topics = KafkaConfig.EMAIL_TOPIC_NAME, groupId = "email-service-group1")
    public void consumeSendAccountActivationEmail(UserAccountDTO message) {

        try {
            log.info("Email service group subscribes to the published event");

            String token = message.getToken();
            String fullname = message.getFullname();
            String email = message.getEmail();

            String[] nameParts = fullname.split(" ");
            String username = nameParts.length > 0 ? nameParts[0] : fullname;

            String activationUrl = String.format("%sauth/verify-email?token=%s", clientUrl, token);

            Context context = new Context();

            context.setVariable("username", username);

            context.setVariable("activationLink", activationUrl);

            context.setVariable("expiresIn", "15mins");

            String emailBody = templateEngine.process("account/email-verification.html", context);

            emailService.sendEmail(email, "Sprnt Account Activation", emailBody);
        } catch (Exception e) {
            log.error("Error processing Kafka message", e);
            throw new RuntimeException("Failed to process email", e);
        }

    }

    @KafkaListener(topics = KafkaConfig.PASSWORD_RESET_TOPIC, groupId = "password-reset-group")
    public void consumeSendAccountPasswordResetEmail(Map<String, String> message) {

        try {
            log.info("Email service group subscribes to the published event");

            String token = message.get("token");
            String email = message.get("email");

            if (token == null || email == null) {
                log.warn("Received malformed Kafka message: {}", message);
                return;
            }

            String url = String.format("%sauth/reset-password?token=%s", clientUrl, token);

            Context ctx = new Context();
            ctx.setVariable("RESET_LINK", url);

            String body = templateEngine.process("account/password-reset.html", ctx);

            emailService.sendEmail(email, "Password Reset - Sprnt", body);
        } catch (Exception e) {
            log.error("Error processing Kafka message", e);
            throw new RuntimeException("Failed to process email", e);
        }

    }

    @KafkaListener(topics = KafkaConfig.RIDE_REQUEST_TOPIC, groupId = "ride-request-grooFixedup")
    public void consumeCreateRideRequest(CreateRideEvent message) {

        Ride ride = rideRepository.findById(message.getRideId())
                .orElseThrow(RideNotFoundException::new);

        rideMatchingService.findAndDispatch(ride, null, 10);
    }

    @KafkaListener(topics = KafkaConfig.DRIVER_LOCATION_TOPIC, groupId = "driver-location-group-1")
    public void consumeDriverLocationUpdate(DriverLocationDTO message) {

        if (message.getActiveRideId() != null) {
            String destination = "/queue/ride/" + message.getActiveRideId().toString();
            log.info("Broadcasting location to destination: {}", destination);

            simpMessagingTemplate.convertAndSend(
                    "/topic/ride/" + message.getActiveRideId().toString(),
                    message);
        } else {
            log.debug("No active ride for driver {}; skipping broadcast.", message.getDriverId());
        }

        try {
            driverRepository.updateLocation(message.getDriverId(), message.getLng(), message.getLat());
        } catch (Exception e) {
            log.error("Error updating DB location for driver {}", message.getDriverId(), e);
            throw new LocationException("Error updating driver: " + message.getDriverId().toString() + " location");
        }

    }

    @KafkaListener(topics = KafkaConfig.RIDE_RATING_TOPIC, groupId = "ratings-group")
    public void consumeAndProcessRatings(RatingMessage message) {

        double avgRating = ratingRepository.getAverageRatingsForUser(message.getRatedUser());
        long ratingCounts = ratingRepository.countRatingsForUser(message.getRatedUser());

        Ride ride = rideRepository.findById(message.getRideId())
                .orElseThrow(RideNotFoundException::new);

        if (message.getRatingSource() == RatingSource.RIDER) {

            driverRepository.updateRating(message.getRatedUser(), avgRating, ratingCounts);
            log.info("Updated Driver {} rating to {}", message.getRatedUser(), avgRating);

        } else {
            // If the DRIVER submitted the review, we update the RIDER's profile.
            riderRepository.updateRating(message.getRatedUser(), avgRating, ratingCounts);
            log.info("Updated Rider {} rating to {}", message.getRatedUser(), avgRating);
        }

    }

}
