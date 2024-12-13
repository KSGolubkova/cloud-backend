package coursework.cloudstorage.controller;

import coursework.cloudstorage.model.dto.StorageObjectDTO;
import coursework.cloudstorage.service.storage.StorageService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/storage")
public class StorageController {
    private final StorageService storageService;

    @GetMapping
    public ResponseEntity<List<StorageObjectDTO>> getObjectsInFolder(@RequestParam(required = false) Long id) {
        return ResponseEntity.ok(storageService.getObjectsInFolder(id));
    }
}
