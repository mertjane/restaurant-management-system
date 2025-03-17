package com.management.management_crm;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    private TestRepository testRepository;

    @GetMapping("/test")
    public List<TestEntity> testConnection() {
        return testRepository.findAll(); // Returns all rows as a JSON array
    }
}