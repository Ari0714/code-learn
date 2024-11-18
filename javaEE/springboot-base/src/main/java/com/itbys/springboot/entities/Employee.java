package com.itbys.springboot.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

	private Integer id;
    private String lastName;
    private String email;
    private Integer gender;      //1 male, 0 female
    private Department department;
    private Date birth;


}
