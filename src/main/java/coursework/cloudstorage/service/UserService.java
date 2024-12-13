package coursework.cloudstorage.service;

import coursework.cloudstorage.model.dto.StorageObjectDTO;
import coursework.cloudstorage.model.dto.UserDTO;
import coursework.cloudstorage.model.dto.response.UserWithFolderRoleResponse;
import coursework.cloudstorage.model.entity.User;
import coursework.cloudstorage.model.enums.RoleEnum;
import coursework.cloudstorage.repository.UserRepository;
import coursework.cloudstorage.service.authority.RoleObjectService;
import coursework.cloudstorage.service.mapper.UserMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final RoleObjectService roleObjectService;

    @Transactional
    public User save(User user) {
        return repository.save(user);
    }

    @Transactional
    public User create(User user) {
        if (repository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Пользователь с таким именем уже существует");
        }

        if (repository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Пользователь с таким email уже существует");
        }

        return save(user);
    }

    @Transactional
    public User getByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
    }

    @Transactional
    public User getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
    }

    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    public User getCurrentUser() {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }

    @Transactional
    public List<StorageObjectDTO> getObjectsForCurrentUser() {
        return roleObjectService.getAlObjectsForCurrentUser(getCurrentUser());
    }

    @Transactional
    public List<UserDTO> getUsers() {
        return repository.findAllByRole(RoleEnum.ROLE_USER).stream().map(UserMapper.INSTANCE::userToUserDTO).toList();
    }

    @Transactional
    public List<UserDTO> getUsersWithRoleFile(Long fileId) {
        return repository.findAllWithRoleFile(fileId).stream().map(UserMapper.INSTANCE::userToUserDTO).toList();
    }

    @Transactional
    public List<UserWithFolderRoleResponse> getUsersWithFolderRoles(Long fileId) {
        return repository.findAllByParentIdWithHasFileRole(fileId).stream().map(UserWithFolderRoleResponse::new).toList();
    }

    @Transactional
    public UserDTO getCurrentUserDTO() {
        return UserMapper.INSTANCE.userToUserDTO(getCurrentUser());
    }
}