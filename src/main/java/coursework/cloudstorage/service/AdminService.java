package coursework.cloudstorage.service;

import coursework.cloudstorage.model.dto.UserDTO;
import coursework.cloudstorage.repository.UserRepository;
import coursework.cloudstorage.service.mapper.UserMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(UserMapper.INSTANCE::userToUserDTO).toList();
    }
}
