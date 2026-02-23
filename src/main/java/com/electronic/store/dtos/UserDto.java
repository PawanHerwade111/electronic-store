package com.electronic.store.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserDto {

	private String userId;

	private String name;

	private String email;

	private String password;

	private String gender;

	private String about;

	private String imageName;
}
