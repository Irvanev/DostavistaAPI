package com.delivery.dostavista.repositories;

import com.delivery.dostavista.models.entities.OrdersHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderHistoryRepository extends JpaRepository<OrdersHistory, Long> {

}
