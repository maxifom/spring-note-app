package com.maxifom.note_app.controller;

import com.maxifom.note_app.domain.MyUserDetails;
import com.maxifom.note_app.domain.Note;
import com.maxifom.note_app.service.NoteService;
import com.maxifom.note_app.service.TagService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class NoteController {
    private final NoteService noteService;
    private final TagService tagService;

    public NoteController(NoteService noteService, TagService tagService) {
        this.noteService = noteService;
        this.tagService = tagService;
    }

    private boolean userCannotAccess(Note note) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return true;
        }
        MyUserDetails authenticationDetails = (MyUserDetails) authentication.getPrincipal();
        Long currentUserId = authenticationDetails.getUserId();

        return !currentUserId.equals(note.getUser().getId()) && !authenticationDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    @GetMapping("/user/notes/{noteId}")
    public String showNote(@PathVariable Long noteId, Model model) throws Exception {
        Note note = noteService.findById(noteId);
        if (userCannotAccess(note)) {
            return "redirect:/";
        }
        model.addAttribute("note", noteService.findById(noteId));

        return "note/show";
    }

    @PostMapping("/user/notes")
    public String addNote(@RequestParam String title, @RequestParam String body, Authentication authentication) throws Exception {
        if (title.isEmpty() || body.isEmpty()) {
            throw new Exception("Required fields missing");
        }
        MyUserDetails authenticationDetails = (MyUserDetails) authentication.getPrincipal();
        Long currentUserId = authenticationDetails.getUserId();
        Note note = noteService.create(title, body, currentUserId);

        return String.format("redirect:/user/notes/%d", note.getId());
    }

    @PutMapping("/user/notes/{noteId}")
    public String updateNote(@PathVariable Long noteId, @RequestParam String title, @RequestParam String body) throws Exception {
        if (title.isEmpty() || body.isEmpty()) {
            throw new Exception("Required fields missing");
        }
        Note note = noteService.findById(noteId);
        if (userCannotAccess(note)) {
            return "redirect:/";
        }

        noteService.update(note, title, body);

        return String.format("redirect:/user/notes/%d", noteId);
    }

    @PostMapping("/user/notes/{noteId}/tags")
    public String addTagToNote(@PathVariable Long noteId, @RequestParam String name) throws Exception {
        if (name.isEmpty()) {
            throw new Exception("Required fields missing");
        }
        Note note = noteService.findById(noteId);
        if (userCannotAccess(note)) {
            return "redirect:/";
        }
        tagService.addTagToNote(noteId, name);

        return String.format("redirect:/user/notes/%d", note.getId());
    }

    @DeleteMapping("/user/notes/{noteId}/tags/{tagId}")
    public String deleteTagFromNote(@PathVariable Long noteId, @PathVariable Long tagId) throws Exception {
        Note note = noteService.findById(noteId);
        if (userCannotAccess(note)) {
            return "redirect:/";
        }

        tagService.deleteTagFromNote(tagId, noteId);

        return String.format("redirect:/user/notes/%d", note.getId());
    }

    @DeleteMapping("/user/notes/{noteId}")
    public String deleteNote(@PathVariable Long noteId) throws Exception {
        Note note = noteService.findById(noteId);
        if (userCannotAccess(note)) {
            return "redirect:/";
        }
        noteService.delete(noteId);

        return "redirect:/";
    }
}
