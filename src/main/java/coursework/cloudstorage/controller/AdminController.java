package coursework.cloudstorage.controller;

import coursework.cloudstorage.model.dto.UserDTO;
import coursework.cloudstorage.service.AdminService;
import coursework.cloudstorage.service.database.PostgreSQLDumpService;
import coursework.cloudstorage.service.export.CsvExportService;
import coursework.cloudstorage.service.export.XlsxExportService;
import coursework.cloudstorage.service.yandex.YandexDiskUploadService;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/admin",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AdminController {
    private final AdminService service;
    private final CsvExportService csvExportService;
    private final XlsxExportService xlsxExportService;
    private final PostgreSQLDumpService postgreSQLDumpService;
    private final YandexDiskUploadService yandexDiskUploadService;

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers(@RequestBody(required = false) Object object) {
        return ResponseEntity.ok(service.getAllUsers());
    }

    @GetMapping("/export/xlsx")
    public ResponseEntity<InputStreamResource> getAllInXlsx(@RequestBody(required = false) Object o) {
        InputStreamResource inputStreamResource = xlsxExportService.writeDataToXlsx();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"database.xlsx\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(inputStreamResource);
    }

    @GetMapping("/export/csv")
    public ResponseEntity<InputStreamResource> getAllInCsv(@RequestBody(required = false) Object o) {
        InputStreamResource inputStreamResource = csvExportService.writeDataToCsv();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"database.csv\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(inputStreamResource);
    }

    @PostMapping("/create-dump")
    public ResponseEntity<String> createBackup(@RequestParam String dbName,
                                               @RequestParam String dbUser,
                                               @RequestParam String dbPassword,
                                               @RequestParam String dumpFilePath,
                                               @RequestParam String oauthToken,
                                               @RequestParam String remoteFilePath) {
        try {
            postgreSQLDumpService.createDatabaseDump(dbName, dbUser, dbPassword, dumpFilePath);

            yandexDiskUploadService.uploadFileToYandexDisk(oauthToken, dumpFilePath, remoteFilePath);

            return ResponseEntity.ok("Дамп базы данных успешно создан и загружен на Яндекс.Диск.");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Ошибка: " + e.getMessage());
        }
    }
}