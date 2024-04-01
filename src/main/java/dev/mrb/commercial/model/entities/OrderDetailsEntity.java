package dev.mrb.commercial.model.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "order_details")
public class OrderDetailsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long entryId;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private OrderEntity orderId;

    @OneToOne(targetEntity = ProductEntity.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ProductEntity> products;
    private List<Long> quantities;
    private List<Long> prices;

}
