package com.tms.auth.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

@FilterDef(name = "deletedFilter", parameters = @ParamDef(name = "isDeleted", type = Boolean.class))
@Filter(name = "deletedFilter", condition = "is_delete = :isDeleted")
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "p_users")
public class User extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role = UserRoleEnum.USER;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_user_id", nullable = false)
    private DeliveryUser deliveryUser;

    private Boolean isDelete = false;


    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void updateUser(String username, String password){
        this.username = username;
        this.password = password;
    }
}
