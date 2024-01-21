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
@Table(name = "billing_address")
public class BillingAddress extends Address {

    @Column( nullable = false)
    private String phoneNumber;

    @OneToOne()
    @JoinColumn(name = "app_user_id", unique = true)
    @JsonIgnore
    private AppUser appUser;

}