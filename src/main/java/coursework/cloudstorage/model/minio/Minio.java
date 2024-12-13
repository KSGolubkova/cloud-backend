package coursework.cloudstorage.model.minio;

import io.minio.MinioClient;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Minio {
    private MinioClient client;
    private MinioBucket bucket;
}