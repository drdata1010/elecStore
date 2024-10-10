package com.soft.electronic.store.dtos;

import com.soft.electronic.store.validate.ImageNameValid;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private String userId;
    @Size(min=3,max=15,message = "Invalid Name !!")
    private String name;

//    @Email(message = "Invalid User Email !!")
    @Pattern(regexp = "^[a-z0-9][-a-z0-9._]+@([-a-z0-9]+\\.)+[a-z]{2,5}$",message = "Email is invalid")
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank
    private String password;
    @Size(min = 4,max = 6,message = "Invalid Gender !!")
    private String gender;
    @NotBlank
    private String about;

    // @Pattern for regex expression validation
    // Custom Validator
    @ImageNameValid
    private String imageName;
}
