package uz.pdp.jwtsecure.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @JdbcTypeCode(SqlTypes.JSON)
    private Set<Role> roles = Set.of(Role.builder().name("USER").build());

    private Long tokenIssuedAt;

    private boolean accountNonExpired = true;


    @Column(name = "account_non_locked")
    private boolean accountNonLocked = true;


    @Column(name = "credentials_non_expired")
    private boolean credentialsNonExpired = true;



    private boolean enabled = true;

    private boolean accepted;
}
