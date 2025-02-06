package com.tms.auth.model;

import com.tms.auth.dto.UserRequestDto;
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
@Table(name = "p_delivery_users")
public class DeliveryUser extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_user_id")
    private Long deliveryUserId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    private Long hubId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DeliveryType deliveryType;

    private boolean isDelete = false;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public DeliveryUser(User user, Long hubId) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.hubId = hubId;
        this.user = user;
    }

    public DeliveryUser(UserRequestDto userRequestDto, User user) {
        this.username = userRequestDto.getUsername();
        this.password = userRequestDto.getPassword();
        this.hubId = userRequestDto.getHubId();
        this.user = user;
        this.deliveryType = DeliveryType.valueOf(userRequestDto.getDeliveryType());
    }

    public void updateDeliveryUser(UserRequestDto requestDto, String password, User user){
        this.username = requestDto.getUsername();
        this.password = password;
        this.hubId = requestDto.getHubId();
        this.user = user;
        this.deliveryType = DeliveryType.valueOf(requestDto.getDeliveryType());
    }
}
