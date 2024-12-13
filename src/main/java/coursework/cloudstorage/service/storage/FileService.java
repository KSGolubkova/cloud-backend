package coursework.cloudstorage.service.storage;

import coursework.cloudstorage.model.dto.StorageObjectDTO;
import coursework.cloudstorage.model.dto.request.FileCreateRequest;
import coursework.cloudstorage.model.entity.File;
import coursework.cloudstorage.model.entity.Folder;
import coursework.cloudstorage.model.entity.User;
import coursework.cloudstorage.model.enums.RoleObjectEnum;
import coursework.cloudstorage.repository.FileRepository;
import coursework.cloudstorage.service.UserService;
import coursework.cloudstorage.service.authority.RoleObjectService;
import coursework.cloudstorage.service.mapper.FileMapper;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository repository;
    private final MinioService minioService;
    private final UserService userService;
    private final RoleObjectService roleObjectService;
    private final FolderService folderService;

    @Transactional(readOnly = true)
    public List<StorageObjectDTO> getFiles() {
        return repository.findAll().stream().map(FileMapper.INSTANCE::fileToStorageObjectDTO).toList();
    }

    public void createAndUploadFile(FileCreateRequest request) {
        if (repository.existsByNameAndParentId(request.getData().getOriginalFilename(), request.getFolderId(), userService.getCurrentUser().getId())) {
            throw new IllegalArgumentException("File with name " + request.getData().getOriginalFilename() +
                                               " already exists in folder with id " + request.getFolderId());
        }
        var fileEntity = repository.save(initializeFile(request.getData(), request.getFolderId() != null ? folderService.getFolder(request.getFolderId()) : null));
        minioService.putObject(fileEntity.getId().toString(), request.getData());
        roleObjectService.createRoleObjectToUserForFile(fileEntity, userService.getCurrentUser(), RoleObjectEnum.AUTHOR);
    }

    @Transactional(readOnly = true)
    public InputStream downloadFile(Long id) {
        return minioService.getObject(id.toString());
    }

    private File initializeFile(MultipartFile file, Folder parent) {
        return File.builder().name(file.getOriginalFilename()).size(file.getSize()).parent(parent).build();
    }

    public List<StorageObjectDTO> getFilesInFolder(Long parentId, User user) {
        return repository.findAllFilesByParentIdAndCurrentUser(parentId, user.getId())
                .stream()
                .map(FileMapper.INSTANCE::fileToStorageObjectDTO)
                .collect(Collectors.toList());
    }

    public void deleteFile(Long fileId) {
        if (!repository.hasRoleAuthor(fileId, userService.getCurrentUser().getId())) {
            throw new IllegalArgumentException("File with id " + fileId + " doesn't have permission to delete it");
        }
        minioService.removeObject(fileId.toString());
        repository.deleteById(fileId);
    }

    @Transactional
    public void renameFile(Long id, String name) {
        var file = repository.findById(id).orElseThrow(() -> new RuntimeException("File not found by id: " + id));
        file.setName(name);
        repository.save(file);
    }

    public void addUserToFile(Long fileId, Long userId) {
        if (!repository.hasRoleAuthor(fileId, userService.getCurrentUser().getId())) {
            throw new IllegalArgumentException("File with id " + fileId + " doesn't have permission to add it");
        }
        if (repository.hasRole(fileId, userId)) {
            throw new IllegalArgumentException("File with id " + fileId + " already exists in folder with id " + userId);
        }
        roleObjectService.createRoleObjectToUserForFile(repository.findById(fileId).orElseThrow(), userService.getById(userId), RoleObjectEnum.READER);
    }

    @Transactional
    public void removeUserToFile(Long fileId, Long userId) {
        if (!repository.hasRoleAuthor(fileId, userService.getCurrentUser().getId())) {
            throw new IllegalArgumentException("File with id " + fileId + " doesn't have permission to add it");
        }
        if (!repository.hasRole(fileId, userId)) {
            throw new IllegalArgumentException("User " + userId + " dont have permission");
        }
        repository.deleteRoleByUserIdAndFileId(fileId, userId);
    }
}
