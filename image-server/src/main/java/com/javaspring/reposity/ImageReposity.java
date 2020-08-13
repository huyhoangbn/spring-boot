package com.javaspring.reposity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.javaspring.entity.ImageInforEntity;

@Repository
public interface ImageReposity extends JpaRepository<ImageInforEntity, String>{

}
