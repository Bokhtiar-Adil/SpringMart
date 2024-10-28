package dev.mrb.commercial.model.dtos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Blob;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {
    private Long productId;
    private String name;
    private String productLineDesc;
    private Long stockNum;
    private String producer;
    private String description;
    private Long quantityInStock;
    private Long buyPrice;
    private Long productionCost;
    private Long sellPrice;
    private Long mrp;
    private String photoUrl;
}
