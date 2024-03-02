package com.switix.onlinebookstore.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "order_detail")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "app_user_id",nullable = false)
    private AppUser appUser;

    @Column(nullable = false)
    private BigDecimal total;

    @ManyToOne
    @JoinColumn(name = "billing_address_id",nullable = false)
    private BillingAddress billingAddress;

    @ManyToOne
    @JoinColumn(name = "shipping_address_id",nullable = false)
    private ShippingAddress shippingAddress;

    @ManyToOne
    @JoinColumn(name = "pay_method_id",nullable = false)
    private PayMethod payMethod;

    @ManyToOne
    @JoinColumn(name = "shipment_method_id",nullable = false)
    private ShipmentMethod shipmentMethod;

    @CreationTimestamp
    private Instant createdAt;

}
