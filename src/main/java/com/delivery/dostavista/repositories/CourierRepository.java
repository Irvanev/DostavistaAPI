package com.delivery.dostavista.repositories;

import com.delivery.dostavista.models.entities.Couriers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourierRepository extends JpaRepository<Couriers, Long> {

}
