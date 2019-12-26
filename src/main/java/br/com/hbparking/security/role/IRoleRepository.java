package br.com.hbparking.security.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {
    Set<Role> findAllByNameIsIn(List<RoleName> names);
}
