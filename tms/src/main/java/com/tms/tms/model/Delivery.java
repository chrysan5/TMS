package com.tms.tms.model;

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
@Table(name = "p_deliveries")
public class Delivery  extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id")
    private Long deliveryId;

    private String address;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderLocation location = OrderLocation.PENDING; //pending, intra, inter

    @Column(nullable = false)
    private Long startHubId;

    @Column(nullable = false)
    private Long endHubId;

    private boolean isDelete = false;

    @OneToOne(mappedBy = "delivery")
    private Order order;

    public Delivery(String address, Long startHubId, Long endHubId, Order order){
        this.address = address;
        this.startHubId = startHubId;
        this.endHubId = endHubId;
        this.order = order;
    }
}
