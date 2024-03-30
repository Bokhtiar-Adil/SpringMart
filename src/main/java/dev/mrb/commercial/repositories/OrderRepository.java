package dev.mrb.commercial.repositories;

import dev.mrb.commercial.model.entities.CustomerEntity;
import dev.mrb.commercial.model.entities.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    @Query("SELECT orderId FROM OrderEntity WHERE customer = :customerEntity")
    List<Long> findOrderIdsByCustomer(Optional<CustomerEntity> customerEntity);
}
