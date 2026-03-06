package com.electronic.store.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
	
	String uploadFile(MultipartFile file, String path) throws IOException ;
	
	InputStream getReSource(String path, String name) throws FileNotFoundException;

}
