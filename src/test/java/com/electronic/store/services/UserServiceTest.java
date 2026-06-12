package com.electronic.store.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.electronic.store.dtos.PageableResponse;
import com.electronic.store.dtos.UserDto;
import com.electronic.store.entities.Role;
import com.electronic.store.entities.User;
import com.electronic.store.repositories.RoleRepository;
import com.electronic.store.repositories.UserRepository;


@SpringBootTest
@ActiveProfiles("local")
public class UserServiceTest {

	@MockitoBean
	private UserRepository userRepository;

	@MockitoBean
	private RoleRepository roleRepository;

	@Autowired
	private UserService userService;

	User user;

	Role role;

	String roleId;

	@Autowired
	private ModelMapper mapper;

	@BeforeEach
	public void init() {
		role = Role.builder()
				.roleId("abc")
				.name("NORMAL")
				.build();

		user = User.builder()
				.name("Pawan")
				.email("pawan@gmail.com")
				.about("I am developer.")
				.gender("Male")
				.imageName("pawan.png")
				.password("pawan")
				.roles(List.of(role))
				.build();
		

		roleId = "abc";

	}

	// create user
	@Test
	public void createUserTest() {
		Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);
		Mockito.when(roleRepository.findById(Mockito.anyString())).thenReturn(Optional.of(role));

		UserDto userDto1 = userService.createUser(mapper.map(user, UserDto.class));
		System.out.println("name:: " + userDto1.getName());

		Assertions.assertNotNull(userDto1);
		Assertions.assertEquals("Pawan", userDto1.getName());
	}

	// update user
	@Test
	public void updateUserTest() {
		String userId = "abcdef";
		UserDto userdto = UserDto.builder()
				.name("Pawan Herwade")
				.about("I am Backend developer.")
				.gender("Male")
				.imageName("pawanherwade.png")
				.build();
		
		Mockito.when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));
		Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);
		
		UserDto updatedUser = userService.updateUSer(userdto, userId);
		System.out.println("updatedUser name:: " + updatedUser.getName());
		System.out.println("updatedUser name:: " + updatedUser.getImageName());
		System.out.println("user name:: " + user.getName());
		Assertions.assertNotNull(userdto);
		Assertions.assertEquals(userdto.getName(), updatedUser.getName(), "Name is not valided");
	}
	
	//delete user
	@Test
	public void deleteUserTest() {
		String userId = "abcdef";

		Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

		userService.deleteUser(userId);
		Mockito.verify(userRepository, Mockito.times(1)).delete(user);
	}
	
	//get all user
	@Test
	public void getAllUserTest() {
		User user1 = User.builder()
				.name("don")
				.email("don@gmail.com")
				.about("I am developer.")
				.gender("Male")
				.imageName("don.png")
				.password("don")
				.roles(List.of(role))
				.build();
		
		User user2 = User.builder()
				.name("Shawn")
				.email("Shawn@gmail.com")
				.about("I am developer.")
				.gender("Male")
				.imageName("Shawn.png")
				.password("Shawn")
				.roles(List.of(role))
				.build();
		List<User> userList = Arrays.asList(user, user1, user2);
		Page<User> page = new PageImpl<>(userList);

		Mockito.when(userRepository.findAll((Pageable) Mockito.any())).thenReturn(page);
		PageableResponse<UserDto> allUser = userService.getAllUser(1, 2, "name", "desc");
		Assertions.assertEquals(3, allUser.getContent().size());
	}
	

}
