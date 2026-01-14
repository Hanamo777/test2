package com.example.backend.posts;

import com.example.backend.posts.dto.PostCreateRequest;
import com.example.backend.posts.dto.PostUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostRepository postRepository;

    public PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @GetMapping
    public List<Post> list() {
        return postRepository.findAll()
                .stream()
                .sorted((a, b) -> b.getId().compareTo(a.getId()))
                .toList();
    }

    @GetMapping("/{id}")
    public Post get(@PathVariable Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Post create(@Valid @RequestBody PostCreateRequest req) {
        Post p = new Post();
        p.setTitle(req.title);
        p.setContent(req.content);
        return postRepository.save(p);
    }

    @PutMapping("/{id}")
    public Post update(@PathVariable Long id, @Valid @RequestBody PostUpdateRequest req) {
        Post p = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));
        p.setTitle(req.title);
        p.setContent(req.content);
        return postRepository.save(p);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!postRepository.existsById(id)) {
            throw new PostNotFoundException(id);
        }
        postRepository.deleteById(id);
    }
}

