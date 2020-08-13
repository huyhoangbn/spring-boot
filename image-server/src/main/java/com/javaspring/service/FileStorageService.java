package com.javaspring.service;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import com.javaspring.exception.MyFileNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileStorageService {

	public Resource loadFileAsResource(String fileName) {
		try {
			String pathFileReader = ImageService.getPath();
			Path filePath = Paths.get(pathFileReader).resolve(fileName).normalize();
			log.debug(String.valueOf(filePath));
			Resource resource = new UrlResource(filePath.toUri());
 			log.debug(String.valueOf(resource));
			if (resource.exists()) {
				return resource;
			} else {
				throw new MyFileNotFoundException("File not found " + fileName);
			}
		} catch (MalformedURLException ex) {
			throw new MyFileNotFoundException("File not found " + fileName, ex);
		}
	}
}
