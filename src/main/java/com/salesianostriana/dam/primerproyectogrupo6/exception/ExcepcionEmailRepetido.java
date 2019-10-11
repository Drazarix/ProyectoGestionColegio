/**
 * 
 */
package com.salesianostriana.dam.primerproyectogrupo6.exception;

/**
 * Esta clase sirve para que no se puedan registrar emails ya guardados en la
 * base de datos
 * 
 * @author Lucas Amado
 *
 */
public class ExcepcionEmailRepetido extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ExcepcionEmailRepetido () {
		
	}

	public ExcepcionEmailRepetido (String mensaje) {
		
		super ("Ese email ya ha sido registrado con anterioridad");
	}

}
