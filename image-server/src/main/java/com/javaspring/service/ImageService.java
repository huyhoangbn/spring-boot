package com.javaspring.service;

import java.awt.image.BufferedImage;
import java.awt.image.ImagingOpException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.google.gson.Gson;
import com.javaspring.entity.Agentconnection;
import com.javaspring.entity.ImageInforEntity;
import com.javaspring.model.ImageInfoRes;
import com.javaspring.model.ThumbImage;
import com.javaspring.properties.FileStorageProperties;
import com.javaspring.reposity.AgenconnectionRepository;
import com.javaspring.reposity.GraphicImage;
import com.javaspring.reposity.ImageReposity;
import com.javaspring.requestmodel.SizeImage;
import com.javaspring.requestmodel.SizeRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ImageService {

	@Autowired
	ImageReposity imageReposity;
	@Autowired
	AgenconnectionRepository agenconnectionRepository;
	@Autowired
	GraphicImage graphicImage;
	@Autowired
	FileStorageProperties fileStorageProperties;
	

	@Value("${server.contextPath}")
	public String urlServer;

	public static final String TEXT_LOGO = "EzCloud";

	public static LocalDateTime timeUpload = java.time.LocalDateTime.now(); // thời điểm up ảnh
	public static String year = String.valueOf(timeUpload).substring(0, 4);
	public static String month = String.valueOf(timeUpload).substring(5, 7);
	
	
	//public static final String PATH_STRING_REAL = "E:/ezcloud/upload/merchant/hotel/images/" + year + "/" + month + "/" ;
	public static String getPath() {
		// Lấy đường dẫn link
		Authentication auth1 = SecurityContextHolder.getContext().getAuthentication();
		Agentconnection cus = (Agentconnection) auth1.getPrincipal();
		       
		//String PATH_STRING_REAL = fileStorageProperties.getUploadDir()+cus.getMerchant()+"/hotel/images/" + year + "/" + month + "/" ;
	    String PATH_STRING_REAL = "E:/ezcloud/upload/"+cus.getMerchant()+"/hotel/images/" + year + "/" + month + "/" ;
	    return PATH_STRING_REAL;
	}

	public ImageInfoRes thumbTestImage(MultipartFile file, String size) throws IllegalStateException, IOException, NoSuchAlgorithmException {
		String PATH_STRING_REAL = getPath();
		List<ThumbImage> listImageThumb =new ArrayList<ThumbImage>();	
		String pathRoot = PATH_STRING_REAL;
		File pathFileRoot = new File(pathRoot);
		if (!pathFileRoot.exists()) {
			pathFileRoot.mkdirs();
		}
		

		// Xử lí trường id
		String idImage = UUID.randomUUID().toString(); // UUID
		String idImageMaster = idImage;

		// Xử lý băm tên thành chữ ký
		String originalName = file.getOriginalFilename();
		String reverseName = "";
		String insteadName = "";
		char[] stt = originalName.toCharArray();
		for (int i = stt.length - 1; i >= 0; i--) {
			reverseName.concat(String.valueOf(stt[i]));
		}
		insteadName = originalName.concat(reverseName);
		String signature = insteadName + String.valueOf(java.time.LocalDateTime.now()); // thời gian thực
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hash = digest.digest(signature.getBytes(StandardCharsets.UTF_8));
		signature = DatatypeConverter.printHexBinary(hash);

		
		String newName = signature.substring(0, 21);

		//Đường dẫn đến ảnh gốc
		String pathFile = pathRoot + newName + "." + file.getContentType().split("/")[1];
		File pathsource = new File(pathFile);
		if (!pathsource.exists()) {
			pathsource.mkdirs();
		}
		
		//Đường dẫn tương đối file gốc
		String fileDownloadUriSource = ServletUriComponentsBuilder.fromCurrentContextPath().path(pathFile.split("ezcloud")[1]).toUriString();

		// Xử lí trường type và sizeFile
		String sizeFile = "";
		String fileType = file.getContentType();
		BufferedImage image = null;
		if (fileType.equals("image/jpeg") || fileType.equals("image/png")) {
			//Nếu là loại ảnh thì đọc file ảnh vào thư mục source và lấy size của ảnh
			file.transferTo(pathsource);
			image = ImageIO.read(new File(pathFile));
			sizeFile += image.getWidth() + "x" + image.getHeight();
			
		} else {
			String pathFileDifferent = pathRoot + "docs/" + newName + "." + file.getContentType().split("/")[1];
			File fileDoc = new File(pathFileDifferent);
			if(!fileDoc.exists())
				fileDoc.mkdirs();	
			file.transferTo(fileDoc);
			ImageInfoRes output2 = new ImageInfoRes(idImage,pathFileDifferent.split("ezcloud")[1],file.getOriginalFilename(),String.valueOf(file.getSize() / 1024) + "Kb",
					file.getContentType(),sizeFile,java.time.LocalDateTime.now(),null);
		
			return output2;
		}

		//Tạo ảnh thumb
		BufferedImage thumbImage = null;
		// Xử lí kích thước ảnh
		// Đọc từng size ảnh
		int ix = image.getWidth();
		int iy = image.getHeight();
		int dx = 0, dy = 0;
		Gson gson = new Gson();
		SizeRequest[] listRequests = gson.fromJson(size, SizeRequest[].class);
		log.debug(String.valueOf(listRequests.length));
		List<SizeImage> listSize = new ArrayList<SizeImage>();
		List<Integer> countThumb = new ArrayList<Integer>();
		for (int i = 0; i < listRequests.length; i++) {
			String sizeString = listRequests[i].getSize();
			int typeThumb = listRequests[i].getType();
			countThumb.add(typeThumb);
			log.debug(String.valueOf(typeThumb));
			listSize.add(new SizeImage(Integer.parseInt(sizeString.split("x")[0]),
					Integer.parseInt(sizeString.split("x")[1])));
			dx = Integer.parseInt(sizeString.split("x")[0]);
			dy = Integer.parseInt(sizeString.split("x")[1]);
			switch (typeThumb) {
			case 0: //  ảnh gốc
				thumbImage = image;
				ImageIO.write(thumbImage, file.getContentType().split("/")[1], pathsource);
				
				graphicImage.addTextWatermark(TEXT_LOGO, new File(pathFile), pathsource);
				
				ImageInforEntity imageInfoResSource = new ImageInforEntity(idImage, fileDownloadUriSource, file.getOriginalFilename(),
						String.valueOf(file.getSize() / 1024) + "Kb", file.getContentType(), sizeFile,
						java.time.LocalDateTime.now(), idImageMaster);
				//listImageThumb.add(new ThumbImage(fileDownloadUriSource,sizeFile));
				//listDb.add(imageInfoResSource);
				imageReposity.save(imageInfoResSource);

				break;
			case 1:// resize 
				thumbImage = Scalr.resize(image, Scalr.Method.SPEED, Scalr.Mode.FIT_TO_WIDTH, dx, dy,
						Scalr.OP_ANTIALIAS);
				String thumbPathResize = "";
				// Băm tên resize
				String newNameResize = newName;
				MessageDigest digestResize = MessageDigest.getInstance("SHA-256");
				byte[] hashResize = digestResize.digest(newNameResize.getBytes(StandardCharsets.UTF_8));
				newNameResize = DatatypeConverter.printHexBinary(hashResize);
				thumbPathResize = PATH_STRING_REAL + newNameResize.substring(0,21) +"." + fileType.split("/")[1];
				File fileInputResize = new File(thumbPathResize);
				if (!fileInputResize.exists()) {
					fileInputResize.mkdirs();
				}
				ImageIO.write(thumbImage, "jpg", new File(thumbPathResize));
				graphicImage.addTextWatermark(TEXT_LOGO, fileInputResize, new File(thumbPathResize));
				
				String fileDownloadUriResize = ServletUriComponentsBuilder.fromCurrentContextPath()
						.path(thumbPathResize.split("ezcloud")[1]).toUriString();
				// add url va file của ảnh thumb
				listImageThumb.add(new ThumbImage(fileDownloadUriResize, thumbImage.getWidth()+"x"+thumbImage.getHeight()));
				String idResizeString = UUID.randomUUID().toString();
				ImageInforEntity imageInfoResResize = new ImageInforEntity(idResizeString, fileDownloadUriSource, file.getOriginalFilename(),
						String.valueOf(file.getSize() / 1024) + "Kb", file.getContentType(), sizeFile,
						java.time.LocalDateTime.now(), idImageMaster);
				//listImageThumb.add(new ThumbImage(fileDownloadUriSource,sizeFile));
				//listDb.add(imageInfoResSource);
				imageReposity.save(imageInfoResResize);
				
				break;
			case 2: // crop
				try {
					thumbImage = Scalr.resize(image, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.AUTOMATIC, dx, dy);
					if (ix > dx) {
						thumbImage = Scalr.crop(image, (ix - dx) / 2, 0, dx, dy);
					} else if (iy > dy) {
						thumbImage = Scalr.crop(image, 0, (iy - dy) / 2, dx, dy);
					}
				} catch (IllegalArgumentException | ImagingOpException e) {
					System.out.println("imgscalr threw an exception: " + e.getMessage());
				}
				// băm tên crop
				String newNameCrop= "";
				for (int j=newName.length()-1;j>0;j--) {
					newNameCrop += newName.charAt(j);
				}
				MessageDigest digestCrop = MessageDigest.getInstance("SHA-256");
				byte[] hashCrop = digestCrop.digest(newNameCrop.getBytes(StandardCharsets.UTF_8));
				newNameCrop = DatatypeConverter.printHexBinary(hashCrop);
				String thumbPathCrop = "";
				thumbPathCrop = PATH_STRING_REAL + newNameCrop.substring(0,21) + "." + fileType.split("/")[1];
				File fileInputCrop = new File(thumbPathCrop);
				if (!fileInputCrop.exists()) {
					fileInputCrop.mkdirs();
				}
				ImageIO.write(thumbImage, "jpg", new File(thumbPathCrop));
				// add logo ảnh và lưu luôn tại path đó
				graphicImage.addTextWatermark(TEXT_LOGO, fileInputCrop, new File(thumbPathCrop));

				String fileDownloadUriCrop = ServletUriComponentsBuilder.fromCurrentContextPath()
						.path(thumbPathCrop.split("ezcloud")[1]).toUriString();
				listImageThumb.add(new ThumbImage(fileDownloadUriCrop,thumbImage.getWidth()+"x"+thumbImage.getHeight()));
	
				String idCrop = UUID.randomUUID().toString();
				ImageInforEntity imageInfoResCrop = new ImageInforEntity(idCrop, fileDownloadUriSource, file.getOriginalFilename(),
						String.valueOf(file.getSize() / 1024) + "Kb", file.getContentType(), sizeFile,
						java.time.LocalDateTime.now(), idImageMaster);
				//listImageThumb.add(new ThumbImage(fileDownloadUriSource,sizeFile));
				//listDb.add(imageInfoResSource);
				imageReposity.save(imageInfoResCrop);
				break;

			default:
				return null;
			}
		}

		
		/**
		ImageInfoRes imageInfoResSource = new ImageInfoRes(idImage, fileDownloadUriSource, file.getOriginalFilename(),
				String.valueOf(file.getSize() / 1024) + "Kb", file.getContentType(), sizeFile,
				LocalDateTime.now(), uuid_image);*/
		ImageInfoRes output = new ImageInfoRes(idImage,fileDownloadUriSource,file.getOriginalFilename(),String.valueOf(file.getSize() / 1024) + "Kb",
				file.getContentType(),sizeFile,java.time.LocalDateTime.now(),listImageThumb);
		//imageReposity.findAll().forEach(listDb::add);
		return output;
	}

	

}
