package uz.pdp.jwtsecure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.jwtsecure.entities.Role;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {



}
