package com.javaspring.requestmodel;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtRequest implements Serializable{

	private static final long serialVersionUID = -2782729175155649001L;
	
	private String username;
	private String password;
	
	
}
