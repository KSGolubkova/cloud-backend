package coursework.cloudstorage.repository;

import coursework.cloudstorage.model.entity.Folder;
import coursework.cloudstorage.model.view.RoleFolderView;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface FolderRepository extends JpaRepository<Folder, Long> {
    @Query(value = """
            SELECT DISTINCT f.*
            FROM t_folder AS f
                     LEFT JOIN t_role_folder AS rf ON rf.folder_id = f.id
                     LEFT JOIN t_role_on_object AS roo ON roo.id = rf.role_object_id
                     LEFT JOIN t_role_user AS ru ON ru.role_object_id = roo.id
                     LEFT JOIN t_user AS u ON u.id = ru.user_id
            WHERE u.id = :userId
              AND (f.parent_id = :folderId OR (:folderId IS NULL AND f.parent_id IS NULL))
            ORDER BY f.name
            """, nativeQuery = true)
    List<Folder> findAllFoldersByParentIdAndCurrentUser(Long folderId, Long userId);

    @Query(value = """
                SELECT f.id AS folderId, u.id AS userId, roo.role_name AS role
                FROM t_folder AS f
                         LEFT JOIN t_role_folder AS rf ON rf.folder_id = f.id
                         LEFT JOIN t_role_on_object AS roo ON roo.id = rf.role_object_id
                         LEFT JOIN t_role_user AS ru ON ru.role_object_id = roo.id
                         LEFT JOIN t_user AS u ON u.id = ru.user_id
                ORDER BY folderId, role, userId
            """, nativeQuery = true)
    List<RoleFolderView> findAllFoldersForMetrics();

    @Query(value = """
                SELECT EXISTS(SELECT rf.folder_id
                FROM t_role_folder AS rf
                         LEFT JOIN t_role_on_object AS roo ON roo.id = rf.role_object_id
                         LEFT JOIN t_role_user AS ru ON ru.role_object_id = roo.id
                         LEFT JOIN t_user AS u ON u.id = ru.user_id
                WHERE u.id = :userId AND rf.folder_id = :folderId AND roo.role_name = 'AUTHOR')
            """, nativeQuery = true)
    boolean hasRoleAuthor(Long folderId, Long userId);

    @Query(value = """
                SELECT EXISTS(SELECT rf.folder_id
                FROM t_role_folder AS rf
                         LEFT JOIN t_role_on_object AS roo ON roo.id = rf.role_object_id
                         LEFT JOIN t_role_user AS ru ON ru.role_object_id = roo.id
                         LEFT JOIN t_user AS u ON u.id = ru.user_id
                WHERE rf.folder_id = :folderId AND u.id = :userId)
            """, nativeQuery = true)
    boolean hasRole(Long folderId, Long userId);

    @Modifying
    @Query(value = """
                WITH get_roo AS (
                    SELECT roo.id
                    FROM t_role_folder AS rf
                             LEFT JOIN t_role_on_object AS roo ON roo.id = rf.role_object_id
                             LEFT JOIN t_role_user AS ru ON ru.role_object_id = roo.id
                    WHERE rf.folder_id = :folderId AND ru.user_id = :userId
                ), delete_role_user AS (
                    DELETE FROM t_role_user
                        WHERE role_object_id IN (SELECT gr.id FROM get_roo gr)
                ),
                     delete_role_file AS (
                         DELETE FROM t_role_folder
                             WHERE role_object_id IN (SELECT gr.id FROM get_roo gr)
                    )
                DELETE FROM t_role_on_object AS roo1
                WHERE roo1.id IN (SELECT gr.id FROM get_roo gr)
            """, nativeQuery = true)
    void deleteRoleByUserIdAndFolderId(Long folderId, Long userId);
}
