package com.management.management_crm.repository;

/* import java.util.Optional; */


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.management.management_crm.models.CustomerEntity;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
    // Custom query to find customers based on user's restaurant
    @Query("SELECT c FROM CustomerEntity c WHERE c.restaurant.user.id = :userId")
    Page<CustomerEntity> findCustomersByUserId(Long userId, Pageable pageable);

}
