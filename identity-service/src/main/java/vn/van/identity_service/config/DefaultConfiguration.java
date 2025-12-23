package vn.van.identity_service.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.van.identity_service.constant.RoleType;
import vn.van.identity_service.entity.Role;
import vn.van.identity_service.entity.User;
import vn.van.identity_service.repository.RoleRepository;
import vn.van.identity_service.repository.UserRepository;

import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class DefaultConfiguration {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.default.user.email}")
    private String userEmail;

    @Value("${app.default.user.password}")
    private String userPassword;

    @Bean
    @ConditionalOnProperty(
            prefix = "spring",
            value = "datasource.driver-class-name",
            havingValue = "com.mysql.cj.jdbc.Driver"
    )
    public ApplicationRunner applicationRunner() {
        return args -> {
            Role adminRole = roleRepository.findById(RoleType.ADMIN.name())
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setName(RoleType.ADMIN.name());
                        roleRepository.save(role);
                        return role;
                    });

            Role userRole = roleRepository.findById(RoleType.USER.name())
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setName(RoleType.USER.name());
                        roleRepository.save(role);
                        return role;
                    });

            if (!userRepository.existsByEmail(userEmail)) {
                User user = new User();
                user.setEmail(userEmail);
                user.setPassword(passwordEncoder.encode(userPassword));
                user.setRoles(Set.of(adminRole, userRole));
                userRepository.save(user);
            }
        };
    }
}
