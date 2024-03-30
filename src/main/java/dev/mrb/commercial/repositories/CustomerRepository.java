package dev.mrb.commercial.repositories;

import dev.mrb.commercial.model.entities.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
    @Query("SELECT customerId FROM CustomerEntity WHERE firstName = :name OR lastName = :name")
    List<Long> findIdByName(String name);
}
