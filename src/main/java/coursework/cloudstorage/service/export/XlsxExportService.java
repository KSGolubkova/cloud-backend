package coursework.cloudstorage.service.export;

import coursework.cloudstorage.repository.FileRepository;
import coursework.cloudstorage.repository.FolderRepository;
import coursework.cloudstorage.repository.RoleObjectRepository;
import coursework.cloudstorage.repository.UserRepository;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class XlsxExportService {
    private final UserRepository userRepository;
    private final FileRepository fileRepository;
    private final FolderRepository folderRepository;
    private final RoleObjectRepository roleObjectRepository;

    public InputStreamResource writeDataToXlsx() {
        try (var workbook = new SXSSFWorkbook()) {
            fillUserSheet(workbook.createSheet("User"));
            fillFileSheet(workbook.createSheet("File"));
            fillFolderSheet(workbook.createSheet("Folder"));
            fillRoleObjectSheet(workbook.createSheet("Role object"));
            fillRoleFileSheet(workbook.createSheet("Role file"));
            fillRoleFolderSheet(workbook.createSheet("Role folder"));
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return new InputStreamResource(new ByteArrayInputStream(outputStream.toByteArray()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void fillRoleFileSheet(Sheet sheet) {
        var headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("File id");
        headerRow.createCell(1).setCellValue("User id");
        headerRow.createCell(2).setCellValue("Role");
        fileRepository.findAllFilesForMetrics().forEach(file -> {
            var row = sheet.createRow(sheet.getLastRowNum() + 1);
            row.createCell(0).setCellValue(file.getFileId());
            row.createCell(1).setCellValue(file.getUserId());
            row.createCell(2).setCellValue(file.getRole());
        });
    }

    private void fillRoleFolderSheet(Sheet sheet) {
        var headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Folder id");
        headerRow.createCell(1).setCellValue("User id");
        headerRow.createCell(2).setCellValue("Role");
        folderRepository.findAllFoldersForMetrics().forEach(folder -> {
            var row = sheet.createRow(sheet.getLastRowNum() + 1);
            row.createCell(0).setCellValue(folder.getFolderId());
            row.createCell(1).setCellValue(folder.getUserId());
            row.createCell(2).setCellValue(folder.getRole());
        });
    }

    private void fillUserSheet(Sheet sheet) {
        var headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Email");
        headerRow.createCell(2).setCellValue("Username");
        headerRow.createCell(3).setCellValue("Role");
        userRepository.findAll().forEach(user -> {
            var row = sheet.createRow(sheet.getLastRowNum() + 1);
            row.createCell(0).setCellValue(user.getId());
            row.createCell(1).setCellValue(user.getEmail());
            row.createCell(2).setCellValue(user.getUsername());
            row.createCell(3).setCellValue(user.getRole().name());
        });
    }

    private void fillFileSheet(Sheet sheet) {
        var headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Name");
        headerRow.createCell(2).setCellValue("Size");
        headerRow.createCell(3).setCellValue("Parent id");
        fileRepository.findAll().forEach(file -> {
            var row = sheet.createRow(sheet.getLastRowNum() + 1);
            row.createCell(0).setCellValue(file.getId());
            row.createCell(1).setCellValue(file.getName());
            row.createCell(2).setCellValue(file.getSize());
            row.createCell(3).setCellValue(file.getParent() != null ? file.getParent().getId().toString() : "");
        });
    }

    private void fillFolderSheet(Sheet sheet) {
        var headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Name");
        headerRow.createCell(2).setCellValue("Parent id");
        folderRepository.findAll().forEach(folder -> {
            var row = sheet.createRow(sheet.getLastRowNum() + 1);
            row.createCell(0).setCellValue(folder.getId());
            row.createCell(1).setCellValue(folder.getName());
            row.createCell(2).setCellValue(folder.getParent() != null ? folder.getParent().getId().toString() : "");
        });
    }

    private void fillRoleObjectSheet(Sheet sheet) {
        var headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Role");
        roleObjectRepository.findAll().forEach(roleObject -> {
            var row = sheet.createRow(sheet.getLastRowNum() + 1);
            row.createCell(0).setCellValue(roleObject.getId());
            row.createCell(1).setCellValue(roleObject.getRole().name());
        });
    }
}
