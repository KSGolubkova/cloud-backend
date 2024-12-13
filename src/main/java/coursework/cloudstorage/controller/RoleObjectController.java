package coursework.cloudstorage.controller;

import coursework.cloudstorage.service.authority.RoleObjectService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/objects/roles")
@RequiredArgsConstructor
public class RoleObjectController {
    private final RoleObjectService service;

    @GetMapping
    ResponseEntity<List<String>> getAllRoles(@RequestBody(required = false) Object object) {
        return ResponseEntity.ok().body(service.getAllObjectRoles());
    }

}
