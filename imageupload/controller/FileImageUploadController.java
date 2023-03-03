package com.example.imageupload.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.imageupload.entity.ImageAttachment;
import com.example.imageupload.errorHandaler.CustomFileExtension;
import com.example.imageupload.errorHandaler.ResponseHandler;
import com.example.imageupload.repository.ImageUploadRepo;
import com.example.imageupload.service.ImageHandlerService;

@RestController
public class FileImageUploadController {
	private static final Logger loc = LoggerFactory.getLogger(FileImageUploadController.class);

	@Autowired
	private ImageHandlerService imageHandlerService;

	/**
	 * This Method is used for to upload the Image
	 * 
	 * @param file name of the image
	 * @return response with message and HTTP status
	 * @throws Exception
	 */
	@PostMapping("/uploadFile")
	public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile files) throws Exception {
		String statusMsg = null;
		if (files != null && !files.isEmpty()) {
			String extension = files.getOriginalFilename().substring(files.getOriginalFilename().lastIndexOf(".") + 1);
			System.out.println(extension);

			if ((extension.equalsIgnoreCase(CustomFileExtension.extension))) {
				// if (!files.getOriginalFilename().toLowerCase().endsWith(".png")) {
				statusMsg = imageHandlerService.uploadImage(files);
				if (statusMsg.contains(ResponseHandler.FILE_UPLOAD_FAILED)) {
					return ResponseHandler.response(statusMsg, HttpStatus.INTERNAL_SERVER_ERROR);
				} else {
					return ResponseHandler.successResponse(statusMsg, HttpStatus.ACCEPTED);
				}

			} else {
				return ResponseHandler.response(ResponseHandler.UNSUPPORTED_FILE_FORMAT, HttpStatus.BAD_REQUEST);
			}
		} else

		{
			return ResponseHandler.response(ResponseHandler.FILE_NOT_UPLOADED_ERR_MSG, HttpStatus.NOT_FOUND);
		}
	}

	
	/**
	 * This method is used converting the image to Base64 String format
	 * 
	 * @param imageId
	 * @return response with message and HTTP status
	 */

	@GetMapping("/convertBase64ByImageId")
	public ResponseEntity<?> convertBase64ByImageId(
			@RequestParam(value = "imageId", required = false) Integer imageId) {

		if (imageId != null) {
			return imageHandlerService.getImageById(imageId);
		} else {
			return ResponseHandler.response("Image ID should not be null or empty.", HttpStatus.BAD_REQUEST);
		}

	}

}
