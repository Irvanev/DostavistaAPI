package com.delivery.dostavista.controllers;

import com.delivery.dostavista.dtos.orderCourierDtos.CreateOrderCourierDto;
import com.delivery.dostavista.dtos.orderCourierDtos.OrderCourierDto;
import com.delivery.dostavista.models.enums.OrderStatusEnum;
import com.delivery.dostavista.services.OrderCourierService;
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
@RequestMapping("/api/orderCouriers")
public class OrderCourierController {

    private final OrderCourierService orderCourierService;

    public OrderCourierController(OrderCourierService orderCourierService) {
        this.orderCourierService = orderCourierService;
    }

    @GetMapping("/find/{id}")
    public EntityModel<OrderCourierDto> getOrderCourierById(@PathVariable Long id) {
        OrderCourierDto orderCourier = orderCourierService.getOrderCourier(id).orElse(null);
        return EntityModel.of(orderCourier,
                linkTo(methodOn(CustomerController.class).getCustomerById(id))
                        .withSelfRel()
                        .withType("GET")
                        .withTitle("Получение заказчика по ID"),
                linkTo(methodOn(OrderCourierController.class).deleteOrderCourier(orderCourier.getId()))
                        .withSelfRel()
                        .withType("DELETE")
                        .withTitle("Удаление заказчика"),
                linkTo(methodOn(OrderCourierController.class).editOrderCourier(new CreateOrderCourierDto(), orderCourier.getId()))
                        .withSelfRel()
                        .withType("PUT")
                        .withTitle("Редактирование данных заказчика")
        );
    }

    @PostMapping("/create")
    public EntityModel<OrderCourierDto> createOrderCourier(@RequestBody CreateOrderCourierDto createOrderCourierDto) {
        OrderCourierDto createdOrderCourier = orderCourierService.createOrderCourier(createOrderCourierDto);
        return EntityModel.of(createdOrderCourier,
                linkTo(methodOn(OrderCourierController.class).getOrderCourierById(createdOrderCourier.getId()))
                        .withSelfRel()
                        .withType("GET")
                        .withTitle("Получение заказа курьера по ID"),
                linkTo(methodOn(OrderCourierController.class).deleteOrderCourier(createdOrderCourier.getId()))
                        .withSelfRel()
                        .withType("DELETE")
                        .withTitle("Удаление заказа курьера"),
                linkTo(methodOn(OrderCourierController.class).editOrderCourier(new CreateOrderCourierDto(), createdOrderCourier.getId()))
                        .withSelfRel()
                        .withType("PUT")
                        .withTitle("Редактирование данных заказчика")
        );
    }

    @GetMapping("/all")
    public PagedModel<EntityModel<OrderCourierDto>> getAllOrdersCourier(
            @SortDefault(sort = "id", direction = Sort.Direction.ASC)
            @PageableDefault(size = 10) Pageable pageable) {

        Page<OrderCourierDto> orderCouriers = orderCourierService.getAllOrdersCourier(pageable);

        List<EntityModel<OrderCourierDto>> orderCourierModels = orderCouriers.stream()
                .map(customer -> EntityModel.of(customer,
                        linkTo(methodOn(CustomerController.class).getCustomerById(customer.getId())).withSelfRel()))
                .collect(Collectors.toList());

        PagedModel<EntityModel<OrderCourierDto>> pagedModel = PagedModel.of(
                orderCourierModels,
                new PagedModel.PageMetadata(orderCouriers.getSize(), orderCouriers.getNumber(), orderCouriers.getTotalElements())
        );

        String selfUrl = String.format("%s?page=%d&size=%d",
                linkTo(methodOn(OrderCourierController.class).getAllOrdersCourier(pageable)).toUri().toString(),
                pageable.getPageNumber(), pageable.getPageSize());
        pagedModel.add(Link.of(selfUrl, "self"));

        if (orderCouriers.hasNext()) {
            String nextUrl = String.format("%s?page=%d&size=%d",
                    linkTo(methodOn(OrderCourierController.class).getAllOrdersCourier(pageable)).toUri().toString(),
                    pageable.getPageNumber() + 1, pageable.getPageSize());
            pagedModel.add(Link.of(nextUrl, "next"));
        }

        if (orderCouriers.hasPrevious()) {
            String prevUrl = String.format("%s?page=%d&size=%d",
                    linkTo(methodOn(OrderCourierController.class).getAllOrdersCourier(pageable)).toUri().toString(),
                    pageable.getPageNumber() - 1, pageable.getPageSize());
            pagedModel.add(Link.of(prevUrl, "prev"));
        }

        return pagedModel;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteOrderCourier(@PathVariable Long id) {
        orderCourierService.deleteOrderCourier(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/edit/{id}")
    public EntityModel<OrderCourierDto> editOrderCourier(@RequestBody CreateOrderCourierDto createOrderCourierDto, @PathVariable Long id) {
        OrderCourierDto orderCourier = orderCourierService.editOrderCourier(id, createOrderCourierDto);
        return EntityModel.of(orderCourier,
                linkTo(methodOn(OrderCourierController.class).getOrderCourierById(id))
                        .withSelfRel()
                        .withType("GET")
                        .withTitle("Получение заказа курьера по ID"),
                linkTo(methodOn(OrderCourierController.class).deleteOrderCourier(orderCourier.getId()))
                        .withSelfRel()
                        .withType("DELETE")
                        .withTitle("Удаление заказа курьера"),
                linkTo(methodOn(OrderCourierController.class).editOrderCourier(new CreateOrderCourierDto(), orderCourier.getId()))
                        .withSelfRel()
                        .withType("PUT")
                        .withTitle("Редактирование данных заказчика")
        );
    }

    @PatchMapping("/{courierId}/orders/{orderId}/status")
    public ResponseEntity<OrderCourierDto> updateOrderStatus(
            @PathVariable Long courierId,
            @PathVariable Long orderId,
            @RequestParam OrderStatusEnum newStatus) {

        OrderCourierDto updatedOrder = orderCourierService.updateOrderStatusByCourier(courierId, orderId, newStatus);

        return ResponseEntity.ok(updatedOrder);
    }
}
