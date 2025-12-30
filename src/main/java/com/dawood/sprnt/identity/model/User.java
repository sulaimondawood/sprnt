package com.dawood.sprnt.identity.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.dawood.sprnt.driver.model.Driver;
import com.dawood.sprnt.rider.model.Rider;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String fullname;

  private String email;

  private String password;

  private LocalDateTime lastLogin;

  @Enumerated(EnumType.STRING)
  private Status status;

  @Enumerated(EnumType.STRING)
  private Role role;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
  private Driver driver;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
  private Rider rider;

  @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
  private VerificationToken token;

  @UpdateTimestamp
  private LocalDateTime updatedAt;

  @CreationTimestamp
  private LocalDateTime createdAt;

}
