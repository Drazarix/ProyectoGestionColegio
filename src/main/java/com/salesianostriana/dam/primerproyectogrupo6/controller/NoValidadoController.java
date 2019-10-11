/**
 * 
 */
package com.salesianostriana.dam.primerproyectogrupo6.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Esta clase permite crear una ruta para aquellos usuarios que aún no han sido validados por un admin
 * @author Lucas Amado
 *
 */

@Controller
@RequestMapping("/noValidate")
public class NoValidadoController {
	
	/**
	 * Al no estar validado este método nos envia a una página de error por validación
	 * @param model
	 * @return Nos devuelve el html de no validado
	 */
	@GetMapping("/")
	public String sinValidar(Model model) {
		return "noValidate/esperar.html";
	}

}
