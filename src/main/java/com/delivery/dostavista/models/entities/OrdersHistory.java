package com.delivery.dostavista.models.entities;

import com.delivery.dostavista.models.enums.OrderStatusEnum;
import jakarta.persistence.*;

import java.util.Date;

@Entity
public class OrdersHistory extends BaseEntity {
    private Orders order;
    private OrderStatusEnum status;
    private Date changedAt;

    protected OrdersHistory() {}

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    public Orders getOrder() {
        return order;
    }

    public void setOrder(Orders order) {
        this.order = order;
    }

    @Enumerated(EnumType.STRING)
    public OrderStatusEnum getStatus() {
        return status;
    }

    public void setStatus(OrderStatusEnum status) {
        this.status = status;
    }

    public Date getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(Date changedAt) {
        this.changedAt = changedAt;
    }
}
