package com.BlueNote.demo.controller;

import com.BlueNote.demo.model.Tag;
import com.BlueNote.demo.repository.TagRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<Tag> createTag(@RequestBody Tag tag) {
        // Tu możesz dodać walidację, np. czy tag już istnieje, ale na razie:
        Tag savedTag = tagRepository.save(tag);
        return ResponseEntity.ok(savedTag);
    }
}
