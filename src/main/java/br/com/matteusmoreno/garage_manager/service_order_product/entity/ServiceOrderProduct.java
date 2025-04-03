package br.com.matteusmoreno.garage_manager.service_order_product.entity;

import br.com.matteusmoreno.garage_manager.product.entity.Product;
import br.com.matteusmoreno.garage_manager.service_order.entity.ServiceOrder;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "service_order_products")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
public class ServiceOrderProduct {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "service_order_id")
    private ServiceOrder serviceOrder;
    @ManyToOne
    private Product product;
    private Integer quantity;
    @Column(name = "unitary_price")
    private BigDecimal unitaryPrice;
    @Column(name = "final_price")
    private BigDecimal finalPrice;
}
