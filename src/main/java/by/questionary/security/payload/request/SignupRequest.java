package by.questionary.security.payload.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class SignupRequest {

    @NotBlank
    @Length(min = 3, max = 50)
    private String name;

    @NotBlank
    @Length(min = 3, max = 70)
    @Email
    private String email;

    @NotBlank
    @Length(min = 6, max = 50)
    private String password;

    @NotBlank
    @Length(min = 6, max = 50)
    private String repeatedPassword;

}
