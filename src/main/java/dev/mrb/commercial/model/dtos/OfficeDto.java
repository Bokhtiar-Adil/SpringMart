package dev.mrb.commercial.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OfficeDto {

    private Long officeId;
    private String addressLine1;
    private String addressLine2;
    private Long address;
    private Long city;
    private String country;
}
