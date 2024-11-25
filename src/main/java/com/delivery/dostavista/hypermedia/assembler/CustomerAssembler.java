package com.delivery.dostavista.hypermedia.assembler;

import com.delivery.dostavista.controllers.CustomerController;
import com.delivery.dostavista.hypermedia.model.CustomerModel;
import com.delivery.dostavista.models.entities.Customers;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static java.lang.Long.valueOf;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CustomerAssembler extends RepresentationModelAssemblerSupport<Customers, CustomerModel> {

    public CustomerAssembler() {
        super(CustomerController.class, CustomerModel.class);
    }

    @Override
    public CustomerModel toModel(@NonNull Customers entity) {
        CustomerModel model = instantiateModel(entity);

        model.setFirstName(entity.getFirstName());
        model.setLastName(entity.getLastName());
        model.setEmail(entity.getEmail());
        model.setPhone(entity.getPhone());

        model.add(linkTo(methodOn(CustomerController.class)
                .getCustomerById(entity.getId()))
                .withSelfRel());

        return model;
    }
}
