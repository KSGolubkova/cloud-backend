package coursework.cloudstorage.repository;

import coursework.cloudstorage.model.entity.User;
import coursework.cloudstorage.model.enums.RoleEnum;
import coursework.cloudstorage.model.view.UserWithFolderView;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    List<User> findAllByRole(RoleEnum role);

    @Query(value = """
            SELECT u.username AS username, f.name AS filename, roo.role_name AS role
            FROM t_user AS u
                     LEFT JOIN t_role_user ru ON u.id = ru.user_id
                     LEFT JOIN t_role_on_object roo ON roo.id = ru.role_object_id
                     LEFT JOIN t_role_file rf ON rf.role_object_id = roo.id
                     LEFT JOIN t_file f ON rf.file_id = f.id
            ORDER BY username, filename, role
            """, nativeQuery = true)
    List<User> findAllWithRoleFile(Long fileId);

    @Query(value = """
                WITH table_all_users AS (
                    SELECT u.id AS id, u.username AS username, u.email AS email, u.role AS user_role, NULL AS role
                    FROM t_user AS u
                             LEFT JOIN t_role_user AS ru ON u.id = ru.user_id
                             LEFT JOIN t_role_on_object AS roo ON ru.role_object_id = roo.id
                             LEFT JOIN t_role_folder AS rfolder ON roo.id = rfolder.role_object_id
                    WHERE rfolder.folder_id = (SELECT tf.parent_id FROM t_file AS tf WHERE tf.id = :fileId)
                       OR (SELECT tf.parent_id FROM t_file AS tf WHERE tf.id = :fileId) IS NULL
                ),
                     table_is_file AS (
                         SELECT u1.id, u1.username, u1.email, u1.role AS user_role, roo1.role_name AS role
                         FROM t_role_folder AS rfolder, t_role_file AS rf1
                                  LEFT JOIN t_file AS f ON f.id = rf1.file_id
                                  LEFT JOIN t_role_on_object AS roo1 ON rf1.role_object_id = roo1.id
                                  LEFT JOIN t_role_user AS ru ON (roo1.id = ru.role_object_id)
                                  LEFT JOIN t_user AS u1 ON ru.user_id = u1.id
                         WHERE rf1.file_id = :fileId
                           AND (f.parent_id IS NULL OR f.parent_id = rfolder.folder_id)
                     ),
                     table_is_folder AS (
                         SELECT u2.id, u2.username, u2.email, u2.role AS user_role, roo2.role_name AS role
                         FROM t_role_folder AS rfolder
                                  LEFT JOIN t_folder AS folder ON folder.id = rfolder.folder_id
                                  LEFT JOIN t_role_on_object AS roo2 ON rfolder.role_object_id = roo2.id
                                  LEFT JOIN t_role_user AS ru ON (roo2.id = ru.role_object_id)
                                  LEFT JOIN t_user AS u2 ON ru.user_id = u2.id
                         WHERE rfolder.folder_id = :fileId
                           AND (folder.parent_id IS NULL OR folder.parent_id = rfolder.folder_id)
                     ),
                     merged_data AS (
                         SELECT id, username, email, user_role, role, 1 AS priority
                         FROM table_is_file
                         UNION ALL
                         SELECT id, username, email, user_role, role, 2 AS priority
                         FROM table_is_folder
                         UNION ALL
                         SELECT id, username, email, user_role, role, 3 AS priority
                         FROM table_all_users
                     )
                SELECT DISTINCT ON (id) id, username, email, user_role, role
                FROM merged_data
                ORDER BY id, priority
            """, nativeQuery = true)
    List<UserWithFolderView> findAllByParentIdWithHasFileRole(Long fileId);
}
