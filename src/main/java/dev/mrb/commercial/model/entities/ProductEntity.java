package dev.mrb.commercial.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Blob;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "products")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long productId;
    private String name;
    private String productLineDesc;

    private Long stockNum;
    private String producer;
    private String description;
    private Long inStock;
    private Long buyPrice;
    private Long productionCost;
    private Long sellPrice;
    private Long mrp;
    private Blob photo;
}
