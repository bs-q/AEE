package com.aee.service;

import com.aee.service.models.ERole;
import com.aee.service.models.Role;
import com.aee.service.models.User;
import com.aee.service.repository.RoleRepository;
import com.aee.service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class AEEServiceApplication {

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	RoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(AEEServiceApplication.class, args);
	}
	@PostConstruct
	public void initialize() {
		createDefaultAdmin();
	}
	private void createDefaultAdmin(){
		User user = userRepository.findByUsername("admin").orElse(null);
		if (user == null){
			user = new User();
			user.setUsername("admin");
			user.setEmail("default@gmail.com");
			user.setPassword(encoder.encode("admin"));
			Set<Role> roles = new HashSet<>();
			Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(adminRole);
			user.setRoles(roles);
			userRepository.save(user);
		}
	}
}
