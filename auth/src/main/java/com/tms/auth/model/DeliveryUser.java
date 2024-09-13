package com.tms.auth.model;

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

    @Column(nullable = false)
    private Long hubId;

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

    public void updateDeliveryUser(String username, String password){
        this.username = username;
        this.password = password;
    }
}
