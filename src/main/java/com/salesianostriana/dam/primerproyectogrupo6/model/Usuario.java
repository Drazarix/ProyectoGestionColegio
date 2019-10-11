/**
 * 
 */
package com.salesianostriana.dam.primerproyectogrupo6.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
 * Clase POJO que modela la entidad Usuario
 * 
 * @author lamado
 */

@Data
@Entity
@NoArgsConstructor
public class Usuario {

	/**
	 * Id que identifica al usuario
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 * Atributos nombre y apellidos de usuario
	 */
	private String nombre, apellidos;

	/**
	 * Atributo que indica el correo electrónico del usuario
	 */
	@Column(unique = true)
	private String email;

	/**
	 * Atributo de la contraseña del usuario
	 */
	private String password;

	/**
	 * Atributo booleano para diferenciar los 3 tipos de usuarios
	 */
	private boolean admin, validate, superAdmin;

	/**
	 * Atributo colegio al que pertenece el usuario
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Colegio colegio;

	/**
	 * Atributo que indica la lista de reservas del usuario
	 */
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "usuario", fetch = FetchType.EAGER)
	private List<Reserva> listaReservas;

	/**
	 * Constructor de usuario
	 * @param nombre
	 * @param apellidos
	 * @param email
	 * @param password
	 * @param admin
	 * @param validate
	 * @param superAdmin
	 */
	public Usuario(String nombre, String apellidos, String email, String password, boolean admin, boolean validate, boolean superAdmin) {
		super();
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.email = email;
		this.password = password;
		this.admin = admin;
		this.validate = validate;
		this.superAdmin = superAdmin;
	}


}