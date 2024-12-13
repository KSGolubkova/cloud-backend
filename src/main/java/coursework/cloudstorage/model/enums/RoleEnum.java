package coursework.cloudstorage.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RoleEnum {
    ROLE_USER("Пользователь"),
    ROLE_ADMIN("Администратор");

    private final String value;
}