package com.management.management_crm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    private TestRepository testRepository;

    @GetMapping("/test")
    public String testConnection() {
        testRepository.save(new TestEntity(1L, "Supabase Test"));
        return "Connection successful! Data saved.";
    }
}