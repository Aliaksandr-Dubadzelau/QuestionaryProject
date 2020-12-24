package by.questionary.security.payload.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class LoginRequest {

    @NotBlank(message = "Field name is empty")
    private String name;

    @NotBlank(message = "Field password is empty")
    private String password;

}
