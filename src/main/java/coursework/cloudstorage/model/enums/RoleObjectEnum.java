package coursework.cloudstorage.model.enums;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@AllArgsConstructor
public enum RoleObjectEnum {
    READER("Просмотр"), AUTHOR("Автор");

    private final String description;

    public static RoleObjectEnum getByName(String name) {
        log.info("{} {}", READER.name(), name);
        return Arrays.stream(RoleObjectEnum.values()).filter(r -> r.name().equals(name)).findFirst()
                .orElseThrow(() -> new RuntimeException("Role " + name + " not found"));
    }
}
