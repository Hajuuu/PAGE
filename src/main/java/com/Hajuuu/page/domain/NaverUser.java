package com.Hajuuu.page.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "naver_user")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NaverUser {

    @Id
    @Column(name = "user_id", nullable = false)
    private int id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    private String snsId;

}
