package coursework.cloudstorage.config;

import coursework.cloudstorage.model.minio.Minio;
import coursework.cloudstorage.model.minio.MinioBucket;
import coursework.cloudstorage.model.minio.MinioProperties;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@RequiredArgsConstructor
public class MinioConfig {
    private final MinioProperties properties;

    @Bean
    @Primary
    public Minio minio() {
        return buildMinio();
    }

    private Minio buildMinio() {
        return Minio.builder()
                .client(buildClient())
                .bucket(buildBucket())
                .build();
    }

    private MinioClient buildClient() {
        var clientProperties = properties.getClient();
        return MinioClient.builder()
                .endpoint(clientProperties.getEndpoint())
                .credentials(clientProperties.getAccessKey(), clientProperties.getSecretKey())
                .build();
    }

    private MinioBucket buildBucket() {
        var bucketProperties = properties.getBucket();
        return MinioBucket.builder()
                .name(bucketProperties.getName())
                .build();
    }
}