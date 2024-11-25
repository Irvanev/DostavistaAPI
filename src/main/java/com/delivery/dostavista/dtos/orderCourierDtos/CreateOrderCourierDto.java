package com.delivery.dostavista.dtos.orderCourierDtos;

public class CreateOrderCourierDto {
    private Long orderId;
    private Long courierId;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getCourierId() {
        return courierId;
    }

    public void setCourierId(Long courierId) {
        this.courierId = courierId;
    }
}
