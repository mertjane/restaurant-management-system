package com.management.management_crm.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.management.management_crm.models.MenuEntity;
import com.management.management_crm.repository.MenuRepository;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuRepository menuRepository;

    // @Get Method for fetching all bookings
    @GetMapping()
    public List<MenuEntity> getAllMenuItems() {
        return menuRepository.findAll();
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<Page<MenuEntity>> getMenuItemsByRestaurant(
            @PathVariable Long restaurantId,
            Pageable pageable) {

        Page<MenuEntity> menu = menuRepository.findMenuByRestaurantId(restaurantId, pageable);
        return ResponseEntity.ok(menu);
    }
}
