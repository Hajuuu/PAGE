package com.Hajuuu.page.domain;

import com.Hajuuu.page.DTO.BaseTimeEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private int id;
    private String loginId;
    private String password;

    private String name;
    private String email;
    private String image;
    @Enumerated(EnumType.STRING)
    private Role role;

    private String provider;
    private String providerId;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Book> books = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private NaverUser naverUser;

    private List<Integer> followingList = new ArrayList<>();
    private List<Integer> followerList = new ArrayList<>();

    public User updateName(String name) {
        this.name = name;
        return this;
    }

    public void addFollowing(int id) {
        this.followingList.add(id);
    }

    public void cancelFollowing(int id) {
        this.followingList.remove(id);
    }

    public void addFollower(int id) {
        this.followerList.add(id);
    }

    public void cancelFollower(int id) {
        this.followerList.remove(id);
    }

    public String getRoleKey() {
        return this.role.getKey();
    }

}
