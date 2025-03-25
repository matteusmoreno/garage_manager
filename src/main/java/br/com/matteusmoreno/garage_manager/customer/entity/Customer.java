package br.com.matteusmoreno.garage_manager.customer.entity;

import br.com.matteusmoreno.garage_manager.address.entity.Address;
import br.com.matteusmoreno.garage_manager.motorcycle.entity.Motorcycle;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "customers")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
public class Customer {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    @Column(name = "birth_date")
    private String birthDate;
    private Integer age;
    private String phone;
    private String email;
    private String cpf;

    @ManyToOne(cascade = CascadeType.ALL)
    private Address address;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Motorcycle> motorcycles = new ArrayList<>();

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
    @Column(name = "is_active")
    private Boolean isActive;

}
