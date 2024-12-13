package coursework.cloudstorage.controller;

import coursework.cloudstorage.model.dto.StorageObjectDTO;
import coursework.cloudstorage.model.dto.UserDTO;
import coursework.cloudstorage.model.dto.response.UserWithFolderRoleResponse;
import coursework.cloudstorage.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping("/user/objects")
    ResponseEntity<List<StorageObjectDTO>> getAllObjectsForCurrentUser(@RequestBody(required = false) Object object) {
        return ResponseEntity.ok().body(service.getObjectsForCurrentUser());
    }

    @GetMapping
    ResponseEntity<List<UserDTO>> getUsers(@RequestBody(required = false) Object object) {
        return ResponseEntity.ok().body(service.getUsers());
    }

    @GetMapping("/user")
    ResponseEntity<UserDTO> getUserById(@RequestBody(required = false) Object object) {
        return ResponseEntity.ok().body(service.getCurrentUserDTO());
    }

    @GetMapping("/{fileId}")
    ResponseEntity<List<UserWithFolderRoleResponse>> getUsersWithFolderRoles(@PathVariable Long fileId, @RequestBody(required = false) Object object) {
        return ResponseEntity.ok().body(service.getUsersWithFolderRoles(fileId));
    }

    @GetMapping("/roles/{id}")
    ResponseEntity<List<UserDTO>> getUsersWithRoleFile(@PathVariable Long id) {
        return ResponseEntity.ok().body(service.getUsersWithRoleFile(id));
    }
}
