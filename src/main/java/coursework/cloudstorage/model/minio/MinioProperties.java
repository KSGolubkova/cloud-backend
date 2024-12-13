package coursework.cloudstorage.model.minio;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ConfigurationProperties(prefix = "minio")
@Component
public class MinioProperties {
    private ClientProperties client;
    private BucketProperties bucket;

    @Getter
    @Setter
    @ConfigurationProperties(prefix = "client")
    @Component
    public static class ClientProperties {
        private String endpoint;
        private String accessKey;
        private String secretKey;
    }

    @Getter
    @Setter
    @ConfigurationProperties(prefix = "bucket")
    @Component
    public static class BucketProperties {
        private String name;
    }
}