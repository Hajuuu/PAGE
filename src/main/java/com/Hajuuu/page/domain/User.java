package com.Hajuuu.page.domain;

import com.Hajuuu.page.DTO.BaseTimeEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Entity
@Getter
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private int id;
    @NotEmpty
    private String loginId;
    @NotEmpty
    private String password;
    private String name;
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Book> books = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();

    private List<Integer> followingList = new ArrayList<>();
    private List<Integer> followerList = new ArrayList<>();

    public void createUser(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
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


}
