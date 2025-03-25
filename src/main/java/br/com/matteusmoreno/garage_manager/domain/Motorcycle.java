package br.com.matteusmoreno.garage_manager.domain;

import br.com.matteusmoreno.garage_manager.constant.MotorcycleBrand;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "motorcycles")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
public class Motorcycle {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Enumerated(EnumType.STRING)
    private MotorcycleBrand brand;
    private String model;
    private String year;
    private String color;
    @Column(name = "license_plate")
    private String licensePlate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
    @Column(name = "is_active")
    private Boolean isActive;
}
