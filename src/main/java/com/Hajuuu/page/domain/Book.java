package com.Hajuuu.page.domain;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Getter;

@Entity
@DiscriminatorValue("B")
@Getter
public class Book extends Post {

    private String title;
    private String author;
    private Long page;
}
