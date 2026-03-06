package com.electronic.store.dtos;

import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ImageResponse {
	private String imageName;
	private String message;
	private boolean success;
	private HttpStatus status;
}
