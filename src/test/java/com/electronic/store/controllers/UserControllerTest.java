package com.electronic.store.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.electronic.store.dtos.UserDto;
import com.electronic.store.entities.Role;
import com.electronic.store.entities.User;
import com.electronic.store.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@ActiveProfiles("local")
@AutoConfigureMockMvc
public class UserControllerTest {
	
	@MockitoBean
	private UserService userService;
	
	User user;

	Role role;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private MockMvc mockMvc;
	
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

	}

	@Test
	public void createUserTest() throws Exception {
		//	/users+POST+user data as json+status created
		UserDto dto = mapper.map(user, UserDto.class);
		
		Mockito.when(userService.createUser(Mockito.any())).thenReturn(dto);
		
		//actual request for url
		this.mockMvc.perform(
				MockMvcRequestBuilders.post("/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(convertObjectToJsonString(user))
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.name").exists());
	}

	private String convertObjectToJsonString(Object user) {
		try {
			return new ObjectMapper().writeValueAsString(user);
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
