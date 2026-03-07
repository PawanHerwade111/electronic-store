package com.electronic.store.services.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.electronic.store.dtos.PageableResponse;
import com.electronic.store.dtos.UserDto;
import com.electronic.store.entities.User;
import com.electronic.store.exceptions.ResourceNotFoundException;
import com.electronic.store.helper.Helper;
import com.electronic.store.repositories.UserRepository;
import com.electronic.store.services.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelMapper mapper;
	
	@Value("${user.profile.image.path}")
	private String imagePath;
	
	private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

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
		// delete user profile image
		String fullPath = imagePath + user.getImageName();
		logger.info("fullPath is:: " + fullPath);
		try {
			Path path = Paths.get(fullPath);
			Files.delete(path);
		} catch (NoSuchFileException e) {
			logger.info("User image not found in folder!");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// delete user
		userRepository.delete(user);

	}

	@Override
	public PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir) {
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		// pageNumber default starts from 0
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<User> page = userRepository.findAll(pageable);
//		List<User> usersList = page.getContent();
//		List<UserDto> usersDtoList = usersList.stream().map(user -> mapEntityToDto(user)).collect(Collectors.toList());
//		PageableResponse<UserDto> response = new PageableResponse<>();
//		response.setContent(usersDtoList);
//		response.setPageNumber(page.getNumber());
//		response.setPageSize(page.getSize());
//		response.setTotalElements(page.getTotalElements());
//		response.setTotalPages(page.getTotalPages());
//		response.setLastPage(page.isLast());
		PageableResponse<UserDto> response = Helper.getPageableResponse(page, UserDto.class);
		return response;
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
