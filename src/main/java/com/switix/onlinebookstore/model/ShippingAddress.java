package com.switix.onlinebookstore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "shipping_address")
public class ShippingAddress extends Address {

    @Column( nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "app_user_id")
    @JsonIgnore
    private AppUser appUser;

}