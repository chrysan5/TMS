package com.tms.tms.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SQLRestriction("is_delete = false")
@Setter
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
    private OrderState state = OrderState.ORDERED; //orderd, canceled, completed

    @Column(nullable = false)
    private String username; //주문한 업체 유저아이디

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store; //주문하는 업체

    @Column(nullable = false)
    private Long sellerStoreId; //주문받는 업체

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    private List<OrderProduct> orderProducts = new ArrayList<>();

    private int totalQuantity;

    private BigDecimal totalPrice = BigDecimal.ZERO;

    private boolean isDelete = false;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id", nullable = false)
    private Delivery delivery;


    public Order(String username, Store requestStore, Long sellerStoreId){
        this.username = username;
        this.store = requestStore;
        this.sellerStoreId = sellerStoreId;
    }

    public void updateOrder(List<OrderProduct> orderProducts, int totalQuantity, BigDecimal totalPrice){
        this.state = OrderState.ORDERED;
        this.orderProducts = orderProducts;
        this.totalQuantity = totalQuantity;
        this.totalPrice = totalPrice;
    }

}
