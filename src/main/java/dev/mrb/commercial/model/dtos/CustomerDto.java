package dev.mrb.commercial.model.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerDto {

    private Long customerId;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String address;
    private String status = null;

    private String username;
    private String password;
}
