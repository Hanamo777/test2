package com.example.backend.posts.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PostUpdateRequest {
    @NotBlank
    @Size(max = 100)
    public String title;

    @NotBlank
    @Size(max = 2000)
    public String content;
}

