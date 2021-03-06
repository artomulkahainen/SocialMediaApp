package com.someapp.backend.entities;

import com.sun.istack.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Entity
public class Post extends AbstractPersistable<UUID> {

    @NotNull
    @Size(min = 1, max = 250)
    private String post;

    @ManyToOne
    @NotNull
    private User user;

    @OneToMany(mappedBy = "post", cascade = { CascadeType.REMOVE })
    private List<PostComment> postComments;

    @OneToMany(cascade = { CascadeType.REMOVE })
    @JoinColumn(name = "POST_ID")
    private List<PostLike> postLikes;

    @CreationTimestamp
    private Timestamp createdDate;

    public Post(String post, User user) {
        this.post = post;
        this.user = user;
    }

    public Post() {};

    public List<PostLike> getPostLikes() { return postLikes; }

    public String getPost() {
        return post;
    }

    public UUID getUserId() {
        return user.getId();
    }

    public List<PostComment> getPostComments() {
        return postComments;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public UUID getUUID() {
        return this.getId();
    }
}
