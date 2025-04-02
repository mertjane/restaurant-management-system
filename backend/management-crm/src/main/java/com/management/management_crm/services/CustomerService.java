package com.management.management_crm.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.management.management_crm.dto.CustomerDTO;

import com.management.management_crm.models.CustomerEntity;
import com.management.management_crm.repository.CustomerRepository;


@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    // Constructor injection (preferred over @Autowired)
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // Search method for customers
    public Page<CustomerDTO> searchCustomers(Long restaurantId, String searchTerm, Pageable pageable) {
        Page<CustomerEntity> customers;

        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            customers = customerRepository.searchByRestaurantIdAndCustomerName(
                    restaurantId, searchTerm.trim(), pageable);
        } else {
            customers = customerRepository.findByRestaurantId(restaurantId, pageable);
        }

        return customers.map(customer -> new CustomerDTO(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhone()));
    }
}
