package com.tms.tms.model;

import com.tms.tms.dto.HubRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;
import java.util.ArrayList;
import java.util.List;

@SQLRestriction("is_delete = false")
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "p_hubs")
public class Hub extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hub_id")
    private Long hubId;

    @Column(nullable = false, unique = true)
    private String hubRegion;

    private String hubAddressX;

    private String hubAddressY;

    private boolean isDelete = false;

    @OneToMany(mappedBy = "hub")
    private List<Store> storeList = new ArrayList<>();

    public Hub(HubRequestDto hubRequestDto){
        this.hubRegion = hubRequestDto.getHubRegion();
        this.hubAddressX = hubRequestDto.getHubAddressX();
        this.hubAddressY = hubRequestDto.getHubAddressY();
    }

    public void updateHub(HubRequestDto hubRequestDto){
        this.hubRegion = hubRequestDto.getHubRegion();
        this.hubAddressX = hubRequestDto.getHubAddressX();
        this.hubAddressY = hubRequestDto.getHubAddressY();
    }

}
