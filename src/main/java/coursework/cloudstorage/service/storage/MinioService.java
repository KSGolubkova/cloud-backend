package coursework.cloudstorage.service.storage;

import coursework.cloudstorage.exception.InternalMinioException;
import coursework.cloudstorage.model.minio.Minio;
import io.minio.GetObjectArgs;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MinioService {
    private final Minio minio;

    private static final String DEFAULT_USER_PREFIX = "users/";

    public InputStream getObject(String filename) {
        try {
            return minio.getClient().getObject(buildGetObjectArgs(minio.getBucket().getName(),
                    DEFAULT_USER_PREFIX + filename));
        } catch (Exception e) {
            throw new InternalMinioException("Не удается загрузить файл из хранилища");
        }
    }

    public void putObject(String filename, MultipartFile object) {
        try {
            minio.getClient().putObject(buildPutObjectArgs(minio.getBucket().getName(),
                    DEFAULT_USER_PREFIX + filename, object.getInputStream(), object.getSize()));
        } catch (Exception e) {
            throw new InternalMinioException("Не удается выгрузить файл в хранилище");
        }
    }

    public void removeObject(String filename) {
        try {
            minio.getClient().removeObject(buildRemoveObjectArgs(minio.getBucket().getName(),
                    DEFAULT_USER_PREFIX + filename));
        } catch (Exception e) {
            throw new InternalMinioException("Не удается удалить файл из хранилища");
        }
    }

    private static RemoveObjectArgs buildRemoveObjectArgs(String bucketName, String filename) {
        return RemoveObjectArgs.builder()
                .bucket(bucketName)
                .object(filename)
                .build();
    }

    private static GetObjectArgs buildGetObjectArgs(String bucketName, String filename) {
        return GetObjectArgs.builder()
                .bucket(bucketName)
                .object(filename)
                .build();
    }

    private static PutObjectArgs buildPutObjectArgs(String bucketName, String filename, InputStream stream, long size) {
        return PutObjectArgs.builder()
                .bucket(bucketName)
                .object(filename)
                .stream(stream, size, -1)
                .build();
    }

}
