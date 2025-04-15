package com.management.management_crm.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
@RequestMapping("/api/v1/customers") // Defined root endpoint
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
            Pageable pageable) { // <-- Let Spring handle page, size, and sort automatically

        Page<CustomerEntity> customerPage = customerRepository.findCustomersByUserId(userId, pageable);

        return customerPage.map(customer -> {
            RestaurantDTO restaurantDTO = new RestaurantDTO(
                    customer.getRestaurant().getId(),
                    customer.getRestaurant().getName(),
                    customer.getRestaurant().getWebsiteUrl(),
                    new UserDTO(
                            customer.getRestaurant().getUser().getId(),
                            customer.getRestaurant().getUser().getEmail(),
                            customer.getRestaurant().getUser().getResetToken(),
                            customer.getRestaurant().getUser().getCreatedAt(),
                            customer.getRestaurant().getUser().getUpdatedAt()));

            return new CustomerDTO(
                    customer.getId(),
                    customer.getName(),
                    customer.getEmail(),
                    customer.getPhone(),
                    customer.getCreatedAt(),
                    restaurantDTO);
        });
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
