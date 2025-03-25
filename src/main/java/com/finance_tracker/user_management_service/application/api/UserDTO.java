package com.finance_tracker.user_management_service.application.api;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserDTO {

 @NotBlank(message = "name is required.")
    private String name;

  @NotBlank(message = "email is required.")
    private String email;

 @NotBlank(message = "password is required.")
    private String password;

}
