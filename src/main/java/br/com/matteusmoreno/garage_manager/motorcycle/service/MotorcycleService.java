package br.com.matteusmoreno.garage_manager.motorcycle.service;

import br.com.matteusmoreno.garage_manager.customer.entity.Customer;
import br.com.matteusmoreno.garage_manager.exception.exception_class.MotorcycleAlreadyExistsException;
import br.com.matteusmoreno.garage_manager.exception.exception_class.MotorcycleIsAlreadyDisabledException;
import br.com.matteusmoreno.garage_manager.exception.exception_class.MotorcycleIsAlreadyEnabledException;
import br.com.matteusmoreno.garage_manager.exception.exception_class.MotorcycleNotFoundException;
import br.com.matteusmoreno.garage_manager.motorcycle.entity.Motorcycle;
import br.com.matteusmoreno.garage_manager.motorcycle.repository.MotorcycleRepository;
import br.com.matteusmoreno.garage_manager.motorcycle.request.CreateMotorcycleRequest;
import br.com.matteusmoreno.garage_manager.motorcycle.request.UpdateMotorcycleRequest;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class MotorcycleService {

    private final MotorcycleRepository motorcycleRepository;
    private final MeterRegistry meterRegistry;

    public MotorcycleService(MotorcycleRepository motorcycleRepository, MeterRegistry meterRegistry) {
        this.motorcycleRepository = motorcycleRepository;
        this.meterRegistry = meterRegistry;
    }

    @Transactional
    public List<Motorcycle> createMotorcycle(List<CreateMotorcycleRequest> request, Customer customer) {
        List<Motorcycle> motorcycles = new ArrayList<>();

        for (CreateMotorcycleRequest motorcycleRequest : request) {
            if (motorcycleRepository.existsByLicensePlate(motorcycleRequest.licensePlate())) {
                meterRegistry.counter("motorcycles_exists").increment();
                throw new MotorcycleAlreadyExistsException("Motorcycle already exists");
            }
            Motorcycle motorcycle = Motorcycle.builder()
                    .brand(motorcycleRequest.brand())
                    .model(motorcycleRequest.model())
                    .year(motorcycleRequest.year().toUpperCase())
                    .color(motorcycleRequest.color().toUpperCase())
                    .licensePlate(motorcycleRequest.licensePlate().toUpperCase())
                    .customer(customer)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(null)
                    .deletedAt(null)
                    .isActive(true)
                    .build();

            meterRegistry.counter("motorcycle_created").increment();
            motorcycleRepository.persist(motorcycle);
            motorcycles.add(motorcycle);
        }

        return motorcycles;
    }

    @Transactional
    public Motorcycle findMotorcycleById(UUID id) {
        if (motorcycleRepository.findByUUID(id) == null) {
            meterRegistry.counter("motorcycle_not_found_by_id").increment();
            throw new MotorcycleNotFoundException("Motorcycle not found");
        }

        meterRegistry.counter("motorcycle_found_by_id").increment();
        return motorcycleRepository.findByUUID(id);
    }

    @Transactional
    public Motorcycle findMotorcycleByLicensePlate(String licensePlate) {
        if (motorcycleRepository.findByLicensePlate(licensePlate) == null) {
            meterRegistry.counter("motorcycle_not_found_by_license_plate").increment();
            throw new MotorcycleNotFoundException("Motorcycle not found");
        }

        meterRegistry.counter("motorcycle_found_by_license_plate").increment();
        return motorcycleRepository.findByLicensePlate(licensePlate);
    }

    @Transactional
    public Motorcycle updateMotorcycle(UpdateMotorcycleRequest request) {
        Motorcycle motorcycle = findMotorcycleById(request.id());

        if (request.brand() != null) motorcycle.setBrand(request.brand());
        if (request.model() != null) motorcycle.setModel(request.model());
        if (request.year() != null) motorcycle.setYear(request.year());
        if (request.color() != null) motorcycle.setColor(request.color());
        if (request.licensePlate() != null) motorcycle.setLicensePlate(request.licensePlate());

        motorcycle.setUpdatedAt(LocalDateTime.now());
        meterRegistry.counter("motorcycle_updated").increment();
        motorcycleRepository.persist(motorcycle);

        return motorcycle;
    }

    @Transactional
    public void disableMotorcycle(UUID id) {
        Motorcycle motorcycle = findMotorcycleById(id);

        if (!motorcycle.getIsActive()) {
            meterRegistry.counter("motorcycle_already_disabled").increment();
            throw new MotorcycleIsAlreadyDisabledException("Motorcycle is already disabled");
        }

        motorcycle.setDeletedAt(LocalDateTime.now());
        motorcycle.setIsActive(false);
        meterRegistry.counter("motorcycle_deleted").increment();
        motorcycleRepository.persist(motorcycle);
    }

    @Transactional
    public Motorcycle enableMotorcycle(UUID id) {
        Motorcycle motorcycle = findMotorcycleById(id);

        if (motorcycle.getIsActive()) {
            meterRegistry.counter("motorcycle_already_enabled").increment();
            throw new MotorcycleIsAlreadyEnabledException("Motorcycle is already enabled");
        }
        motorcycle.setUpdatedAt(LocalDateTime.now());
        motorcycle.setDeletedAt(null);
        motorcycle.setIsActive(true);
        meterRegistry.counter("motorcycle_enabled").increment();
        motorcycleRepository.persist(motorcycle);
        return motorcycle;
    }
}
