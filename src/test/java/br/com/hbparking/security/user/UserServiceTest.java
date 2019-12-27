package br.com.hbparking.security.user;

import br.com.hbparking.security.role.Role;
import br.com.hbparking.security.role.RoleName;
import br.com.hbparking.security.role.RoleService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserServiceTest {

    private IUserRepository iUserRepository = Mockito.mock(IUserRepository.class);
    private RoleService roleService = Mockito.mock(RoleService.class);
    private PasswordEncoder encoder = Mockito.mock(PasswordEncoder.class);

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(iUserRepository, roleService, encoder);
    }

    @Test
    void persistingUserToDatabase() {
        UserDTO userDTO = new UserDTO(null,
                "rubenskleinschmidtjr@gmail.com",
                "Rubens Kleinschmidt Júnior",
                "12345", null, new ArrayList<>(Arrays.asList(RoleName.ROLE_USER, RoleName.ROLE_SISTEMA)));

        HashSet<Role> roles = new HashSet<>(Arrays.asList(new Role(RoleName.ROLE_USER), new Role(RoleName.ROLE_SISTEMA)));
        given(iUserRepository.save(any(User.class))).willReturn(new User(0L, userDTO.getEmail(), userDTO.getNomeCompleto(), userDTO.getPassword(), roles));

        User user = userService.save(userDTO);

        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isNotNull();
        assertThat(user.getEmail()).isNotEmpty();
        assertThat(user.getEmail()).isEqualTo("rubenskleinschmidtjr@gmail.com");
        assertThat(user.getNomeCompleto()).isNotNull();
        assertThat(user.getNomeCompleto()).isNotEmpty();
        assertThat(user.getNomeCompleto()).isEqualTo("Rubens Kleinschmidt Júnior");
        assertThat(user.getPassword()).isNotNull();
        assertThat(user.getPassword()).isNotEmpty();
        assertThat(user.getPassword()).isEqualTo("12345");
        assertThat(user.getRoles()).isNotEmpty();
        assertThat(user.getRoles()).isEqualTo(roles);
    }
}
