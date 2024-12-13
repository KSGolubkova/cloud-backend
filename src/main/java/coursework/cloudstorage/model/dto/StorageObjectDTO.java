package coursework.cloudstorage.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StorageObjectDTO {
    private Long id;
    private String name;
    private Boolean isFile;
    private Long size;
    private Long folderId;
    private String role;
}
