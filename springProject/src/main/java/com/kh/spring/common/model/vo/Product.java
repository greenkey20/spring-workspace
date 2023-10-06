package com.kh.spring.common.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// 2022.3.4(금) 10h50

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
	
	private String id;
	private String name;
	private String box;
	private String createDate;
	private String status;
	private String location;
	private int quantity;
	
}
