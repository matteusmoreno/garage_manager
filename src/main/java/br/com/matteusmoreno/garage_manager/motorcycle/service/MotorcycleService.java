package br.com.matteusmoreno.garage_manager.motorcycle.service;

import br.com.matteusmoreno.garage_manager.customer.entity.Customer;
import br.com.matteusmoreno.garage_manager.exception.exception_class.MotorcycleAlreadyExistsException;
import br.com.matteusmoreno.garage_manager.motorcycle.entity.Motorcycle;
import br.com.matteusmoreno.garage_manager.motorcycle.repository.MotorcycleRepository;
import br.com.matteusmoreno.garage_manager.motorcycle.request.CreateMotorcycleRequest;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
            if (motorcycleRepository.findByLicensePlate(motorcycleRequest.licensePlate())) {
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
}
