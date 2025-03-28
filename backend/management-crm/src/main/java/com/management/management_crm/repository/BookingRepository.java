package com.management.management_crm.repository;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.management.management_crm.models.BookingEntity;

public interface BookingRepository extends JpaRepository<BookingEntity, Long> {
    // Custom query to find bookings belongs to user's restaurant
    @Query("SELECT b FROM BookingEntity b WHERE b.restaurant.id = :restaurantId")
    Page<BookingEntity> findBookingsByRestaurantId(@Param("restaurantId") Long restaurantId, Pageable pageable);

}
