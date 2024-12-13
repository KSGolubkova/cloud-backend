package coursework.cloudstorage.service.database;

import coursework.cloudstorage.service.yandex.YandexDiskUploadService;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class DatabaseBackupScheduler {

    private final PostgreSQLDumpService postgreSQLDumpService;
    private final YandexDiskUploadService yandexDiskUploadService;

    @Value("${app.database.name}")
    private String dbName;

    @Value("${app.database.user}")
    private String dbUser;

    @Value("${app.database.password}")
    private String dbPassword;

    @Value("${app.yandex.oauth.token}")
    private String oauthToken;

    @Value("${app.database.backup.path}")
    private String dumpFilePath;

    @Value("${app.yandex.backup.path}")
    private String remoteFilePath;

    public DatabaseBackupScheduler(PostgreSQLDumpService postgreSQLDumpService, YandexDiskUploadService yandexDiskUploadService) {
        this.postgreSQLDumpService = postgreSQLDumpService;
        this.yandexDiskUploadService = yandexDiskUploadService;
    }

    @Scheduled(cron = "0 0 12 * * ?")
    public void performDatabaseBackup() {
        try {
            postgreSQLDumpService.createDatabaseDump(dbName, dbUser, dbPassword, dumpFilePath);

            yandexDiskUploadService.uploadFileToYandexDisk(oauthToken, dumpFilePath, remoteFilePath);

            System.out.println("Дамп базы данных успешно создан и загружен на Яндекс.Диск.");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Ошибка при создании дампа или загрузке на Яндекс.Диск: " + e.getMessage());
        }
    }
}