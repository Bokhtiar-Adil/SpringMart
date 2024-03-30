package dev.mrb.commercial.model.dtos;


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
public class OrderDetailsDto {

    private Long entryId;
    private OrderDto orderId;
    private List<ProductDto> products;
    private List<Long> quantities;
    private List<Long> prices;

}
