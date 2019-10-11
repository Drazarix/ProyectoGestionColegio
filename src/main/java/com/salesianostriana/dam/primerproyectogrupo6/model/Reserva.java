/**
 * 
 */
package com.salesianostriana.dam.primerproyectogrupo6.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase POJO que modela la entidad Reserva
 * 
 * @author lamado
 */

@Data
@Entity
@NoArgsConstructor
public class Reserva {

	/** 
	 * Atributo id para diferenciar las reservas
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 * Atributo usuario al que pertenece la reserva
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Usuario usuario;

	/**
	 * Atributo aula de la cual se ha hecho la reserva
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Aula aula;
	
	/**
	 * Atributo fecha en la que se ha hecho la reserva
	 */
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private LocalDate fecha;
	
	/**
	 * Atributo hora a la que se ha hecho la reserva
	 */
	private String horaI;

	/**
	 * Constructor de la reserva
	 * @param usuario
	 * @param aula
	 * @param hora
	 * @param fecha
	 * @param horaI
	 */
	public Reserva(Usuario usuario, Aula aula, String hora, LocalDate fecha, String horaI) {
		super();
		this.usuario = usuario;
		this.aula = aula;
		this.fecha = fecha;
		this.horaI=horaI;
	}

}
