package com.delivery.dostavista.controllers;

import com.delivery.dostavista.dtos.courierDtos.CourierDto;
import com.delivery.dostavista.dtos.courierDtos.CreateCourierDto;
import com.delivery.dostavista.services.CourierService;
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
@RequestMapping("/api/courier")
public class CourierController {

    private final CourierService courierService;

    public CourierController(CourierService courierService) {
        this.courierService = courierService;
    }

    @GetMapping("/find/{id}")
    public EntityModel<CourierDto> getCourierById(@PathVariable Long id) {
        CourierDto courier = courierService.getCourierById(id).orElse(null);
        return EntityModel.of(courier,
                linkTo(methodOn(CourierController.class).getCourierById(id))
                        .withSelfRel()
                        .withType("GET")
                        .withTitle("Получение курьера по ID"),
                linkTo(methodOn(CourierController.class).deleteCourier(id))
                        .withSelfRel()
                        .withType("DELETE")
                        .withTitle("Удаление курьера"),
                linkTo(methodOn(CourierController.class).editCourier(new CreateCourierDto(), courier.getId()))
                        .withSelfRel()
                        .withType("PUT")
                        .withTitle("Редактирование данных курьера")
        );
    }

    @PostMapping("/create")
    public EntityModel<CourierDto> createCourier(@RequestBody CreateCourierDto createCourierDto) {
        CourierDto createdCourier = courierService.createCourier(createCourierDto);
        return EntityModel.of(createdCourier,
                linkTo(methodOn(CourierController.class).getCourierById(createdCourier.getId()))
                        .withSelfRel()
                        .withType("GET")
                        .withTitle("Получение курьера по ID"),
                linkTo(methodOn(CourierController.class).deleteCourier(createdCourier.getId()))
                        .withSelfRel()
                        .withType("DELETE")
                        .withTitle("Удаление курьера"),
                linkTo(methodOn(CourierController.class).editCourier(new CreateCourierDto(), createdCourier.getId()))
                        .withSelfRel()
                        .withType("PUT")
                        .withTitle("Редактирование данных курьера")
        );
    }

    @GetMapping("/all")
    public PagedModel<EntityModel<CourierDto>> getAllCouriers(
            @SortDefault(sort = "id", direction = Sort.Direction.ASC)
            @PageableDefault(size = 10) Pageable pageable) {

        Page<CourierDto> couriers = courierService.getAllCouriers(pageable);

        List<EntityModel<CourierDto>> courierModels = couriers.stream()
                .map(customer -> EntityModel.of(customer,
                        linkTo(methodOn(CustomerController.class).getCustomerById(customer.getId())).withSelfRel()))
                .collect(Collectors.toList());

        PagedModel<EntityModel<CourierDto>> pagedModel = PagedModel.of(
                courierModels,
                new PagedModel.PageMetadata(couriers.getSize(), couriers.getNumber(), couriers.getTotalElements())
        );

        String selfUrl = String.format("%s?page=%d&size=%d",
                linkTo(methodOn(CourierController.class).getAllCouriers(pageable)).toUri().toString(),
                pageable.getPageNumber(), pageable.getPageSize());
        pagedModel.add(Link.of(selfUrl, "self"));

        if (couriers.hasNext()) {
            String nextUrl = String.format("%s?page=%d&size=%d",
                    linkTo(methodOn(CourierController.class).getAllCouriers(pageable)).toUri().toString(),
                    pageable.getPageNumber() + 1, pageable.getPageSize());
            pagedModel.add(Link.of(nextUrl, "next"));
        }

        if (couriers.hasPrevious()) {
            String prevUrl = String.format("%s?page=%d&size=%d",
                    linkTo(methodOn(CourierController.class).getAllCouriers(pageable)).toUri().toString(),
                    pageable.getPageNumber() - 1, pageable.getPageSize());
            pagedModel.add(Link.of(prevUrl, "prev"));
        }

        return pagedModel;
    }

    @PutMapping("/edit/{id}")
    public EntityModel<CourierDto> editCourier(@RequestBody CreateCourierDto createCourierDto, @PathVariable Long id) {
        CourierDto courier = courierService.editCourier(id, createCourierDto);
        return EntityModel.of(courier,
                linkTo(methodOn(CourierController.class).getCourierById(id))
                        .withSelfRel()
                        .withType("GET")
                        .withTitle("Получение курьера по ID"),
                linkTo(methodOn(CourierController.class).deleteCourier(id))
                        .withSelfRel()
                        .withType("DELETE")
                        .withTitle("Удаление курьера"),
                linkTo(methodOn(CourierController.class).editCourier(new CreateCourierDto(), courier.getId()))
                        .withSelfRel()
                        .withType("PUT")
                        .withTitle("Редактирование данных курьера")
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCourier(@PathVariable Long id) {
        courierService.deleteCourier(id);
        return ResponseEntity.noContent().build();
    }
}
