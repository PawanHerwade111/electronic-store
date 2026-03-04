package com.electronic.store.dtos;

import com.electronic.store.validate.ImageNameValid;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserDto {

	private String userId;

	@Size(min = 3, max = 25, message = "Invalid Name !")
	private String name;

	//@Email(message = "Invalid User Email !")
	@Pattern(regexp = "^[a-z0-9][-a-z0-9._]+@([-a-z0-9]+\\.)+[a-z]{2,5}$", message = "Invalid User Email !")
	@NotBlank(message = "Email is required !")
	private String email;

	@NotBlank(message = "Password is required !")
	private String password;

	@Size(min = 4, max = 6, message = "Invalid Gender !")
	private String gender;

	@NotBlank(message = "Write something about yourself !")
	private String about;

	@ImageNameValid
	private String imageName;
}
