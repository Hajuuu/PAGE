package com.Hajuuu.page.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Entity
@Getter
public class Book {

    @Id
    @GeneratedValue
    @Column(name = "book_id")
    private Long id;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();

    private String title;
    private String author;
    private Long page;
    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void createBook(String title, String author, Long page, String image) {
        this.title = title;
        this.author = author;
        this.page = page;
        this.image = image;
    }


}
