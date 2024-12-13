package coursework.cloudstorage.controller;

import coursework.cloudstorage.model.dto.StorageObjectDTO;
import coursework.cloudstorage.model.dto.request.FileCreateRequest;
import coursework.cloudstorage.service.storage.FileService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
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
@RequestMapping(path = "/api/files",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class FileController {
    private final FileService service;

    @GetMapping
    public ResponseEntity<List<StorageObjectDTO>> getFiles(@RequestBody(required = false) Object o) {
        return ResponseEntity.ok(service.getFiles());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createAndUploadFile(FileCreateRequest request) {
        service.createAndUploadFile(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable Long id, @RequestBody(required = false) Object o) {
        return ResponseEntity.ok().body(new InputStreamResource(service.downloadFile(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFileById(@PathVariable Long id, @RequestBody(required = false) Object o) {
        service.deleteFile(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> renameFile(@PathVariable Long id, String name) {
        service.renameFile(id, name);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{fileId}/{userId}")
    public ResponseEntity<Void> addUserToFile(@PathVariable Long fileId, @PathVariable Long userId, @RequestBody(required = false) Object o) {
        service.addUserToFile(fileId, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{fileId}/{userId}")
    public ResponseEntity<Void> removeUserToFile(@PathVariable Long fileId, @PathVariable Long userId, @RequestBody(required = false) Object o) {
        service.removeUserToFile(fileId, userId);
        return ResponseEntity.ok().build();
    }
}
