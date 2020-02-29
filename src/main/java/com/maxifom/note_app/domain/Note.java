package com.maxifom.note_app.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "notes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String body;

    @ManyToMany
    @JoinTable(name = "notes_tags", joinColumns = @JoinColumn(name="note_id"),
    inverseJoinColumns = @JoinColumn(name="tag_id"))
    private Set<Tag> tags;

    @ManyToOne
    private User user;

    public Note(String title, String body, Long userId) {
        this.title = title;
        this.body = body;
        this.user = new User(userId);
    }

    public Note(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public User getUser() {
        return user;
    }
}
