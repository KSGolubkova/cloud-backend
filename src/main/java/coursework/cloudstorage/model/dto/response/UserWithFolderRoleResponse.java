package coursework.cloudstorage.model.dto.response;

import coursework.cloudstorage.model.view.UserWithFolderView;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserWithFolderRoleResponse {
    private Long id;
    private String email;
    private String username;
    private String role;

    public UserWithFolderRoleResponse(UserWithFolderView view) {
        this.id = view.getId();
        this.email = view.getEmail();
        this.username = view.getUsername();
        this.role = view.getRole();
    }
}
