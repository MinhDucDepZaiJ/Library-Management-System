package com.library.lms.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthorRequest {

    @NotBlank(message = "Tên tác giả không được để trống")
    private String name;

    private String biography;
}
