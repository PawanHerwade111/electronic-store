package com.electronic.store.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.electronic.store.dtos.ApiResponseMessage;
import com.electronic.store.dtos.ImageResponse;
import com.electronic.store.dtos.PageableResponse;
import com.electronic.store.dtos.UserDto;
import com.electronic.store.services.FileService;
import com.electronic.store.services.UserService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private FileService fileService;
	
	@Value("${user.profile.image.path}")
	private String imageUploadPath;
	
	private Logger logger = LoggerFactory.getLogger(UserController.class);

	// create user
	@PostMapping
	public ResponseEntity<UserDto> createUser( @Valid @RequestBody UserDto userDto) {
		UserDto userDto1 = userService.createUser(userDto);
		return new ResponseEntity<>(userDto1, HttpStatus.CREATED);

	}

	// update user
	@PutMapping("/{userId}")
	public ResponseEntity<UserDto> updateUser(@PathVariable("userId") String userId, @Valid @RequestBody UserDto userDto) {
		UserDto userDto1 = userService.updateUSer(userDto, userId);
		return new ResponseEntity<>(userDto1, HttpStatus.OK);
	}

	// delete user
	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable("userId") String userId) {
		userService.deleteUser(userId);
		ApiResponseMessage message = ApiResponseMessage
				.builder()
				.message("User is deleted Successfully.")
				.success(true)
				.status(HttpStatus.OK)
				.build();
		return new ResponseEntity<>(message, HttpStatus.OK);
	}

	// get all users
	@GetMapping
	public ResponseEntity<PageableResponse<UserDto>> getAllUsers(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value ="pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "name", required = false) String sortBy,
			@RequestParam(value ="sortDir", defaultValue = "asc", required = false) String sortDir) {
		return new ResponseEntity<>(userService.getAllUser(pageNumber, pageSize, sortBy, sortDir), HttpStatus.OK);
	}

	// get single user
	@GetMapping("/{userId}")
	public ResponseEntity<UserDto> getUser(@PathVariable("userId") String userId) {
		return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
	}

	// get user by email
	@GetMapping("/email/{email}")
	public ResponseEntity<UserDto> getUserByEmail(@PathVariable("email") String email) {
		return new ResponseEntity<>(userService.getUserByEmail(email), HttpStatus.OK);
	}

	// search user
	@GetMapping("/search/{keywords}")
	public ResponseEntity<List<UserDto>> searchUser(@PathVariable("keywords") String keywords) {
		return new ResponseEntity<>(userService.searchUser(keywords), HttpStatus.OK);
	}
	
	
	// upload user image
	@PostMapping("/image/{userId}")
	public ResponseEntity<ImageResponse> uploadUserImage(@RequestParam("userImage") MultipartFile image,
			@PathVariable("userId") String userId) throws IOException {
		String imageName = fileService.uploadFile(image, imageUploadPath);
		UserDto userDto = userService.getUserById(userId);
		userDto.setImageName(imageName);
		userService.updateUSer(userDto, userId);
		ImageResponse imageResponse = ImageResponse.builder().imageName(imageName).message("Image Uploaded Successfully.").success(true)
				.status(HttpStatus.CREATED).build();
		return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);
	}
	
	//serve user image
	@GetMapping("/image/{userId}")
	public void serveUserImage(@PathVariable("userId") String userId, HttpServletResponse response) throws IOException {
		UserDto user = userService.getUserById(userId);
		logger.info("user image name is:: "+user.getImageName());
		InputStream resource = fileService.getReSource(imageUploadPath, user.getImageName());
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(resource, response.getOutputStream());
	}
}
