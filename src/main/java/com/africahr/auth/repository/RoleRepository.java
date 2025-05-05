// src/main/java/com/africahr/auth/repository/RoleRepository.java

package com.africahr.auth.repository;

import com.africahr.auth.model.Role;
import com.africahr.auth.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByName(String roleName);
}