package com.delivery.dostavista.controllers;

import com.delivery.dostavista.dtos.feedbackDtos.CreateFeedbackDto;
import com.delivery.dostavista.dtos.feedbackDtos.FeedbackDto;
import com.delivery.dostavista.services.FeedbackService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {
    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @GetMapping("/find/{id}")
    public EntityModel<FeedbackDto> getFeedbackById(@PathVariable Long id) {
        FeedbackDto feedback = feedbackService.getFeedback(id).orElse(null);
        return EntityModel.of(feedback,
                linkTo(methodOn(FeedbackController.class).getFeedbackById(id))
                        .withSelfRel()
                        .withType("GET")
                        .withTitle("Получение отзыва по ID"),
                linkTo(methodOn(FeedbackController.class).deleteFeedback(feedback.getId()))
                        .withSelfRel()
                        .withType("DELETE")
                        .withTitle("Удаление отзыва"),
                linkTo(methodOn(FeedbackController.class).editFeedback(new CreateFeedbackDto(), feedback.getId()))
                        .withSelfRel()
                        .withType("PUT")
                        .withTitle("Редактирование данных отзыва")
        );
    }

    @PostMapping("/create")
    public EntityModel<FeedbackDto> createFeedback(@RequestBody CreateFeedbackDto createFeedbackDto) {
        FeedbackDto createdFeedback = feedbackService.createFeedback(createFeedbackDto);
        return EntityModel.of(createdFeedback,
                linkTo(methodOn(FeedbackController.class).getFeedbackById(createdFeedback.getId()))
                        .withSelfRel()
                        .withType("GET")
                        .withTitle("Получение отзыва по ID"),
                linkTo(methodOn(FeedbackController.class).deleteFeedback(createdFeedback.getId()))
                        .withSelfRel()
                        .withType("DELETE")
                        .withTitle("Удаление отзыва"),
                linkTo(methodOn(FeedbackController.class).editFeedback(new CreateFeedbackDto(), createdFeedback.getId()))
                        .withSelfRel()
                        .withType("PUT")
                        .withTitle("Редактирование данных отзыва")
        );
    }

    @GetMapping("/all")
    public PagedModel<EntityModel<FeedbackDto>> getAllFeedbacks(
            @SortDefault(sort = "id", direction = Sort.Direction.ASC)
            @PageableDefault(size = 10) Pageable pageable) {

        Page<FeedbackDto> feedbacks = feedbackService.getAllFeedbacks(pageable);

        List<EntityModel<FeedbackDto>> feedbackModels = feedbacks.stream()
                .map(customer -> EntityModel.of(customer,
                        linkTo(methodOn(FeedbackController.class).getFeedbackById(customer.getId())).withSelfRel()))
                .collect(Collectors.toList());

        PagedModel<EntityModel<FeedbackDto>> pagedModel = PagedModel.of(
                feedbackModels,
                new PagedModel.PageMetadata(feedbacks.getSize(), feedbacks.getNumber(), feedbacks.getTotalElements())
        );

        String selfUrl = String.format("%s?page=%d&size=%d",
                linkTo(methodOn(FeedbackController.class).getAllFeedbacks(pageable)).toUri().toString(),
                pageable.getPageNumber(), pageable.getPageSize());
        pagedModel.add(Link.of(selfUrl, "self"));

        if (feedbacks.hasNext()) {
            String nextUrl = String.format("%s?page=%d&size=%d",
                    linkTo(methodOn(FeedbackController.class).getAllFeedbacks(pageable)).toUri().toString(),
                    pageable.getPageNumber() + 1, pageable.getPageSize());
            pagedModel.add(Link.of(nextUrl, "next"));
        }

        if (feedbacks.hasPrevious()) {
            String prevUrl = String.format("%s?page=%d&size=%d",
                    linkTo(methodOn(FeedbackController.class).getAllFeedbacks(pageable)).toUri().toString(),
                    pageable.getPageNumber() - 1, pageable.getPageSize());
            pagedModel.add(Link.of(prevUrl, "prev"));
        }

        return pagedModel;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable Long id) {
        feedbackService.deleteFeedback(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/edit/{id}")
    public EntityModel<FeedbackDto> editFeedback(@RequestBody CreateFeedbackDto createFeedbackDto, @PathVariable Long id) {
        FeedbackDto feedback = feedbackService.editFeedback(id, createFeedbackDto);
        return EntityModel.of(feedback,
                linkTo(methodOn(FeedbackController.class).getFeedbackById(id))
                        .withSelfRel()
                        .withType("GET")
                        .withTitle("Получение отзыва по ID"),
                linkTo(methodOn(FeedbackController.class).deleteFeedback(id))
                        .withSelfRel()
                        .withType("DELETE")
                        .withTitle("Удаление отзыва"),
                linkTo(methodOn(FeedbackController.class).editFeedback(new CreateFeedbackDto(), feedback.getId()))
                        .withSelfRel()
                        .withType("PUT")
                        .withTitle("Редактирование данных отзыва")
        );
    }
}
