package com.example.softwareEngineer.service;

import com.example.softwareEngineer.DTO.Review;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ReviewService {
    Review addReview(Review review,Integer userId);

    Review addReply(Integer reviewId, Review review);
}
