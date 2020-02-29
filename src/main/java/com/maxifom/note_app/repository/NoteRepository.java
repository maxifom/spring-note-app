package com.maxifom.note_app.repository;

import com.maxifom.note_app.domain.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository  extends JpaRepository<Note, Long> {
    List<Note> getAllByUserId(Long userId);
}
