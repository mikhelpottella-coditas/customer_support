package com.example.customersupport.service;

import com.example.customersupport.dto.response.GenericResponse;
import com.example.customersupport.entity.Category;
import com.example.customersupport.exception.CustomException;
import com.example.customersupport.repo.CategoryRepo;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CategoryService {

    private final CategoryRepo categoryRepo;




    public GenericResponse addCategory(@NotBlank String issue) {
        Category category = Category.builder()
                .issue(issue)
                .build();
        categoryRepo.save(category);
        log.info("adding the new category with the issue : {}",issue);
        return new GenericResponse(HttpStatus.CREATED,"new category is added successfully!!");
    }

    public Category getById(Long category) {
        return categoryRepo.findById(category).orElseThrow(()-> new CustomException(HttpStatus.NOT_ACCEPTABLE,"category is not found"));
    }
}
