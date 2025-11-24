package com.Hajuuu.page.domain;

import com.Hajuuu.page.DTO.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String content;
    private Long startPage;
    private Long endPage;

    public String getBookName() {
        return book.getTitle();
    }


    public void createPost(Book book, User user, String content, Long startPage, Long endPage) {
        this.book = book;
        this.user = user;
        this.content = content;
        this.startPage = startPage;
        this.endPage = endPage;
    }
}
