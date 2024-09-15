package com.tms.tms.model;

import com.tms.tms.dto.OrderRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

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
    private OrderState state;

    @Column(nullable = false)
    private Long receiveStoreId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderLocation location;

    @Column(nullable = false)
    private Long startHubId;

    @Column(nullable = false)
    private Long endHubId;

    private boolean isDelete = false;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private Integer productQuantity;

    public Order(OrderRequestDto requestDto, Long endHubId, Store store, Product product){
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
    }
}
