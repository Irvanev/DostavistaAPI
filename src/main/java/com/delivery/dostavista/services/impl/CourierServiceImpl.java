package com.delivery.dostavista.services.impl;

import com.delivery.dostavista.dtos.courierDtos.CourierDto;
import com.delivery.dostavista.dtos.courierDtos.CreateCourierDto;
import com.delivery.dostavista.models.entities.Couriers;
import com.delivery.dostavista.models.enums.UserStatusEnum;
import com.delivery.dostavista.repositories.CourierRepository;
import com.delivery.dostavista.services.CourierService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class CourierServiceImpl implements CourierService {

    private final CourierRepository courierRepository;
    private final ModelMapper modelMapper;

    public CourierServiceImpl(CourierRepository courierRepository, ModelMapper modelMapper) {
        this.courierRepository = courierRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public CourierDto createCourier(CreateCourierDto createCourierDto) {
        Couriers couriers = modelMapper.map(createCourierDto, Couriers.class);
        couriers.setCreatedAt(new Date());
        couriers.setStatus(UserStatusEnum.FREE);

        return modelMapper.map(courierRepository.saveAndFlush(couriers), CourierDto.class);
    }

    @Override
    public Page<CourierDto> getAllCouriers(Pageable pageable) {
        Page<Couriers> couriersPage = courierRepository.findAll(pageable);
        return couriersPage.map(courier -> modelMapper.map(courier, CourierDto.class));
    }

    @Override
    public Optional<CourierDto> getCourierById(Long id) {
        return courierRepository.findById(id).map(courier -> modelMapper.map(courier, CourierDto.class));
    }

    @Override
    public void deleteCourier(Long id) {
        courierRepository.deleteById(id);
    }

    @Override
    public CourierDto editCourier(Long id, CreateCourierDto createCourierDto) {
        Couriers couriers = courierRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Courier not found"));

        modelMapper.map(createCourierDto, couriers);

        return modelMapper.map(courierRepository.saveAndFlush(couriers), CourierDto.class);
    }
}
