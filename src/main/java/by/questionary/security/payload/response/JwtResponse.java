package by.questionary.security.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class JwtResponse {

    private static final String TYPE = "Bearer";
    private String token;
    private long id;
    private String name;
    private String email;
    private boolean activated;
    private List<String> roles;

}
