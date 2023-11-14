package com.switix.onlinebookstore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorBookCountDto {
    private Long id;
    private String name;
    private Long bookCount;
}
