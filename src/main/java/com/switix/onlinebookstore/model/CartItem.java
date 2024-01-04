package com.switix.onlinebookstore.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cart_item")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "shopping_session_id", nullable = false)
    private ShoppingSession shoppingSession;

    @OneToOne
    @JoinColumn(name = "book_id" , nullable = false)
    private Book book;

    @Column(nullable = false)
    private int quantity;
}
