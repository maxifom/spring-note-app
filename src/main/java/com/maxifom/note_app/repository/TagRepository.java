package com.maxifom.note_app.repository;

import com.maxifom.note_app.domain.Note;
import com.maxifom.note_app.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findAllByNotesIn(List<Note> notes);
}
