package coursework.cloudstorage.repository;

import coursework.cloudstorage.model.entity.File;
import coursework.cloudstorage.model.entity.Folder;
import coursework.cloudstorage.model.entity.RoleObject;
import coursework.cloudstorage.model.entity.User;
import coursework.cloudstorage.model.enums.RoleObjectEnum;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleObjectRepository extends JpaRepository<RoleObject, Long> {
    Optional<RoleObject> findByRole(RoleObjectEnum role);

    List<RoleObject> findAllByUsersContains(User user);

    List<RoleObject> findAllByFilesContains(File file);

    List<RoleObject> findAllByFoldersContains(Folder folder);

    boolean existsByFilesContainsAndUsersContainsAndRole(File file, User user, RoleObjectEnum role);
}
