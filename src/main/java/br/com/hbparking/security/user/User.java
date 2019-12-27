package br.com.hbparking.security.user;

import br.com.hbparking.security.role.Role;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_auth")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NaturalId
    @NotNull
    @NotBlank
    @Size(max = 150)
    @Email
    @Column(name = "email", nullable = false, updatable = false)
    private String email;

    @NotNull
    @NotBlank
    @Column(name = "nome_completo", length = 150, nullable = false)
    private String nomeCompleto;

    @NotNull
    @NotBlank
    @Column(name = "password", length = 50, nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public User() {
    }

    public User(Long id, String email, String nomeCompleto, String password, Set<Role> roles) {
        this.id = id;
        this.email = email;
        this.nomeCompleto = nomeCompleto;
        this.password = password;
        this.roles = roles;
    }


    public User(String email, String nomeCompleto, String password, Set<Role> roles) {
        this.email = email;
        this.nomeCompleto = nomeCompleto;
        this.password = password;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
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

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}