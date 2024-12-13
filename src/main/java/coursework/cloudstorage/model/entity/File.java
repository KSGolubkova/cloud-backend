package coursework.cloudstorage.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Entity
@Getter
@Setter
@Table(name = "t_file")
@AllArgsConstructor
@NoArgsConstructor
public class File {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    protected Long id;

    private Long size;

    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private Folder parent;

    @ManyToMany
    @JoinTable(
            name = "t_role_file",
            joinColumns = @JoinColumn(name = "file_id"),
            inverseJoinColumns = @JoinColumn(name = "role_object_id")
    )
    private List<RoleObject> roles = new ArrayList<>();
    private String name;
}
