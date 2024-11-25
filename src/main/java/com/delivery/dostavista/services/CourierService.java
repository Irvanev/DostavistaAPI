package com.delivery.dostavista.services;

import com.delivery.dostavista.dtos.courierDtos.CourierDto;
import com.delivery.dostavista.dtos.courierDtos.CreateCourierDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CourierService {
    CourierDto createCourier(CreateCourierDto createCourierDto);
    Page<CourierDto> getAllCouriers(Pageable pageable);
    Optional<CourierDto> getCourierById(Long id);
    void deleteCourier(Long id);
    CourierDto editCourier(Long id, CreateCourierDto createCourierDto);
}
