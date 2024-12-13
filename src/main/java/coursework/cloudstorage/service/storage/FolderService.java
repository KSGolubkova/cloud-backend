package coursework.cloudstorage.service.storage;

import coursework.cloudstorage.model.dto.StorageObjectDTO;
import coursework.cloudstorage.model.entity.Folder;
import coursework.cloudstorage.model.entity.User;
import coursework.cloudstorage.model.enums.RoleObjectEnum;
import coursework.cloudstorage.repository.FolderRepository;
import coursework.cloudstorage.service.UserService;
import coursework.cloudstorage.service.authority.RoleObjectService;
import coursework.cloudstorage.service.mapper.FolderMapper;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FolderService {
    private final FolderRepository repository;
    private final UserService userService;
    private final RoleObjectService roleObjectService;

    public StorageObjectDTO getStorageObjectDTO(Long id) {
        return FolderMapper.INSTANCE.folderToStorageObjectDTO(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("not found by folder id: " + id)));
    }

    public void createFolder(String name, Long parentId) {
        var folder = repository.save(
                Folder.builder()
                        .name(name)
                        .parent(parentId != null ? repository.findById(parentId).orElseThrow() : null)
                        .build());
        roleObjectService.createRoleObjectToUserForFolder(folder, userService.getCurrentUser(), RoleObjectEnum.AUTHOR);
    }

    @Transactional(readOnly = true)
    public Folder getFolder(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public List<StorageObjectDTO> getFoldersInFolder(Long parentId, User user) {
        return repository.findAllFoldersByParentIdAndCurrentUser(parentId, user.getId())
                .stream()
                .map(FolderMapper.INSTANCE::folderToStorageObjectDTO)
                .collect(Collectors.toList());
    }

    public void deleteFolderById(Long id) {
        repository.deleteById(id);
    }

    @Transactional
    public void renameFolder(Long id, String name) {
        var folder = repository.findById(id).orElseThrow(() -> new RuntimeException("not found by id:" + id));
        folder.setName(name);
        repository.save(folder);
    }

    @Transactional
    public void addUserToFolder(Long folderId, Long userId) {
        if (!repository.hasRoleAuthor(folderId, userService.getCurrentUser().getId())) {
            throw new IllegalArgumentException("Folder with id " + folderId + " doesn't have permission to add it");
        }
        if (repository.hasRole(folderId, userId)) {
            throw new IllegalArgumentException("Folder with id " + folderId + " already exists in folder with id " + userId);
        }
        roleObjectService.createRoleObjectToUserForFolder(repository.findById(folderId).orElseThrow(), userService.getById(userId), RoleObjectEnum.READER);
    }

    @Transactional
    public void removeUserToFolder(Long folderId, Long userId) {
        if (!repository.hasRoleAuthor(folderId, userService.getCurrentUser().getId())) {
            throw new IllegalArgumentException("Folder with id " + folderId + " doesn't have permission to add it");
        }
        if (!repository.hasRole(folderId, userId)) {
            throw new IllegalArgumentException("User " + userId + " dont have permission");
        }
        repository.deleteRoleByUserIdAndFolderId(folderId, userId);
    }
}
