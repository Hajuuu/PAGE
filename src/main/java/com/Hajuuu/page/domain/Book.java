package com.Hajuuu.page.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Getter;

@Entity
@Getter
public class Book {

    @Id @GeneratedValue
    @Column(name = "book_id")
    private Long id;

    @OneToOne(mappedBy = "book", fetch = FetchType.LAZY)
    private Post post;
    private String title;

    private String author;

    private Long page;
}
