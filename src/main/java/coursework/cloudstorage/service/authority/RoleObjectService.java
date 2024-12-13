package coursework.cloudstorage.service.authority;

import coursework.cloudstorage.model.dto.StorageObjectDTO;
import coursework.cloudstorage.model.entity.File;
import coursework.cloudstorage.model.entity.Folder;
import coursework.cloudstorage.model.entity.RoleObject;
import coursework.cloudstorage.model.entity.User;
import coursework.cloudstorage.model.enums.RoleObjectEnum;
import coursework.cloudstorage.repository.RoleObjectRepository;
import coursework.cloudstorage.service.mapper.FileMapper;
import coursework.cloudstorage.service.mapper.FolderMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoleObjectService {
    private final RoleObjectRepository repository;

    public List<String> getAllObjectRoles() {
        return Arrays.stream(RoleObjectEnum.values()).map(Enum::name).toList();
    }

    @Transactional(readOnly = true)
    public List<StorageObjectDTO> getAlObjectsForCurrentUser(User user) {
        List<StorageObjectDTO> res = new ArrayList<>();
        res.addAll(getFoldersForUser(user));
        res.addAll(getFilesForUser(user));
        return res;
    }

    public void createRoleObjectToUserForFile(File file, User user, RoleObjectEnum role) {
        RoleObject roleObject = new RoleObject(role);
        roleObject.getFiles().add(file);
        roleObject.getUsers().add(user);
        repository.save(roleObject);
    }

    public void createRoleObjectToUserForFolder(Folder folder, User user, RoleObjectEnum role) {
        RoleObject roleObject = new RoleObject(role);
        roleObject.getFolders().add(folder);
        roleObject.getUsers().add(user);
        repository.save(roleObject);
    }

    public List<StorageObjectDTO> getFilesForUser(User user) {
        return repository.findAllByUsersContains(user).stream()
                .flatMap(roleObject -> roleObject.getFiles().stream())
                .map(file -> {
                    var dto = FileMapper.INSTANCE.fileToStorageObjectDTO(file);
                    dto.setRole(getUserRoleForFile(file, user).getDescription());
                    return dto;
                })
                .toList();
    }

    public List<StorageObjectDTO> getFoldersForUser(User user) {
        return repository.findAllByUsersContains(user).stream()
                .flatMap(roleObject -> roleObject.getFolders().stream())
                .map(folder -> {
                    var dto = FolderMapper.INSTANCE.folderToStorageObjectDTO(folder);
                    dto.setRole(getUserRoleForFolder(folder, user).getDescription());
                    return dto;
                })
                .toList();
    }

    private RoleObjectEnum getUserRoleForFile(File file, User user) {
        return repository.findAllByFilesContains(file)
                .stream()
                .filter(roleObject -> roleObject.getUsers().contains(user))
                .findFirst()
                .map(RoleObject::getRole)
                .orElse(null);
    }

    private RoleObjectEnum getUserRoleForFolder(Folder folder, User user) {
        return repository.findAllByFoldersContains(folder)
                .stream()
                .filter(roleObject -> roleObject.getUsers().contains(user))
                .findFirst()
                .map(RoleObject::getRole)
                .orElse(null);
    }
}
