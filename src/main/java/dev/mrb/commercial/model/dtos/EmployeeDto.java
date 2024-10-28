package dev.mrb.commercial.model.dtos;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeDto {
    private Long employeeId = null;
    private String firstName = null;
    private String lastName = null;
    private String contactNo = null;
    private String email = null;
    private Long officeId = null;
    private String officeAddress = null;
    private String designation = null;
    private String roles = null;
    private String specialInfo = null;
    private String username = null;
}
