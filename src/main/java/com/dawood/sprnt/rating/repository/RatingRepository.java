package com.dawood.sprnt.rating.repository;

import com.dawood.sprnt.rating.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RatingRepository extends JpaRepository<Rating, UUID> {

}
