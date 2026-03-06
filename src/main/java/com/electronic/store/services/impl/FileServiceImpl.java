package com.electronic.store.services.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.electronic.store.exceptions.BadApiRequest;
import com.electronic.store.services.FileService;

@Service
public class FileServiceImpl implements FileService {

	private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

	@Override
	public String uploadFile(MultipartFile file, String path) throws IOException {

		// abc.png
		String originalFileName = file.getOriginalFilename();
		logger.info("Original File name is:: " + originalFileName);
		String fileName = UUID.randomUUID().toString();
		String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
		String fileNameWithExtension = fileName + extension;
		String fullPathWithFileName = path +  fileNameWithExtension;
		if (extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpg")
				|| extension.equalsIgnoreCase(".jpeg")) {
			logger.info("File extension is:: " + extension);
			// save file
			File folder = new File(path);
			if (!folder.exists()) {
				// create folder
				logger.info("creating folder.");
				folder.mkdirs();
			}
			// upload
			Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));
			return fileNameWithExtension;

		} else {
			throw new BadApiRequest("File with this extension is not allowed!");
		}
	}

	@Override
	public InputStream getReSource(String path, String name) throws FileNotFoundException {
		String fullPath = path + File.separator + name;
		InputStream inputStream = new FileInputStream(fullPath);
		return inputStream;
	}

}
