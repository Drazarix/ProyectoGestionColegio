/**
 * 
 */
package com.salesianostriana.dam.primerproyectogrupo6.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Clase POJO que modela la entidad Aula
 * 
 * @author lamado
 */

@Data
@Entity
@NoArgsConstructor
public class Aula {
	
	/**
	 * id para identificarlo las aulas
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	/**
	 * Este atributo indica el nombre del aula
	 */
	private String nombre;
	
	/**
	 * Este atributo indica la capacidad máxima del aula
	 */
	private int capacidadMax;
	
	/**
	 * Este atributo nos indica a que colegio pertenece el aula
	 */
	@ManyToOne
	private Colegio colegio;
	
	/**
	 * lista de reservas asocidas a esa aula
	 */
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "aula")
	private List <Reserva> listaReservas;

	/**
	 * Constructor de aula
	 * @param nombre
	 * @param capacidadMax
	 * @param colegio
	 */
	public Aula(String nombre, int capacidadMax, Colegio colegio) {
		super();
		this.nombre = nombre;
		this.capacidadMax = capacidadMax;
	}
	
}
