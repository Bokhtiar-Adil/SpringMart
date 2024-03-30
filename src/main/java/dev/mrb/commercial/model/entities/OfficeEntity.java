package dev.mrb.commercial.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "offices")
public class OfficeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long officeId;
    private String addressLine1;
    private String addressLine2;
    private Long address;
    private Long city;
    private String country;
}
