package com.delivery.dostavista.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.util.Date;

@Entity
public class Feedbacks extends BaseEntity {
    private Customers customer;
    private Couriers courier;
    private int rating;
    private String comment;
    private Date createdAt;

    protected Feedbacks() {}

    @ManyToOne
    @JoinColumn(name = "customer_id")
    public Customers getCustomer() {
        return customer;
    }

    public void setCustomer(Customers customer) {
        this.customer = customer;
    }

    @ManyToOne
    @JoinColumn(name = "courier_id")
    public Couriers getCourier() {
        return courier;
    }

    public void setCourier(Couriers courier) {
        this.courier = courier;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
