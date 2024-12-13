package coursework.cloudstorage.repository;

import coursework.cloudstorage.model.entity.File;
import coursework.cloudstorage.model.view.RoleFileView;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface FileRepository extends JpaRepository<File, Long> {
    @Query(value = """
                SELECT EXISTS(
                SELECT f.id
                FROM t_file AS f
                         LEFT JOIN t_role_file AS rf ON rf.file_id = f.id
                         LEFT JOIN t_role_on_object AS roo ON roo.id = rf.role_object_id
                         LEFT JOIN t_role_user AS ru ON ru.role_object_id = roo.id
                         LEFT JOIN t_user AS u ON u.id = ru.user_id
                WHERE f.id = :folderId OR (f.parent_id IS NULL AND :folderId IS NULL)
                  AND u.id = :userId
                  AND f.name = :name
                )
            """, nativeQuery = true)
    boolean existsByNameAndParentId(String name, Long folderId, Long userId);

    @Query(value = """
            SELECT DISTINCT f.*
            FROM t_file AS f
                     LEFT JOIN t_role_file AS rf ON rf.file_id = f.id
                     LEFT JOIN t_role_on_object AS roo ON roo.id = rf.role_object_id
                     LEFT JOIN t_role_user AS ru ON ru.role_object_id = roo.id
                     LEFT JOIN t_user AS u ON u.id = ru.user_id
            WHERE u.id = :userId
                AND (f.parent_id = :folderId OR (:folderId IS NULL AND f.parent_id IS NULL))
            ORDER BY f.name
            """, nativeQuery = true)
    List<File> findAllFilesByParentIdAndCurrentUser(Long folderId, Long userId);

    @Query(value = """
                SELECT EXISTS(SELECT f.id
                FROM t_file AS f
                         LEFT JOIN t_role_file AS rf ON rf.file_id = f.id
                         LEFT JOIN t_role_on_object AS roo ON roo.id = rf.role_object_id
                         LEFT JOIN t_role_user AS ru ON ru.role_object_id = roo.id
                         LEFT JOIN t_user AS u ON u.id = ru.user_id
                WHERE u.id = :userId AND f.id = :fileId AND roo.role_name = 'AUTHOR')
            """, nativeQuery = true)
    boolean hasRoleAuthor(Long fileId, Long userId);

    @Query(value = """
                SELECT EXISTS(SELECT f.id
                FROM t_file AS f
                         LEFT JOIN t_role_file AS rf ON rf.file_id = f.id
                         LEFT JOIN t_role_on_object AS roo ON roo.id = rf.role_object_id
                         LEFT JOIN t_role_user AS ru ON ru.role_object_id = roo.id
                         LEFT JOIN t_user AS u ON u.id = ru.user_id
                WHERE f.id = :fileId AND u.id = :userId)
            """, nativeQuery = true)
    boolean hasRole(Long fileId, Long userId);

    @Query(value = """
                SELECT f.id AS fileId, u.id AS userId, roo.role_name AS role
                FROM t_file AS f
                         LEFT JOIN t_role_file AS rf ON rf.file_id = f.id
                         LEFT JOIN t_role_on_object AS roo ON roo.id = rf.role_object_id
                         LEFT JOIN t_role_user AS ru ON ru.role_object_id = roo.id
                         LEFT JOIN t_user AS u ON u.id = ru.user_id
                ORDER BY fileId, role, userId
            """, nativeQuery = true)
    List<RoleFileView> findAllFilesForMetrics();

    @Modifying
    @Query(value = """
                WITH get_roo AS (
                    SELECT roo.id
                    FROM t_role_file AS rf
                             LEFT JOIN t_role_on_object AS roo ON roo.id = rf.role_object_id
                             LEFT JOIN t_role_user AS ru ON ru.role_object_id = roo.id
                    WHERE rf.file_id = :fileId AND ru.user_id = :userId
                ), delete_role_user AS (
                    DELETE FROM t_role_user
                        WHERE role_object_id IN (SELECT gr.id FROM get_roo gr)
                ),
                     delete_role_file AS (
                         DELETE FROM t_role_file
                             WHERE role_object_id IN (SELECT gr.id FROM get_roo gr)
                    )
                DELETE FROM t_role_on_object AS roo1
                WHERE roo1.id IN (SELECT gr.id FROM get_roo gr)
            """, nativeQuery = true)
    void deleteRoleByUserIdAndFileId(Long fileId, Long userId);
}
