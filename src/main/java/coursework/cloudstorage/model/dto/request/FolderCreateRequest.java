package coursework.cloudstorage.model.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FolderCreateRequest {
    private String name;
    private Long folderId;
}
