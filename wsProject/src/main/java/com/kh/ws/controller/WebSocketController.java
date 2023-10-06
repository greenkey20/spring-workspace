package com.kh.ws.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

// 2022.3.3(목) 15h5
@Controller
public class WebSocketController {
	
		@RequestMapping("/basic")
		public String basic() {
			return "websocket/basic";
		}
		
		// 2022.3.3(목) 16h10
		@RequestMapping("/group")
		public String group() {
			return "websocket/group";
		}
		
}
