package com.maxifom.note_app.controller;

import com.maxifom.note_app.domain.MyUserDetails;
import com.maxifom.note_app.domain.Tag;
import com.maxifom.note_app.service.TagService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class TagController {
    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    private boolean userCannotAccess(Tag tag) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return true;
        }
        MyUserDetails authenticationDetails = (MyUserDetails) authentication.getPrincipal();
        Long currentUserId = authenticationDetails.getUserId();
        if (tag.getNotes().isEmpty()) {
            return false;
        }
        Long tagOwnerId = tag.getNotes().get(0).getUser().getId();

        return !currentUserId.equals(tagOwnerId) && !authenticationDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    @GetMapping("/user/tags")
    public String list(Authentication authentication, Model model,
                       SecurityContextHolderAwareRequestWrapper securityContextHolderAwareRequestWrapper) {
        MyUserDetails authenticationDetails = (MyUserDetails) authentication.getPrincipal();
        Long currentUserId = authenticationDetails.getUserId();
        boolean isAdmin = securityContextHolderAwareRequestWrapper.isUserInRole("ROLE_ADMIN");
        List<Tag> tags;
        if (isAdmin) {
            tags = tagService.findAll();
        } else {
            tags = tagService.findTagsByUserId(currentUserId);
        }
        model.addAttribute("tags", tags);
        return "tag/list";
    }

    @PutMapping("/user/tags/{tagId}")
    public String updateTag(@PathVariable Long tagId, @RequestParam String name) throws Exception {
        if (name.isEmpty()) {
            throw new Exception("name is empty");
        }
        Tag tag = tagService.findById(tagId);
        if (userCannotAccess(tag)) {
            return "redirect:/";
        }


        tagService.update(tag, name);
        return String.format("redirect:/user/tags/%d", tagId);
    }

    @GetMapping("/user/tags/{tagId}")
    public String show(@PathVariable Long tagId, Model model) throws Exception {
        Tag tag = tagService.findById(tagId);
        if (userCannotAccess(tag)) {
            return "redirect:/";
        }


        model.addAttribute("tag", tag);
        return "tag/show";
    }

    @DeleteMapping("/user/tags/{tagId}")
    public String deleteTag(@PathVariable Long tagId) throws Exception {
        Tag tag = tagService.findById(tagId);
        if (userCannotAccess(tag)) {
            return "redirect:/";
        }
        tagService.delete(tag);

        return "redirect:/user/tags";
    }
}
