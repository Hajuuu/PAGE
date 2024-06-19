package com.Hajuuu.page.domain;

import com.Hajuuu.page.api.BookState;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue
    @Column(name = "book_id")
    private int id;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();

    private String title;
    private String author;
    private Long page;
    private String image;
    private BookState bookState;
    private String isbn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    public void createBook(User user, String title, String author, Long page, String image, String isbn,
                           BookState bookState) {
        this.user = user;
        this.title = title;
        this.author = author;
        this.page = page;
        this.image = image;
        this.isbn = isbn;
        this.bookState = bookState;
    }

    public void changePage(Long page) {
        this.page = page;
    }


}
