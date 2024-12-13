package coursework.cloudstorage.model.dto;

import java.io.Serializable;

public record FileDTO(Long id, String name, long size, Long parentId) implements Serializable {
}