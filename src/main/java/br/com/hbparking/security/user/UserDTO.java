package br.com.hbparking.security.user;

import br.com.hbparking.security.role.Role;
import br.com.hbparking.security.role.RoleName;

import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserDTO {

    private Long id;
    private String email;
    private String nomeCompleto;
    @Size(max = 50)
    private String password;
    private Set<Role> roles;
    private List<RoleName> rolesString;

    public UserDTO() {
    }

    public UserDTO(Long id, String email, String nomeCompleto, String password, Set<Role> roles) {
        this.id = id;
        this.email = email;
        this.nomeCompleto = nomeCompleto;
        this.password = password;
        this.roles = roles;
    }

    public UserDTO(Long id, String email, String nomeCompleto, String password, Set<Role> roles, List<RoleName> rolesString) {
        this.id = id;
        this.email = email;
        this.nomeCompleto = nomeCompleto;
        this.password = password;
        this.roles = roles;
        this.rolesString = rolesString;
    }

    public UserDTO(String email, String nomeCompleto, String password, Set<Role> roles) {
        this.email = email;
        this.nomeCompleto = nomeCompleto;
        this.password = password;
        this.roles = roles;
    }

    public static UserDTO of(User user) {
        return new UserDTO(user.getId(),
                user.getEmail(),
                user.getNomeCompleto(),
                user.getPassword(),
                user.getRoles(),
                user.getRoles().stream().map(Role::getName).collect(Collectors.toList()));
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public List<RoleName> getRolesString() {
        return rolesString;
    }
}
