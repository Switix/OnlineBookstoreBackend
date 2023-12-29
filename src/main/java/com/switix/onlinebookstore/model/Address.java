package com.switix.onlinebookstore.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String street;

    @ManyToOne(cascade = CascadeType.MERGE)
    private City city;

    @Column(nullable = false)
    private String zipCode;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Country country;

    @Column(nullable = false)
    private String buildingNumber;

    private String apartmentNumber;
}