package com.electronic.store.services.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.electronic.store.dtos.UserDto;
import com.electronic.store.entities.User;
import com.electronic.store.exceptions.ResourceNotFoundException;
import com.electronic.store.repositories.UserRepository;
import com.electronic.store.services.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelMapper mapper;

	@Override
	public UserDto createUser(UserDto userDto) {
		String userId = UUID.randomUUID().toString();
		userDto.setUserId(userId);
		User user = mapDtoToEntity(userDto);
		User savedUser = userRepository.save(user);
		UserDto newDto = mapEntityToDto(savedUser);
		return newDto;
	}

	private UserDto mapEntityToDto(User savedUser) {
//		UserDto userDto = UserDto.builder().userId(savedUser.getUserId()).name(savedUser.getName())
//				.email(savedUser.getEmail()).password(savedUser.getPassword()).about(savedUser.getAbout())
//				.gender(savedUser.getGender()).imageName(savedUser.getImageName()).build();

		return mapper.map(savedUser, UserDto.class);
	}

	private User mapDtoToEntity(UserDto userDto) {
//		User user = User.builder().userId(userDto.getUserId()).name(userDto.getName()).email(userDto.getEmail())
//				.password(userDto.getPassword()).about(userDto.getAbout()).gender(userDto.getGender())
//				.imageName(userDto.getImageName()).build();
		return mapper.map(userDto, User.class);
	}

	@Override
	public UserDto updateUSer(UserDto userDto, String userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with given userId."));
		user.setName(userDto.getName());
		user.setAbout(userDto.getAbout());
		user.setGender(userDto.getGender());
		user.setPassword(userDto.getPassword());
		user.setImageName(userDto.getImageName());
		User updatedUser = userRepository.save(user);
		UserDto updatedDto = mapEntityToDto(updatedUser);
		return updatedDto;
	}

	@Override
	public void deleteUser(String userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with given userId."));
		userRepository.delete(user);

	}

	@Override
	public List<UserDto> getAllUser() {
		List<User> usersList = userRepository.findAll();
		List<UserDto> usersDtoList = usersList.stream().map(user -> mapEntityToDto(user)).collect(Collectors.toList());
		return usersDtoList;
	}

	@Override
	public UserDto getUserById(String userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with given userId."));
		UserDto userDto = mapEntityToDto(user);
		return userDto;
	}

	@Override
	public UserDto getUserByEmail(String email) {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with given emailId."));
		UserDto userDto = mapEntityToDto(user);
		return userDto;
	}

	@Override
	public List<UserDto> searchUser(String keyword) {
		List<User> usersList = userRepository.findByNameContaining(keyword);
		List<UserDto> usersDtoList = usersList.stream().map(user -> mapEntityToDto(user)).collect(Collectors.toList());
		return usersDtoList;
	}

}
