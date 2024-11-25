package com.delivery.dostavista.services;

import com.delivery.dostavista.dtos.feedbackDtos.CreateFeedbackDto;
import com.delivery.dostavista.dtos.feedbackDtos.FeedbackDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface FeedbackService {
    FeedbackDto createFeedback(CreateFeedbackDto createFeedbackDto);
    void deleteFeedback(Long id);
    FeedbackDto editFeedback(Long id, CreateFeedbackDto createFeedbackDto);
    Page<FeedbackDto> getAllFeedbacks(Pageable pageable);
    Optional<FeedbackDto> getFeedback(Long id);
}
