package com.delivery.dostavista.controllers;

import com.delivery.dostavista.dtos.customerDtos.CreateCustomerDto;
import com.delivery.dostavista.dtos.customerDtos.CustomerDto;
import com.delivery.dostavista.services.CustomerService;
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
@RequestMapping("/api/customer")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/find/{id}")
    public EntityModel<CustomerDto> getCustomerById(@PathVariable Long id) {
        CustomerDto customer = customerService.getCustomer(id).orElse(null);
        return EntityModel.of(customer,
                linkTo(methodOn(CustomerController.class).getCustomerById(id))
                        .withSelfRel()
                        .withType("GET")
                        .withTitle("Получение заказчика по ID"),
                linkTo(methodOn(CustomerController.class).deleteCustomer(customer.getId()))
                        .withSelfRel()
                        .withType("DELETE")
                        .withTitle("Удаление заказчика"),
                linkTo(methodOn(CustomerController.class).editCustomer(new CreateCustomerDto(), customer.getId()))
                        .withSelfRel()
                        .withType("PUT")
                        .withTitle("Редактирование данных заказчика")
        );
    }

    @PostMapping("/create")
    public EntityModel<CustomerDto> createCustomer(@RequestBody CreateCustomerDto createCustomerDto) {
        CustomerDto createdCustomer = customerService.createCustomers(createCustomerDto);
        return EntityModel.of(createdCustomer,
                linkTo(methodOn(CustomerController.class).getCustomerById(createdCustomer.getId()))
                        .withSelfRel()
                        .withType("GET")
                        .withTitle("Получение закачика по ID"),
                linkTo(methodOn(CustomerController.class).deleteCustomer(createdCustomer.getId()))
                        .withSelfRel()
                        .withType("DELETE")
                        .withTitle("Удаление заказчика"),
                linkTo(methodOn(CustomerController.class).editCustomer(new CreateCustomerDto(), createdCustomer.getId()))
                        .withSelfRel()
                        .withType("PUT")
                        .withTitle("Редактирование данных заказчика")
        );
    }

    @GetMapping("/all")
    public PagedModel<EntityModel<CustomerDto>> getAllCustomers(
            @SortDefault(sort = "id", direction = Sort.Direction.ASC)
            @PageableDefault(size = 10) Pageable pageable) {

        Page<CustomerDto> customers = customerService.getAllCustomers(pageable);

        List<EntityModel<CustomerDto>> customerModels = customers.stream()
                .map(customer -> EntityModel.of(customer,
                        linkTo(methodOn(CustomerController.class).getCustomerById(customer.getId())).withSelfRel()))
                .collect(Collectors.toList());

        PagedModel<EntityModel<CustomerDto>> pagedModel = PagedModel.of(
                customerModels,
                new PagedModel.PageMetadata(customers.getSize(), customers.getNumber(), customers.getTotalElements())
        );

        String selfUrl = String.format("%s?page=%d&size=%d",
                linkTo(methodOn(CustomerController.class).getAllCustomers(pageable)).toUri().toString(),
                pageable.getPageNumber(), pageable.getPageSize());
        pagedModel.add(Link.of(selfUrl, "self"));

        if (customers.hasNext()) {
            String nextUrl = String.format("%s?page=%d&size=%d",
                    linkTo(methodOn(CustomerController.class).getAllCustomers(pageable)).toUri().toString(),
                    pageable.getPageNumber() + 1, pageable.getPageSize());
            pagedModel.add(Link.of(nextUrl, "next"));
        }

        if (customers.hasPrevious()) {
            String prevUrl = String.format("%s?page=%d&size=%d",
                    linkTo(methodOn(CustomerController.class).getAllCustomers(pageable)).toUri().toString(),
                    pageable.getPageNumber() - 1, pageable.getPageSize());
            pagedModel.add(Link.of(prevUrl, "prev"));
        }

        return pagedModel;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/edit/{id}")
    public EntityModel<CustomerDto> editCustomer(@RequestBody CreateCustomerDto createCustomerDto, @PathVariable Long id) {
        CustomerDto customer = customerService.editCustomer(id, createCustomerDto);
        return EntityModel.of(customer,
                linkTo(methodOn(CustomerController.class).getCustomerById(id))
                        .withSelfRel()
                        .withType("GET")
                        .withTitle("Получение заказчика по ID"),
                linkTo(methodOn(CustomerController.class).deleteCustomer(id))
                        .withSelfRel()
                        .withType("DELETE")
                        .withTitle("Удаление заказчика"),
                linkTo(methodOn(CustomerController.class).editCustomer(new CreateCustomerDto(), customer.getId()))
                        .withSelfRel()
                        .withType("PUT")
                        .withTitle("Редактирование данных заказчика")
        );
    }
}
