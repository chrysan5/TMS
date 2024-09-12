package com.tms.tms.model;



import com.tms.tms.dto.ProductRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "p_products")
public class Product extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(nullable = false, unique = true)
    private String productName;

    @Column(nullable = false)
    private Integer productPrice;

    private boolean isDelete = false;

    private Long hubId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    public Product(ProductRequestDto productRequestDto, Store store, Long hubId){
        this.productName = productRequestDto.getProductName();
        this.productPrice = productRequestDto.getProductPrice();
        this.store = store;
        this.hubId = hubId;
    }

    public void updateProduct(ProductRequestDto productRequestDto, Store store, Long hubId){
        this.productName = productRequestDto.getProductName();
        this.productPrice = productRequestDto.getProductPrice();
        this.store = store;
        this.hubId = hubId;
    }
}
