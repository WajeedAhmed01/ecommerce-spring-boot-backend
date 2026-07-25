package com.wajeed.ecommerce.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegistrationRequest
{
    @NotBlank(message = "name cant be empty")
    String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "email cant be blank")
    String email;
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(
            // Added \\. and other missing standard symbols inside the brackets
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&\\.#_\\-])[A-Za-z\\d@$!%*?&\\.#_\\-]{8,}$",
            message = "Password must be at least 8 characters long, containing at least one uppercase letter, one lowercase letter, one number, and one special character."
    )
    private String password;
}
