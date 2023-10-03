package com.kh.spring.common.run;

import com.kh.spring.common.model.vo.Product;

// 2022.3.4(금) 11h
public class Run {

	public static void main(String[] args) {
		
		Product pd = new Product();
		
		pd.setId("p001");
		pd.setName("상품1");
		pd.setBox("b12");
		pd.setCreateDate("2022-03-02");
		pd.setStatus("Y");
		pd.setLocation("l1");

		System.out.println(pd);

		// builder pattern
		Product pd2 = Product.builder()
				.id("p002")
				.name("상품2")
				.createDate("2022-03-03")
				.status("N")
				.location("l3")
				.quantity(0)
				.build();

		System.out.println(pd2);
	}

}
