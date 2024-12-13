package coursework.cloudstorage.model.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class FileCreateRequest {
    private MultipartFile data;
    private Long folderId;
}
