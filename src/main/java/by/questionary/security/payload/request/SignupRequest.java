package by.questionary.security.payload.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class SignupRequest {

    @NotBlank(message = "Field name is empty.")
    @Length(min = 3, max = 50, message = "Name must be (3-50) size.")
    private String name;

    @NotBlank(message = "Field email is empty.")
    @Length(min = 3, max = 70, message = "Email must be (3-70) size.")
    @Email(message = "Wrong email format.")
    private String email;

    @NotBlank(message = "Field password is empty.")
    @Length(min = 6, max = 50, message = "Password must be (6-50) size.")
    private String password;

    @NotBlank(message = "Field repeated password is empty.")
    @Length(min = 6, max = 50, message = "Repeated password must be (6-50) size.")
    private String repeatedPassword;

}
