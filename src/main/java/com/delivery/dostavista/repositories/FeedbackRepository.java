package com.delivery.dostavista.repositories;

import com.delivery.dostavista.models.entities.Feedbacks;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedbacks, Long> {

}
