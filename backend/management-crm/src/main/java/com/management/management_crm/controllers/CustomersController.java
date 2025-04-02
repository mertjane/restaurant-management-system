package com.management.management_crm.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.management.management_crm.dto.CustomerDTO;
import com.management.management_crm.dto.RestaurantDTO;
import com.management.management_crm.dto.UserDTO;
import com.management.management_crm.models.CustomerEntity;
import com.management.management_crm.repository.CustomerRepository;
import com.management.management_crm.services.CustomerService;


@RestController
@RequestMapping("/customers") // Defined root endpoint
public class CustomersController {
    
   
    private final CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    
    public CustomersController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // @Get Method for fetching all customers
    @GetMapping() // No need to give endpoint we have already defined root endpoint above
    public List<CustomerEntity> getAllCustomers() {
        return customerRepository.findAll();
    }

    @GetMapping("/user/{userId}")
    public Page<CustomerDTO> getCustomersByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        // Create a PageRequest object for pagination
        PageRequest pageRequest = PageRequest.of(page, size);

        // Fetch the paginated result from the repository
        Page<CustomerEntity> customerPage = customerRepository.findCustomersByUserId(userId, pageRequest);

        // Convert the paginated results to a Page of CustomerDTOs
        Page<CustomerDTO> customerDTOPage = customerPage.map(customer -> {
            // Map RestaurantEntity to RestaurantDTO first
            RestaurantDTO restaurantDTO = new RestaurantDTO(
                    customer.getRestaurant().getId(),
                    customer.getRestaurant().getName(),
                    customer.getRestaurant().getWebsiteUrl(),
                    // Map UserEntity to UserDTO inside RestaurantDTO
                    new UserDTO(
                            customer.getRestaurant().getUser().getId(),
                            customer.getRestaurant().getUser().getEmail(),
                            customer.getRestaurant().getUser().getResetToken(),
                            customer.getRestaurant().getUser().getCreatedAt(),
                            customer.getRestaurant().getUser().getUpdatedAt()));

            // Map CustomerEntity to CustomerDTO
            return new CustomerDTO(
                    customer.getId(),
                    customer.getName(),
                    customer.getEmail(),
                    customer.getPhone(),
                    restaurantDTO);
        });

        // Return the Page of CustomerDTOs
        return customerDTOPage;
    }

    // New search endpoint
    @GetMapping("/restaurant/{restaurantId}/search")
    public ResponseEntity<Page<CustomerDTO>> searchCustomers(
            @PathVariable Long restaurantId,
            @RequestParam(required = false) String searchTerm,
            Pageable pageable) {

        Page<CustomerDTO> searchResults = customerService.searchCustomers(restaurantId, searchTerm, pageable);
        return ResponseEntity.ok(searchResults);
    }
}
