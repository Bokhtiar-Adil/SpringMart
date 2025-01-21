package dev.mrb.commercial.repositories;

import dev.mrb.commercial.model.entities.CustomerEntity;
import dev.mrb.commercial.model.entities.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    @Query("SELECT orderId FROM OrderEntity WHERE customer = :customerEntity")
    List<Long> findOrderIdsByCustomer(Optional<CustomerEntity> customerEntity);

    @Modifying
    @Query("UPDATE OrderEntity o SET o.orderConfirmationToken = :token WHERE o.orderId = :orderId")
    void saveOrderConfirmationTokenById(@Param("orderId") Long orderId, @Param("token") String token);

}
