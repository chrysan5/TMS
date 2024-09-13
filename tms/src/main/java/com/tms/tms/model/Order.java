package com.tms.tms.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@SQLRestriction("is_delete = false")
@Getter
@NoArgsConstructor
@Entity
@Table(name = "p_orders")
public class Order extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderState state;

    @Column(nullable = false)
    private String receive_store_id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderLocation location;

    @Column(nullable = false)
    private String startHubId;

    @Column(nullable = false)
    private String endHubId;

    private boolean isDelete = false;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private Integer productQuantity;

}
