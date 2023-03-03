package com.example.imageupload.service;

import java.io.FileNotFoundException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ImageHandlerInterface {

	public abstract String uploadImage(MultipartFile file) throws Exception;
	


	public ResponseEntity<?> getImageById(Integer imageId);
	
	
}
