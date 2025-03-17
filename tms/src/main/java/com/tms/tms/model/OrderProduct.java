package com.tms.tms.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(
        name = "p_order_products",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_order_product", columnNames = {"order_id", "product_id"})
        }
)
public class OrderProduct  extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_product_id")
    private Long orderProductId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private int quantity;

    private BigDecimal price;

    public OrderProduct(Order order, Product product, int quantity, BigDecimal price){
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }
}
