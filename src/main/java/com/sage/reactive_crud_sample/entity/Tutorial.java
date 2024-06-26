package com.sage.reactive_crud_sample.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tutorial {
    @Id
    private Long id;
    private String title;
    private String description;
    private boolean published;
}
