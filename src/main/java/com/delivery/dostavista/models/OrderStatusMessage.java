package com.delivery.dostavista.models;

public class OrderStatusMessage {
        private Long courierId;
        private int price;
        private String requisite;

    public OrderStatusMessage(Long courierId, int price, String requisite) {
        this.courierId = courierId;
        this.price = price;
        this.requisite = requisite;
    }

    public Long getCourierId() {
        return courierId;
    }

    public void setCourierId(Long courierId) {
        this.courierId = courierId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getRequisite() {
        return requisite;
    }

    public void setRequisite(String requisite) {
        this.requisite = requisite;
    }

    @Override
    public String toString() {
        return "OrderStatusMessage{" +
                "courierId=" + courierId +
                ", price=" + price +
                ", requisite='" + requisite + '\'' +
                '}';
    }
}
