package com.example.news_feed.member.entity;

import com.example.news_feed.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "member")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
<<<<<<< HEAD
=======
    @Column(name="id")
>>>>>>> b735579f6d538921d0fddf41f8d5a6b3bd8c7829
    private Long id;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name = "email",nullable = false)
    private String email;

    @Column(name = "password",nullable = false)
    private String password;

    public Member(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void update(String name) {
        this.name = name;
    }


    public void updatePassword(String encodePassword) {
        this.password = encodePassword;
    }
}
