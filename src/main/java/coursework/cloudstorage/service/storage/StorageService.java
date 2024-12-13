package coursework.cloudstorage.service.storage;

import coursework.cloudstorage.model.dto.StorageObjectDTO;
import coursework.cloudstorage.service.UserService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StorageService {
    private final FolderService folderService;
    private final FileService fileService;
    private final UserService userService;

    @Transactional(readOnly = true)
    public List<StorageObjectDTO> getObjectsInFolder(Long id) {
        var user = userService.getCurrentUser();
        var res = new ArrayList<StorageObjectDTO>();
        res.addAll(folderService.getFoldersInFolder(id, user));
        res.addAll(fileService.getFilesInFolder(id, user));
        return res;
    }
}
