package br.com.matteusmoreno.garage_manager.service_order_product.response;

import br.com.matteusmoreno.garage_manager.service_order_product.entity.ServiceOrderProduct;

import java.math.BigDecimal;

public record ServiceOrderProductResponse(
        String productName,
        String productBrand,
        Integer quantity,
        BigDecimal unitaryPrice,
        BigDecimal finalPrice) {

    public ServiceOrderProductResponse(ServiceOrderProduct serviceOrderProduct) {
        this(
                serviceOrderProduct.getProduct().getName(),
                serviceOrderProduct.getProduct().getBrand(),
                serviceOrderProduct.getQuantity(),
                serviceOrderProduct.getUnitaryPrice(),
                serviceOrderProduct.getFinalPrice()
        );
    }
}
