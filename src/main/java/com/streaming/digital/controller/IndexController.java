package com.streaming.digital.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.streaming.digital.backing.LectorCorreos;


@Controller 
public class IndexController {
	
	@Autowired
	private LectorCorreos lectorCorreos;
	
	@GetMapping("/")
	public String index() {
		return "consulta";
	}
	
	@PostMapping("/consulta")
	public String getMethodName(@RequestParam("correo") String pCorreo, Model model) {
		String salida = lectorCorreos.lectorCorreos(pCorreo, "BellaMerylAgosto", "Saludo");
		if(salida.contains("salida:")) {
			model.addAttribute("mensaje", salida.replace("salida:", ""));
		}else {
			model.addAttribute("error", salida.replace("error:", ""));
		}
		return "consulta";
	}
	
	
}
