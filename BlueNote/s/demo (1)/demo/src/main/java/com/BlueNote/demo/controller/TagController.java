package com.BlueNote.demo.controller;

import com.BlueNote.demo.model.Tag;
import com.BlueNote.demo.repository.TagRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagRepository tagRepository;

    public TagController(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @GetMapping
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> createTag(@RequestBody Tag tag) {
        if (tag.getName() == null || tag.getName().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Tag name cannot be empty");
        }

        Optional<Tag> existing = tagRepository.findByNameIgnoreCase(tag.getName().trim());
        if (existing.isPresent()) {
            // jeśli istnieje — możesz zwrócić go zamiast błędu
            return ResponseEntity.ok(existing.get());
        }

        if (tag.getColor() == null || tag.getColor().isBlank()) {
            tag.setColor("#1d4e9f"); // domyślny kolor
        }

        Tag savedTag = tagRepository.save(tag);
        return ResponseEntity.ok(savedTag);
    }
}
