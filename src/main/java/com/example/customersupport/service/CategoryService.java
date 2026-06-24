package com.example.customersupport.service;

import com.example.customersupport.entity.Category;
import com.example.customersupport.exception.CustomException;
import com.example.customersupport.repo.CategoryRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryRepo categoryRepo;


    public Category getByIssue(String filter) {
        return categoryRepo.findCategoryByIssue(filter).orElseThrow(()-> new CustomException(HttpStatus.NOT_FOUND,"provided category is not available"));
    }
}
