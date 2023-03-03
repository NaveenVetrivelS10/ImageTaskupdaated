package com.example.imageupload.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.imageupload.entity.ImageAttachment;
import com.example.imageupload.entity.ImageuploadOne;

public interface ImageUploadRepo1 extends JpaRepository<ImageuploadOne,Integer> {

}
