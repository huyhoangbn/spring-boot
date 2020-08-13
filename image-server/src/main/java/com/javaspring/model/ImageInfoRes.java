package com.javaspring.model;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageInfoRes{
	
	private String id;
	private String url;
	private String fileName;
	private String capacity;
	private String fileType;
	private String size;
	private LocalDateTime createdDate;
	private List<ThumbImage> files;
	
	
}
