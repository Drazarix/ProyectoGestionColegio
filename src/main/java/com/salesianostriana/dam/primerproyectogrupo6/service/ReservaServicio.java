/**
 * 
 */
package com.salesianostriana.dam.primerproyectogrupo6.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.salesianostriana.dam.primerproyectogrupo6.model.Reserva;
import com.salesianostriana.dam.primerproyectogrupo6.model.Usuario;
import com.salesianostriana.dam.primerproyectogrupo6.repository.ReservaRepository;
import com.salesianostriana.dam.primerproyectogrupo6.service.base.BaseService;

import lombok.Getter;

/**
 * Clase que contiene los servicios de las reservas
 * 
 * @author Lucas Amado
 *
 */

@Service
public class ReservaServicio extends BaseService<Reserva, Long, ReservaRepository> {
	/**
	 * Lista con el rango de horas disponibles para la reserva de las aulas
	 */
	
	@Getter
	private List<String> rangoHoras = new ArrayList<String>(
			Arrays.asList("8:00", "9:00", "10:00", "11:30", "12:30", "13:30"));
	
	//PERSONALES
	/**
	 * Pagina las reservas pendientes dependiendo del usuario
	 * @param logeado
	 * @param pageable
	 * @return reservas pendientes por usuario
	 */
	public Page <Reserva> findPendientesByUsuario(Usuario logeado, Pageable pageable){
		return repositorio.findPendientesByUsuario(logeado, pageable);
	}
	
	/**
	 * Pagina las reservas pendientes por usuario y nombre de aula
	 * @param logeado
	 * @param nombre
	 * @param pageable
	 * @return reservas pendientes por usuario y nombre de aula
	 */
	public Page <Reserva> findPendientesByUsuarioAndNombreAula(Usuario logeado, String nombre,Pageable pageable){
		return repositorio.findPendientesByUsuarioAndAulaNombre(logeado, nombre, pageable);
	}
	
	/**
	 * Pagina reservas caducadas por usuarios
	 * @param logeado
	 * @param pageable
	 * @return reservas caducadas por ususarios
	 */
	public Page <Reserva> findCaducadasByUsuario(Usuario logeado, Pageable pageable){
		return repositorio.findCaducadasByUsuario(logeado, pageable);
	}
	
	/**
	 * Pagina reservas caducadas por usuario y nombre de aula
	 * @param logeado
	 * @param nombre
	 * @param pageable
	 * @return reservas caducadas por usuario y nombre de aula
	 */
	public Page <Reserva> findCaducadasByUsuarioAndNombreAula(Usuario logeado, String nombre, Pageable pageable){
		return repositorio.findCaducadasByUsuarioAndAulaNombre(logeado, nombre, pageable);
	}
	
	//COLEGIO
	
	/**
	 * Pagina las reservas pendientes por colegio
	 * @param id
	 * @param pageable
	 * @return reservas pendientes por colegio
	 */
	public Page <Reserva> findPendientesByColegio(Long id, Pageable pageable){
		return repositorio.findPendientesByColegio(id, pageable);
	}
	
	/**
	 * Pagina reservas pendientes por colegio y nombre de usuario
	 * @param id
	 * @param nombre
	 * @param pageable
	 * @return reservas pendientes por colegio y nombre de usuario
	 */
	public Page <Reserva> findPendientesByColegioAndNombreUsuario(Long id, String nombre, Pageable pageable){
		return repositorio.findPendientesByColegioAndNombreUsuario(id, nombre,pageable);
	}
	
	/**
	 * Pagina reservas caducadas por colegio
	 * @param id
	 * @param pageable
	 * @return todas las reservas caducadas por colegio
	 */
	public Page <Reserva> findCaducadasByColegio(Long id, Pageable pageable){
		return repositorio.findCaducadasByColegio(id, pageable);
	}
	
	/**
	 * Pagina reservas caducadas por colegio y nombre de usuario
	 * @param id
	 * @param nombre
	 * @param pageable
	 * @return reservas caducadas por colegio y nombre de usuario
	 */
	public Page <Reserva> findCaducadasByColegioAndNombreUsuario(Long id, String nombre, Pageable pageable){
		return repositorio.findCaducadasByColegioAndNombreUsuario(id, nombre,pageable);
	}
	
	
	//SUPERADMIN
	
	/**
	 * Pagina reservas pendientes 
	 * @param pageable
	 * @return reservas pendientes
	 */
	public Page <Reserva> findPendientes(Pageable pageable){
		return repositorio.findPendientes(pageable);
	}
	
	/**
	 * Pagina reservas pendientes por nombre de colegio
	 * @param nombre
	 * @param pageable
	 * @return reservas pendientes por nombre de colegio
	 */
	public Page <Reserva> findPendientesByColegioNombre(String nombre, Pageable pageable){
		return repositorio.findPendientesByColegioNombre(nombre, pageable);
	}
	
	/**
	 * Pagina rerservas caducadas
	 * @param pageable
	 * @return reservas caducadas
	 */
	public Page <Reserva> findCaducadas(Pageable pageable){
		return repositorio.findCaducadas(pageable);
	}
	
	/**
	 * Lista las reservas realizadas en dias festivos
	 * @param fecha
	 * @return reservas de dias festivos
	 */
	public List<Reserva> findReservasEnDiasFestivos(LocalDate fecha) {
		return repositorio.findReservasEnFestivos(fecha);
	}
	
	/**
	 * Pagina reservas caducadas por nombre de colegio
	 * @param nombre
	 * @param pageable
	 * @return reservas caducadas por nombre de colegio
	 */
	public Page<Reserva> findCaducadasByColegioNombre(String nombre, Pageable pageable) {
		return repositorio.findCaducadasByColegioNombre(nombre, pageable);
	}

}
