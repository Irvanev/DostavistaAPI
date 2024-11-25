package com.delivery.dostavista.models.entities;

import com.delivery.dostavista.models.enums.OrderStatusEnum;
import jakarta.persistence.*;

import java.util.Date;

@Entity
public class OrdersCourier extends BaseEntity {
    private Orders order;
    private Couriers courier;
    private OrderStatusEnum status;
    private Date assignedAt;

    public OrdersCourier() {}

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    public Orders getOrder() {
        return order;
    }

    public void setOrder(Orders order) {
        this.order = order;
    }

    @ManyToOne
    @JoinColumn(name = "courier_id", nullable = false)
    public Couriers getCourier() {
        return courier;
    }

    public void setCourier(Couriers courier) {
        this.courier = courier;
    }

    @Enumerated(EnumType.STRING)
    public OrderStatusEnum getStatus() {
        return status;
    }

    public void setStatus(OrderStatusEnum status) {
        this.status = status;
    }

    public Date getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(Date assignedAt) {
        this.assignedAt = assignedAt;
    }
}
