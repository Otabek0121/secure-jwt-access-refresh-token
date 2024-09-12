package uz.pdp.jwtsecure.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.pdp.jwtsecure.entities.Role;
import uz.pdp.jwtsecure.entities.User;
import uz.pdp.jwtsecure.enums.RoleEnum;
import uz.pdp.jwtsecure.repository.UserRepository;

import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void run(String... args) throws Exception {

        if (userRepository.findByUsername("ketmon").isEmpty()) {
            User user = new User();
            user.setId(UUID.randomUUID());
            user.setName("ketmon");
            user.setUsername("ketmon");
            user.setPassword(passwordEncoder.encode("root123"));
            user.setRoles(Set.of(Role.builder().name("USER").build()));
            user.setAccountNonExpired(true);
            user.setAccountNonLocked(true);
            user.setCredentialsNonExpired(true);
            user.setEnabled(true);
            user.setAccepted(true);

            userRepository.save(user);

            System.out.println("Default user 'ketmon' created.");
        } else {
            System.out.println("User 'ketmon' already exists.");
        }





    }
}
