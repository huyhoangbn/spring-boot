package com.javaspring.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse implements Serializable{
	
	private static final long serialVersionUID = -5557166002963157240L;
	private String token;
	
}
