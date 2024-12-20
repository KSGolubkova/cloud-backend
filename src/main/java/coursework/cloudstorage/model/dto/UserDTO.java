package coursework.cloudstorage.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private Long id;
    private String email;
    private String username;
    private String role;
}
