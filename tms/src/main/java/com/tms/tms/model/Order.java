package com.tms.tms.model;

import com.tms.tms.dto.OrderRequestDto;
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
    private Long userId; //주문한 업체 유저 아이디

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store; //주문하는 업체

    @Column(nullable = false)
    private Long sellerStoreId; //주문받는 업체

    @Column(nullable = false)
    private BigDecimal totalQuantity;

    @Column(nullable = false)
    private BigDecimal totalPrice;


    private boolean isDelete = false;


    @OneToOne
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderProduct> orderProducts = new ArrayList<>();

    /*public Order(OrderRequestDto requestDto, Long endHubId, Store store, Product product){
        this.state = OrderState.ORDERED;
        this.receiveStoreId = requestDto.getReceiveStoreId();
        this.location = OrderLocation.PENDING;
        this.startHubId = store.getHub().getHubId();
        this.endHubId = endHubId;
        this.store = store;
        this.product = product;
        this.productQuantity = requestDto.getProductQuantity();
    }

    public void updateOrder(OrderRequestDto requestDto, Long endHubId, Product product){
        this.state = OrderState.ORDERED;
        this.receiveStoreId = requestDto.getReceiveStoreId();
        this.location = OrderLocation.PENDING;
        this.startHubId = store.getHub().getHubId();
        this.endHubId = endHubId;
        this.product = product;
        this.productQuantity = requestDto.getProductQuantity();
    }*/
}
