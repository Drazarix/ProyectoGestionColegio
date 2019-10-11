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

@Data
@Entity(name="Dia_Festivo")
@NoArgsConstructor
public class DiaFestivo {

	/**
	 * Atributo id para identificar los dias festivos
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	/**
	 * Atributo nombre de día festivo
	 */
	private String nombre;
	
	/**
	 * Atributo para saber la fecha del día festivo
	 */
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private LocalDate fecha;
	
	/**
	 * Atributo colegio al que pertenecen los festivos
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Colegio colegio;
	
	/**
	 * Atributo booleano para saber si es un día laborable
	 */
	private Boolean laborable;
}
