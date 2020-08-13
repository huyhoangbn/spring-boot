package com.javaspring.controller;

import com.javaspring.entity.Agentconnection;
import com.javaspring.model.ImageInfoRes;
import com.javaspring.model.Res;
import com.javaspring.service.ImageService;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequestMapping(path = "/test")
public class TestController {
	@Autowired
	ImageService imageService;
	
    @GetMapping("")
    public Res Test() {
    	Authentication auth1 = SecurityContextHolder.getContext().getAuthentication();
        Agentconnection cus = (Agentconnection) auth1.getPrincipal();
        return Res.success("Thành công", cus);
    }
    
    @PostMapping(value = "/thumbTestImage")
	public Res thumbTestImage(@RequestParam MultipartFile file,@RequestParam String size) throws IllegalStateException, IOException, NoSuchAlgorithmException {
		ImageInfoRes result = imageService.thumbTestImage(file,size);
    	return Res.success("Thành công",result );
	}
}
