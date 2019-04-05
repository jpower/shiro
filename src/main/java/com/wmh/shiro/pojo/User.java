package com.wmh.shiro.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class User implements Serializable{

	private static final long serialVersionUID = -5440372534300871944L;
	
	private Integer id;
	private String username;
	private String password;
	private Date creatime;
	private String status;

}
