package com.tms.tms.model;


import com.tms.tms.dto.StoreRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "p_stores")
public class Store extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long storeId;

    @Column(nullable = false)
    private String storeName;

    private String storeAddress;

    private boolean isDelete = false;

    @Column(nullable = false)
    private String userId;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> productList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hub_id", nullable = false)
    private Hub hub;

    @OneToMany(mappedBy = "store")
    private List<Order> orderList = new ArrayList<>();


    public Store(StoreRequestDto storeRequestDto, String userId, Hub hub){
        this.storeName = storeRequestDto.getStoreName();
        this.storeAddress = storeRequestDto.getStoreAddress();
        this.hub = hub;
        this.userId = userId;
    }

    public void updateStore(StoreRequestDto storeRequestDto, Hub hub){
        this.storeName = storeRequestDto.getStoreName();
        this.storeAddress = storeRequestDto.getStoreAddress();
        this.hub = hub;
    }
}
