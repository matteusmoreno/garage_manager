package br.com.matteusmoreno.garage_manager.service;

import br.com.matteusmoreno.garage_manager.domain.Customer;
import br.com.matteusmoreno.garage_manager.domain.Motorcycle;
import br.com.matteusmoreno.garage_manager.repository.MotorcycleRepository;
import br.com.matteusmoreno.garage_manager.request.CreateMotorcycleRequest;
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
