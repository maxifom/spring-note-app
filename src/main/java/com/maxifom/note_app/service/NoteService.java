package com.maxifom.note_app.service;

import com.maxifom.note_app.domain.Note;
import com.maxifom.note_app.repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NoteService {

    final private NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public Note findById(Long id) throws Exception {
        Optional<Note> note = noteRepository.findById(id);
        note.orElseThrow(()-> new Exception("Wrong note id"));
        return note.get();
    }

    public Note create(String title, String body, Long userId) {
        return noteRepository.save(new Note(title, body, userId));
    }

    public Note update(Note note, String title, String body) {
        note.setTitle(title);
        note.setBody(body);
        return noteRepository.save(note);
    }

    public void delete(Long noteId) {
        noteRepository.deleteById(noteId);
    }

    public List<Note> getAllByUserId(Long userId) {
        return noteRepository.getAllByUserId(userId);
    }

    public List<Note> findAll() {
        return noteRepository.findAll();
    }
}
