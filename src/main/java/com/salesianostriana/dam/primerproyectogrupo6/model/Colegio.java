/**
 * 
 */
package com.salesianostriana.dam.primerproyectogrupo6.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Clase POJO que modela la entidad Colegio
 * 
 * @author lamado
 */

@Data
@Entity
@NoArgsConstructor
public class Colegio {
	
	/**
	 * Atributo id para identificarlo los colegios
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	/**
	 * Atributo que nos indica el nombre del colegio
	 */
	@Column(unique = true)
	private String nombre;
	
	/**
	 * Atributos que nos indican la provincia y la localidad del colegio
	 */
	private String provincia, localidad;
	
	/**
	 * Atributo de lista de usuarios que pertenecen al colegio
	 */
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "colegio")
	private List <Usuario> listaUsuarios;
	
	/**
	 * Atributo que nos indica las aulas que pertenecen al colegio
	 */
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "colegio")
	private List <Aula> listaAulas;
	
	
	/**
	 * Atributo de la lista de días festivos pertenecientes al colegio
	 */
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@OneToMany(mappedBy = "colegio")
	private List <DiaFestivo> listaDiaFestivo;
	
	/**
	 * Atributo booleano que indica si se pueden reservar los fines de semanas del colegio 
	 */
	private boolean finde;

	/**
	 * Constructor Colegio
	 * @param nombre
	 * @param provincia
	 * @param localidad
	 */
	public Colegio(String nombre, String provincia, String localidad) {
		super();
		this.nombre = nombre;
		this.provincia = provincia;
		this.localidad = localidad;
	}

}
