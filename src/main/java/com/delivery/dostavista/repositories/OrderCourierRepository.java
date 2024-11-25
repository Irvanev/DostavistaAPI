package com.delivery.dostavista.repositories;

import com.delivery.dostavista.models.entities.OrdersCourier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderCourierRepository extends JpaRepository<OrdersCourier, Long> {
    Optional<OrdersCourier> findByCourierIdAndOrderId(Long courierId, Long orderId);
}
