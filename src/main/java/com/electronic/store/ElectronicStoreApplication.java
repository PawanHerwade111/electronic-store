package com.electronic.store;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.electronic.store.entities.Role;
import com.electronic.store.entities.User;
import com.electronic.store.repositories.RoleRepository;
import com.electronic.store.repositories.UserRepository;

@SpringBootApplication
@EnableWebMvc
public class ElectronicStoreApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ElectronicStoreApplication.class, args);
	}

	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private UserRepository userRepository;

	@Override
	public void run(String... args) throws Exception {

		System.out.println("Encoded Password is:: " + passwordEncoder.encode("pawan"));

		Role roleAdmin = roleRepository.findByName("ROLE_ADMIN").orElse(null);
		Role role1 = null;
		if (roleAdmin == null) {
			role1 = new Role();
			role1.setRoleId(UUID.randomUUID().toString());
			role1.setName("ROLE_ADMIN");
			roleRepository.save(role1);
		}

		Role roleNormal = roleRepository.findByName("ROLE_NORMAL").orElse(null);
		if (roleNormal == null) {
			Role role2 = new Role();
			role2.setRoleId(UUID.randomUUID().toString());
			role2.setName("ROLE_NORMAL");
			roleRepository.save(role2);
		}

		User user = userRepository.findByEmail("pawan@gmail.com").orElse(null);
		if (user == null) {
			user = new User();
			user.setName("pawan");
			user.setEmail("pawan@gmail.com");
			user.setPassword(passwordEncoder.encode("pawan"));
			if (roleAdmin == null)
				user.setRoles(List.of(role1));
			else
				user.setRoles(List.of(roleAdmin));
			user.setUserId(UUID.randomUUID().toString());

			userRepository.save(user);
		}

	}

}
