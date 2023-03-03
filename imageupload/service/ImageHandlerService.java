package com.example.imageupload.service;

import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.imageupload.entity.ImageAttachment;
import com.example.imageupload.entity.ImageuploadOne;

import com.example.imageupload.errorHandaler.ResponseHandler;
import com.example.imageupload.repository.ImageUploadRepo;
import com.example.imageupload.repository.ImageUploadRepo1;

@Service
public class ImageHandlerService implements ImageHandlerInterface {
	private String location = "D:/JavaTrainning/Imageupload/imageupload/imageupload/src/main/resources/static/image";
	@Autowired
	private ImageUploadRepo imageUploadRepo;

	@Autowired
	private ImageUploadRepo1 imageUploadRepo1;

	@Override
	public String uploadImage(MultipartFile file) throws Exception {
		String msg = null;
		try {

			Files.copy(file.getInputStream(), Paths.get(location + File.separator + file.getOriginalFilename()),
					StandardCopyOption.REPLACE_EXISTING);
			ImageAttachment imageAttachment = new ImageAttachment();
			imageAttachment.setImagename(file.getOriginalFilename());
			imageAttachment.setImageType(file.getContentType());
			imageAttachment.setImagePath(location);
			imageUploadRepo.save(imageAttachment);
			ImageuploadOne imageuploadOne = new ImageuploadOne();
			imageuploadOne.setImagename(file.getOriginalFilename());
			imageuploadOne.setImageType(file.getContentType());
			imageuploadOne.setImagePath(location);

			imageUploadRepo1.save(imageuploadOne);
			msg = ResponseHandler.FILE_UPLOAD_SUCCESS_MSG;
		} catch (Exception e) {

			msg = ResponseHandler.FILE_UPLOAD_FAILED + " due to " + ResponseHandler.FILE_ISNOT_AVAILABLE;
		}
		return msg;
	}

	

	@Override
	public ResponseEntity<?> getImageById(Integer imageId) {
		String content = null;
		byte[] arr = null;
		boolean isImageExists = imageUploadRepo.existsById(imageId);
		if (isImageExists) {
			ImageAttachment imageAttachment = imageUploadRepo.findById(imageId).get();
			if (imageAttachment != null) {
				String expectedPath = imageAttachment.getImagePath() + "/" + imageAttachment.getImagename();
				File file = new File(expectedPath);
				FileInputStream istream = null;
				try {
					istream = new FileInputStream(file);
					arr = new byte[(int) file.length()];
					istream.read(arr);
					istream.close();
					content = Base64.getEncoder().encodeToString(arr);
				} catch (IOException e) {
					content = ResponseHandler.FILE_READ_FAILED + " from " + expectedPath;
				}				
				return ResponseEntity.status(HttpStatus.ACCEPTED).body(content);
			} else {
				return ResponseHandler.response(ResponseHandler.FILE_NOT_FOUND, HttpStatus.BAD_REQUEST);
			}
		} else {
			return ResponseHandler.response(ResponseHandler.FILE_ID_NOT_FOUND, HttpStatus.BAD_REQUEST);
		}
	}
}