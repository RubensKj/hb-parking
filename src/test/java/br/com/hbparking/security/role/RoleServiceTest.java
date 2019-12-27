package br.com.hbparking.security.role;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RoleServiceTest {

    private IRoleRepository iRoleRepository = Mockito.mock(IRoleRepository.class);

    private RoleService roleService;

    @BeforeEach
    void setUp() {
        roleService = new RoleService(iRoleRepository);
    }

    @Test
    void findAllByNamesIn() {
        List<RoleName> roleNames = new ArrayList<>(Arrays.asList(RoleName.ROLE_USER, RoleName.ROLE_GESTOR, RoleName.ROLE_SISTEMA));

        Set<Role> rolesFromDatabase = new HashSet<>(Arrays.asList(new Role(RoleName.ROLE_USER), new Role(RoleName.ROLE_GESTOR), new Role(RoleName.ROLE_SISTEMA)));
        given(iRoleRepository.findAllByNameIsIn(any())).willReturn(rolesFromDatabase);

        Set<Role> allRolesByNamesIn = roleService.findAllByNamesIn(roleNames);

        assertThat(allRolesByNamesIn).isNotNull();
        assertThat(allRolesByNamesIn).isNotEmpty();
        assertThat(allRolesByNamesIn).isEqualTo(rolesFromDatabase);
    }
}
