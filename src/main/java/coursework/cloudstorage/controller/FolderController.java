package coursework.cloudstorage.controller;

import coursework.cloudstorage.model.dto.StorageObjectDTO;
import coursework.cloudstorage.model.dto.request.FolderCreateRequest;
import coursework.cloudstorage.service.storage.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/folders",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class FolderController {
    private final FolderService service;

    @GetMapping("/{id}")
    public ResponseEntity<StorageObjectDTO> getFolder(@PathVariable("id") Long id, @RequestBody(required = false) Object o) {
        return ResponseEntity.ok().body(service.getStorageObjectDTO(id));
    }

    @PostMapping
    public ResponseEntity<Void> createFolder(@RequestBody FolderCreateRequest folderCreateRequest) {
        service.createFolder(folderCreateRequest.getName(), folderCreateRequest.getFolderId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFolder(@PathVariable Long id) {
        service.deleteFolderById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> renameFolder(@PathVariable Long id, String name) {
        service.renameFolder(id, name);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{folderId}/{userId}")
    public ResponseEntity<Void> addUserToFile(@PathVariable Long folderId, @PathVariable Long userId, @RequestBody(required = false) Object o) {
        service.addUserToFolder(folderId, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{folderId}/{userId}")
    public ResponseEntity<Void> removeUserToFile(@PathVariable Long folderId, @PathVariable Long userId, @RequestBody(required = false) Object o) {
        service.removeUserToFolder(folderId, userId);
        return ResponseEntity.ok().build();
    }
}
