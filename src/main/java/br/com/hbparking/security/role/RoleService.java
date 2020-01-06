package br.com.hbparking.security.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class RoleService {

    private final IRoleRepository iRoleRepository;

    @Autowired
    public RoleService(IRoleRepository iRoleRepository) {
        this.iRoleRepository = iRoleRepository;
    }

    public Set<Role> findAllByNamesIn(List<RoleName> roles) {
        return this.iRoleRepository.findAllByNameIsIn(roles);
    }

    public Role findRoleByName(RoleName roleName) {
        Optional<Role> roleOptional = this.iRoleRepository.findByName(roleName);
        if (roleOptional.isPresent()) {
            return roleOptional.get();
        }
        throw new IllegalArgumentException("Role não encontrada");
    }
}
