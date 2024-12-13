package coursework.cloudstorage.service.export;

import coursework.cloudstorage.model.entity.File;
import coursework.cloudstorage.model.entity.Folder;
import coursework.cloudstorage.model.entity.RoleObject;
import coursework.cloudstorage.model.entity.User;
import coursework.cloudstorage.repository.FileRepository;
import coursework.cloudstorage.repository.FolderRepository;
import coursework.cloudstorage.repository.RoleObjectRepository;
import coursework.cloudstorage.repository.UserRepository;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CsvExportService {
    private final UserRepository userRepository;
    private final FileRepository fileRepository;
    private final FolderRepository folderRepository;
    private final RoleObjectRepository roleObjectRepository;

    public InputStreamResource writeDataToCsv() {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             Writer writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {

            writer.write("Users\n");
            writer.write("ID,Email,Username,Role\n");
            writeUsersToCsv(writer);

            writer.write("\nFiles\n");
            writer.write("ID,Name,Size,Parent ID\n");
            writeFilesToCsv(writer);

            writer.write("\nFolders\n");
            writer.write("ID,Name,Parent ID\n");
            writeFoldersToCsv(writer);

            writer.write("\nRole Objects\n");
            writer.write("ID,Role\n");
            writeRoleObjectsToCsv(writer);

            writer.write("\nRole File\n");
            writer.write("File id,User id,Role");
            writeRoleFileToCsv(writer);

            writer.write("\nRole Folder\n");
            writer.write("Folder id,User id,Role");
            writeRoleFolderToCsv(writer);

            writer.flush();
            return new InputStreamResource(new ByteArrayInputStream(outputStream.toByteArray()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeRoleFolderToCsv(Writer writer) throws IOException {
        for (var file : folderRepository.findAllFoldersForMetrics()) {
            writer.write(file.getFolderId() + ",");
            writer.write(file.getUserId() + ",");
            writer.write(file.getRole() + "\n");
        }
    }

    private void writeRoleFileToCsv(Writer writer) throws IOException {
        for (var file : fileRepository.findAllFilesForMetrics()) {
            writer.write(file.getFileId() + ",");
            writer.write(file.getUserId() + ",");
            writer.write(file.getRole() + "\n");
        }
    }

    private void writeUsersToCsv(Writer writer) throws IOException {
        for (User user : userRepository.findAll()) {
            writer.write(user.getId() + ",");
            writer.write(user.getEmail() + ",");
            writer.write(user.getUsername() + ",");
            writer.write(user.getRole().name() + "\n");
        }
    }

    private void writeFilesToCsv(Writer writer) throws IOException {
        for (File file : fileRepository.findAll()) {
            writer.write(file.getId() + ",");
            writer.write(file.getName() + ",");
            writer.write(file.getSize() + ",");
            writer.write(file.getParent() != null ? file.getParent().getId().toString() : "\n");
        }
    }

    private void writeFoldersToCsv(Writer writer) throws IOException {
        for (Folder folder : folderRepository.findAll()) {
            writer.write(folder.getId() + ",");
            writer.write(folder.getName() + ",");
            writer.write(folder.getParent() != null ? folder.getParent().getId().toString() : "\n");
        }
    }

    private void writeRoleObjectsToCsv(Writer writer) throws IOException {
        List<RoleObject> roleObjects = roleObjectRepository.findAll();
        for (RoleObject roleObject : roleObjects) {
            writer.write(roleObject.getId() + ",");
            writer.write(roleObject.getRole().name() + "\n");
        }
    }
}

