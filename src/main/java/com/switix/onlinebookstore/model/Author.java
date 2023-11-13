package com.switix.onlinebookstore.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.switix.onlinebookstore.modelSerializer.AuthorSerializer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@JsonSerialize(using = AuthorSerializer.class)
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToMany(mappedBy = "bookAuthors")
    private Set<Book> authorBooks = new HashSet<>();

}
