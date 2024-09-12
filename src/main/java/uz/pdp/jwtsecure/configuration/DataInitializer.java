package uz.pdp.jwtsecure.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.pdp.jwtsecure.entities.Role;
import uz.pdp.jwtsecure.entities.User;
import uz.pdp.jwtsecure.repository.RoleRepository;
import uz.pdp.jwtsecure.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;


    @Override
    public void run(String... args) throws Exception {



        Optional<Role> userRoleOpt = roleRepository.findByName("USER");
        Optional<Role> adminRoleOpt = roleRepository.findByName("ADMIN");

        Role userRole;
        Role adminRole;

        if (userRoleOpt.isEmpty()) {
            userRole = new Role();
            userRole.setName("USER");
            roleRepository.save(userRole);
        } else {
            userRole = userRoleOpt.get();
        }

        if (adminRoleOpt.isEmpty()) {
            adminRole = new Role();
            adminRole.setName("ADMIN");
            roleRepository.save(adminRole);
        } else {
            adminRole = adminRoleOpt.get();
        }


        Optional<User> existingUser = userRepository.findByUsername("+998901234567");

        if (existingUser.isEmpty()) {
            User user = new User();
            user.setName("Ketmon");
            user.setUsername("ketmon");
            user.setPassword(passwordEncoder.encode("root123"));
            user.setRoles(Set.of(adminRole));
            user.setAccountNonExpired(true);
            user.setAccountNonLocked(true);
            user.setCredentialsNonExpired(true);
            user.setEnabled(true);
            user.setAccepted(true);

            userRepository.save(user);
        }





    }
}
