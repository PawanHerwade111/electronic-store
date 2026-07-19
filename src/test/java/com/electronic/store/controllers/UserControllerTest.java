package com.electronic.store.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.electronic.store.dtos.PageableResponse;
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
	
	@Test
	public void updateUserTest() throws Exception {
		//  /user + put request +json +userId
		String userId = "123";
		UserDto dto = this.mapper.map(user, UserDto.class);
		Mockito.when(userService.updateUSer(Mockito.any(), Mockito.anyString())).thenReturn(dto);
		this.mockMvc.perform(MockMvcRequestBuilders.put("/users/"+userId)
						.header(HttpHeaders.AUTHORIZATION, "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwYXdhbkBnbWFpbC5jb20iLCJpYXQiOjE3ODQ0NzcxMzEsImV4cCI6MTc4NDQ5NTEzMX0.H0tOL5JBOWWNg_hoitR9LIqEbg0ILGCRFUZldhpVb7pJbu5eBMKebVPr9U24xZxSdlq6eJiaM1cwuyQndWSXqw")
						.contentType(MediaType.APPLICATION_JSON)
						.content(convertObjectToJsonString(user))
						.accept(MediaType.APPLICATION_JSON))
						.andDo(print())
						.andExpect(status().isOk())
						.andExpect(jsonPath("$.name").exists());
		
	}
	
	@Test
	public void getAllUsersTest() throws Exception {
		UserDto user = UserDto.builder()
				.name("Pawan")
				.email("pawan@gmail.com")
				.about("I am developer.")
				.gender("Male")
				.imageName("pawan.png")
				.password("pawan")
				//.roles(List.of(role))
				.build();	
		UserDto user1 = UserDto.builder()
				.name("Allu")
				.email("pawan@gmail.com")
				.about("I am developer.")
				.gender("Male")
				.imageName("pawan.png")
				.password("pawan")
				//.roles(List.of(role))
				.build();
		UserDto user2 = UserDto.builder()
				.name("Ram")
				.email("pawan@gmail.com")
				.about("I am developer.")
				.gender("Male")
				.imageName("pawan.png")
				.password("pawan")
				//.roles(List.of(role))
				.build();
		UserDto user3 = UserDto.builder()
				.name("Nagarjuna")
				.email("pawan@gmail.com")
				.about("I am developer.")
				.gender("Male")
				.imageName("pawan.png")
				.password("pawan")
				//.roles(List.of(role))
				.build();
		PageableResponse<UserDto> pageableResponse = new PageableResponse<>();
		pageableResponse.setContent(Arrays.asList(user,user1,user2,user3));
		pageableResponse.setLastPage(false);
		pageableResponse.setPageSize(10);
		pageableResponse.setTotalPages(100);
		pageableResponse.setTotalElements(1000);
		Mockito.when(userService.getAllUser(Mockito.anyInt(),Mockito.anyInt(), Mockito.anyString(), Mockito.anyString())).thenReturn(pageableResponse);
		this.mockMvc.perform(MockMvcRequestBuilders.get("/users")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk());
	}
	
}
