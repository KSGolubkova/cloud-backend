package coursework.cloudstorage.model.dto;

import java.io.Serializable;

public record FolderDTO(Long id, String name, Long parentId) implements Serializable {
}