package br.com.matteusmoreno.garage_manager.product.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
public class Product {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String brand;
    @Column(name = "purchase_price", precision = 10, scale = 2)
    private BigDecimal purchasePrice;
    @Column(name = "sale_price", precision = 10, scale = 2)
    private BigDecimal salePrice;
    @Column(name = "stock_quantity")
    private Integer stockQuantity;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
    @Column(name = "is_active")
    private Boolean isActive;
}
