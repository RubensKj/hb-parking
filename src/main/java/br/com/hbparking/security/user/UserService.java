package br.com.hbparking.security.user;

import br.com.hbparking.security.role.Role;
import br.com.hbparking.security.role.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final IUserRepository IUserRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(br.com.hbparking.security.user.IUserRepository iUserRepository, RoleService roleService, PasswordEncoder passwordEncoder) {
        IUserRepository = iUserRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    public User findByEmail(String email) {
        return this.IUserRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Usuário não foi encontrado com esse email." + email));
    }

    public String encryptUserDTOPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public User save(UserDTO userDTO) {
        this.validate(userDTO);

        LOGGER.info("Salvando usuário.");
        LOGGER.debug("UserDTO: {}", userDTO);

        Set<Role> rolesFromDatabase = roleService.findAllByNamesIn(userDTO.getRolesString());

        return this.IUserRepository.save(new User(userDTO.getEmail(), userDTO.getNomeCompleto(), userDTO.getPassword(), rolesFromDatabase));
    }

    public void saveAllUsers(List<User> userList) {
        this.IUserRepository.saveAll(userList);
    }

    private void validate(UserDTO userDTO) {
        LOGGER.info("Validando usuário.");

        if (StringUtils.isEmpty(userDTO.getEmail())) {
            throw new IllegalArgumentException("E-mail não pode ser nulo/vázio.");
        }

        if (this.IUserRepository.existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("Usuário com este e-mail já existe.");
        }

        if (StringUtils.isEmpty(userDTO.getNomeCompleto())) {
            throw new IllegalArgumentException("Nome completo não pode ser nulo/vázio.");
        }

        if (StringUtils.isEmpty(userDTO.getPassword())) {
            throw new IllegalArgumentException("Senha não deve ser nula/vázia.");
        }

        if (userDTO.getRolesString().isEmpty()) {
            throw new IllegalArgumentException("RolesStrings não deve ser nula/vázia.");
        }
    }

    public void updateSenha(String password, String email) {
        User user = this.findByEmail(email);

        user.setPassword(this.encryptUserDTOPassword(password));

        this.IUserRepository.save(user);
    }
}
