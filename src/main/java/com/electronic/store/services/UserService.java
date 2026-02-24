package com.electronic.store.services;

import java.util.List;

import com.electronic.store.dtos.UserDto;

public interface UserService {

	// create
	UserDto createUser(UserDto user);

	// update
	UserDto updateUSer(UserDto userDto, String userId);

	// delete
	void deleteUser(String userId);

	// get all users
	List<UserDto> getAllUser();

	// get single user by id
	UserDto getUserById(String userId);

	// get single user by email
	UserDto getUserByEmail(String userId);

	// search user
	List<UserDto> searchUser(String keyword);

	// other user specific features

}
