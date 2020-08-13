package com.javaspring.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "image")
public class ImageInforEntity {
	@Id
	private String id;
	@Column
	private String url;
	@Column
	private String fileName;
	@Column
	private String capacity;
	@Column
	private String fileType;
	@Column
	private String size;
	@Column
	private LocalDateTime createdDate;
	@Column
	private String idMaster;
	
}
