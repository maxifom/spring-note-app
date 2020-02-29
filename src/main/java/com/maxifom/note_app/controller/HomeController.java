package com.maxifom.note_app.controller;

import com.maxifom.note_app.domain.MyUserDetails;
import com.maxifom.note_app.domain.Note;
import com.maxifom.note_app.service.NoteService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private final NoteService noteService;

    public HomeController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping("/")
    public String home(Model model, Authentication authentication,
                       SecurityContextHolderAwareRequestWrapper securityContextHolderAwareRequestWrapper) {
        if (authentication != null) {
            MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
            List<Note> notes;
            boolean isAdmin = securityContextHolderAwareRequestWrapper.isUserInRole("ROLE_ADMIN");
            notes = isAdmin ? noteService.findAll() : noteService.getAllByUserId(userDetails.getUserId());
            model.addAttribute("is_admin", isAdmin);
            model.addAttribute("notes", notes);
        }

        return "index";
    }
}
