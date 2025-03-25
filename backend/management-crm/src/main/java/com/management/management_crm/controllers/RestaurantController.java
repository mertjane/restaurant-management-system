package com.management.management_crm.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.management.management_crm.dto.RestaurantDTO;
import com.management.management_crm.dto.UserDTO;
import com.management.management_crm.repository.RestaurantRepository;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {
    @Autowired
    private RestaurantRepository restaurantRepository;

    @GetMapping
    public List<RestaurantDTO> getAllRestaurants() {
        return restaurantRepository.findAll().stream().map(restaurant -> {
            UserDTO userDTO = new UserDTO(
                    restaurant.getUser().getId(),
                    restaurant.getUser().getEmail(),
                    restaurant.getUser().getResetToken(),
                    restaurant.getUser().getCreatedAt(),
                    restaurant.getUser().getUpdatedAt());

            return new RestaurantDTO(
                    restaurant.getId(),
                    restaurant.getName(),
                    restaurant.getWebsiteUrl(),
                    userDTO);
        }).collect(Collectors.toList());
    }

    // Fetch all restaurants by userId
    @GetMapping("/user/{userId}")
    public List<RestaurantDTO> getRestaurantsByUserId(@PathVariable Long userId) {
        return restaurantRepository.findByUserId(userId).stream().map(restaurant -> {
            UserDTO userDTO = new UserDTO(
                restaurant.getUser().getId(),
                restaurant.getUser().getEmail(),
                restaurant.getUser().getResetToken(),
                restaurant.getUser().getCreatedAt(),
                restaurant.getUser().getUpdatedAt()
            );

            return new RestaurantDTO(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getWebsiteUrl(),
                userDTO
            );
        }).collect(Collectors.toList());
    }
}
