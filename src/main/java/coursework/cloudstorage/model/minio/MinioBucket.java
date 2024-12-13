package coursework.cloudstorage.model.minio;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MinioBucket {
    private String name;
}