package br.com.matteusmoreno.garage_manager.service_order.entity;

import br.com.matteusmoreno.garage_manager.employee.entity.Employee;
import br.com.matteusmoreno.garage_manager.motorcycle.entity.Motorcycle;
import br.com.matteusmoreno.garage_manager.service_order.constant.ServiceOrderStatus;
import br.com.matteusmoreno.garage_manager.service_order_product.entity.ServiceOrderProduct;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "service_orders")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
public class ServiceOrder {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Motorcycle motorcycle;

    @ManyToOne
    private Employee seller;

    @ManyToOne
    private Employee mechanic;

    @OneToMany(mappedBy = "serviceOrder", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ServiceOrderProduct> products = new ArrayList<>();

    private String description;
    @Column(name = "labor_price")
    private BigDecimal laborPrice;
    @Column(name = "total_cost")
    private BigDecimal totalCost;

    @Enumerated(EnumType.STRING)
    @Column(name = "service_order_status")
    private ServiceOrderStatus serviceOrderStatus;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "started_at")
    private LocalDateTime startedAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name = "finished_at")
    private LocalDateTime finishedAt;
    @Column(name = "canceled_at")
    private LocalDateTime canceledAt;
}
