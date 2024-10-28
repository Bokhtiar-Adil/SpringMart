package dev.mrb.commercial.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderDetailsRepository extends JpaRepository<OrderDetailsEntity, Long> {

    @Query("SELECT entryId FROM OrderDetailsEntity WHERE orderId = :queriedOrderId")
    Long findByOrderId(Long queriedOrderId);
}
