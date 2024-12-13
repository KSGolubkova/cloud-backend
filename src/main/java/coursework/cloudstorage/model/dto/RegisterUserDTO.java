package coursework.cloudstorage.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RegisterUserDTO {
    @NotNull(message = "поле username должно быть задано")
    private String username;
    @NotNull(message = "поле password должно быть задано")
    private String password;
    @NotNull(message = "поле passwordConfirm должно быть задано")
    private String passwordConfirm;
}
