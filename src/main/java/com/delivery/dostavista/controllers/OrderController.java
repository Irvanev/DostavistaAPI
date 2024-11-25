package com.delivery.dostavista.controllers;

import com.delivery.dostavista.dtos.orderDtos.CreateOrderDto;
import com.delivery.dostavista.dtos.orderDtos.OrderDto;
import com.delivery.dostavista.services.OrderService;
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
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/find/{id}")
    public EntityModel<OrderDto> getOrderById(@PathVariable Long id) {
        OrderDto order = orderService.getOrderById(id).orElse(null);
        return EntityModel.of(order,
                linkTo(methodOn(OrderController.class).getOrderById(id))
                        .withSelfRel()
                        .withType("GET")
                        .withTitle("Получение заказчика по ID"),
                linkTo(methodOn(OrderController.class).deleteOrder(order.getId()))
                        .withSelfRel()
                        .withType("DELETE")
                        .withTitle("Удаление заказчика"),
                linkTo(methodOn(OrderController.class).editOrder(new CreateOrderDto(), order.getId()))
                        .withSelfRel()
                        .withType("PUT")
                        .withTitle("Редактирование данных заказчика")
        );
    }

    @PostMapping("/create")
    public EntityModel<OrderDto> createCustomer(@RequestBody CreateOrderDto createOrderDto) {
        OrderDto order = orderService.createOrder(createOrderDto);
        return EntityModel.of(order,
                linkTo(methodOn(OrderController.class).getOrderById(order.getId()))
                        .withSelfRel()
                        .withType("GET")
                        .withTitle("Получение закачика по ID"),
                linkTo(methodOn(OrderController.class).deleteOrder(order.getId()))
                        .withSelfRel()
                        .withType("DELETE")
                        .withTitle("Удаление заказчика"),
                linkTo(methodOn(OrderController.class).editOrder(new CreateOrderDto(), order.getId()))
                        .withSelfRel()
                        .withType("PUT")
                        .withTitle("Редактирование данных заказчика")
        );
    }

    @GetMapping("/all")
    public PagedModel<EntityModel<OrderDto>> getAllOrders(
            @SortDefault(sort = "id", direction = Sort.Direction.ASC)
            @PageableDefault(size = 10) Pageable pageable) {

        Page<OrderDto> orders = orderService.getAllOrders(pageable);

        List<EntityModel<OrderDto>> orderModels = orders.stream()
                .map(order -> EntityModel.of(order,
                        linkTo(methodOn(OrderController.class).getOrderById(order.getId())).withSelfRel()))
                .collect(Collectors.toList());

        PagedModel<EntityModel<OrderDto>> pagedModel = PagedModel.of(
                orderModels,
                new PagedModel.PageMetadata(orders.getSize(), orders.getNumber(), orders.getTotalElements())
        );

        String selfUrl = String.format("%s?page=%d&size=%d",
                linkTo(methodOn(OrderController.class).getAllOrders(pageable)).toUri().toString(),
                pageable.getPageNumber(), pageable.getPageSize());
        pagedModel.add(Link.of(selfUrl, "self"));

        if (orders.hasNext()) {
            String nextUrl = String.format("%s?page=%d&size=%d",
                    linkTo(methodOn(OrderController.class).getAllOrders(pageable)).toUri().toString(),
                    pageable.getPageNumber() + 1, pageable.getPageSize());
            pagedModel.add(Link.of(nextUrl, "next"));
        }

        if (orders.hasPrevious()) {
            String prevUrl = String.format("%s?page=%d&size=%d",
                    linkTo(methodOn(OrderController.class).getAllOrders(pageable)).toUri().toString(),
                    pageable.getPageNumber() - 1, pageable.getPageSize());
            pagedModel.add(Link.of(prevUrl, "prev"));
        }

        return pagedModel;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/edit/{id}")
    public EntityModel<OrderDto> editOrder(@RequestBody CreateOrderDto createOrderDto, @PathVariable Long id) {
        OrderDto order = orderService.editOrder(id, createOrderDto);
        return EntityModel.of(order,
                linkTo(methodOn(OrderController.class).getOrderById(id))
                        .withSelfRel()
                        .withType("GET")
                        .withTitle("Получение заказа по ID"),
                linkTo(methodOn(OrderController.class).deleteOrder(id))
                        .withSelfRel()
                        .withType("DELETE")
                        .withTitle("Удаление заказа"),
                linkTo(methodOn(OrderController.class).editOrder(new CreateOrderDto(), order.getId()))
                        .withSelfRel()
                        .withType("PUT")
                        .withTitle("Редактирование данных заказчика")
        );
    }
}
