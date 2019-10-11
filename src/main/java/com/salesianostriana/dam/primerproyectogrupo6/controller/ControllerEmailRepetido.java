/**
 * 
 */
package com.salesianostriana.dam.primerproyectogrupo6.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.salesianostriana.dam.primerproyectogrupo6.exception.ExcepcionEmailRepetido;

/**
 * Esta clase permite en caso de que un usuario se intente registrar con un
 * email ya guardado en la base de datos se le envíe a una página de error
 * 
 * @author Lucas Amado
 *
 */

@ControllerAdvice
public class ControllerEmailRepetido {
	@ExceptionHandler(ExcepcionEmailRepetido.class)
	public String excepcioncarrito(Model model, ControllerEmailRepetido ecv) {

		model.addAttribute("excep", ecv);
		return "usuario/errores/mismoEmail.html";
	}
}
