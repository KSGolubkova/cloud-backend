package coursework.cloudstorage.model.entity;

import coursework.cloudstorage.model.enums.RoleObjectEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "t_role_on_object")
@NoArgsConstructor
public class RoleObject implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name", nullable = false)
    private RoleObjectEnum role;

    @ManyToMany
    @JoinTable(
            name = "t_role_user",
            joinColumns = @JoinColumn(name = "role_object_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> users = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "t_role_file",
            joinColumns = @JoinColumn(name = "role_object_id"),
            inverseJoinColumns = @JoinColumn(name = "file_id")
    )
    private List<File> files = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "t_role_folder",
            joinColumns = @JoinColumn(name = "role_object_id"),
            inverseJoinColumns = @JoinColumn(name = "folder_id")
    )
    private List<Folder> folders = new ArrayList<>();

    public RoleObject(RoleObjectEnum role) {
        this.role = role;
    }
}
