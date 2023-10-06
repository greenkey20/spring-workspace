package com.kh.spring.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// 2022.3.15(화) 11h40
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CertVo {
	private String who, secret, when; // 변수 선언 시, 같은 자료형 변수들은 comma로 나열해서 선언 가능 vs 한 줄 한 줄 쓰는 게 더 좋은 방법이긴 함
	// WHO	VARCHAR2(23 BYTE)	nullable No
	// SECRET	VARCHAR2(6 BYTE)	nullable No
	// WHEN	DATE	nullable No
}
