package com.maxifom.note_app.service;

import com.maxifom.note_app.domain.Note;
import com.maxifom.note_app.domain.Tag;
import com.maxifom.note_app.repository.NoteRepository;
import com.maxifom.note_app.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class TagService {

    private final TagRepository tagRepository;
    private final NoteRepository noteRepository;

    public TagService(TagRepository tagRepository, NoteRepository noteRepository) {
        this.tagRepository = tagRepository;
        this.noteRepository = noteRepository;
    }

    public Tag addTagToNote(Long noteId, String name) {
        List<Note> notes = Collections.singletonList(new Note(noteId));
        Tag tag = new Tag(name, notes);

        return tagRepository.save(tag);
    }

    public void deleteTagFromNote(Long tagId, Long noteId) throws Exception {
        Optional<Tag> tag = tagRepository.findById(tagId);
        tag.orElseThrow(() -> new Exception(String.format("no tag found by id %d", tagId)));
        tag.get().getNotes().removeIf(n -> n.getId().equals(noteId));
        if (tag.get().getNotes().isEmpty()) {
            tagRepository.delete(tag.get());
        } else {
            tagRepository.save(tag.get());
        }

    }

    public List<Tag> findTagsByUserId(Long currentUserId) {
        List<Note> notes = noteRepository.getAllByUserId(currentUserId);
        return tagRepository.findAllByNotesIn(notes);
    }

    public Tag findById(Long tagId) throws Exception {
        Optional<Tag> tag = tagRepository.findById(tagId);
        tag.orElseThrow(()-> new Exception(String.format("Tag with id %d does not exists", tagId)));
        return tag.get();
    }

    public void delete(Tag tag) {
        tagRepository.delete(tag);
    }

    public List<Tag> findAll() {
        return tagRepository.findAll();
    }

    public Tag update(Tag tag, String name) {
        tag.setName(name);
        return tagRepository.save(tag);
    }
}
