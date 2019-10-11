/**
 * 
 */
package com.salesianostriana.dam.primerproyectogrupo6.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.salesianostriana.dam.primerproyectogrupo6.model.Reserva;
import com.salesianostriana.dam.primerproyectogrupo6.model.Usuario;

/**
 * Este repositorio sirve para conectar el servicio de reserva de la base de datos
 * @author Lucas Amado
 *
 */
public interface ReservaRepository extends JpaRepository<Reserva, Long>{

	//PERSONALES
	
	/**
	 * Permite buscar las reservas realizadas por un usuario concreto
	 * 
	 * @param logeado usuario
	 * @param pageable
	 * @return reservas hechas por el usuario sin caducar ordenadas por fecha en orden decreciente
	 * @author Lucas Amado
	 */
	@Query("select r from Reserva r where r.usuario=?1 and r.fecha>=CURRENT_DATE order by fecha desc")
	public Page <Reserva> findPendientesByUsuario(Usuario logeado, Pageable pageable);
	
	/**
	 * Buscar reservas de un usuario concreto, sin caducar y coincidan con el nombre del aula buscada
	 * 
	 * @param logeado usuario
	 * @param nombre nombre aula
	 * @param pageable pageable
	 * @return reservas sin caducar de un usuario concreto. Ordenadas por fecha en orden decreciente
	 */
	@Query("select r from Reserva r where r.usuario=?1 and lower(r.aula.nombre) like lower(concat('%',?2,'%')) and r.fecha>=CURRENT_DATE order by fecha desc")
	public Page <Reserva> findPendientesByUsuarioAndAulaNombre(Usuario logeado, String nombre, Pageable pageable);
	
	/**
	 * Reservas historicas de un usuario concreto
	 * 
	 * @param logeado usuario logeado
	 * @param pageable pageable
	 * @return reservas caducadas de un usuario
	 */
	@Query("select r from Reserva r where r.usuario=?1 and r.fecha<CURRENT_DATE order by fecha asc")
	public Page <Reserva> findCaducadasByUsuario(Usuario logeado, Pageable pageable);
	
	
	/**
	 * Reservas caducadas de un usuario concreto, buscada por el nombre del aula
	 * 
	 * @param logeado usuario logeado
	 * @param nombre nombre del aula
	 * @param pageable pageable
	 * @return reservas caducadas de un usuario en concreto, buscando por el nombre de un aula
	 */
	@Query("select r from Reserva r where r.usuario=?1 and lower(r.aula.nombre) like lower(concat('%',?2,'%')) and r.fecha<CURRENT_DATE order by fecha asc")
	public Page <Reserva> findCaducadasByUsuarioAndAulaNombre(Usuario logeado, String nombre, Pageable pageable);
	
	
	
	//COLEGIO

	/**
	 * Reservas pendientes de un colegio
	 * 
	 * @param id id del colegio
	 * @param pageable pageable
	 * @return todas las reservas hechas en un colegio que están pendientes
	 */
	@Query("select r from Reserva r where r.usuario.colegio.id = ?1 and r.fecha>=CURRENT_DATE order by r.fecha asc")
	public Page <Reserva> findPendientesByColegio(Long id, Pageable pageable);
	
	/**
	 * Reservas pendientes de uno o varios usuarios, (buscado por nombre) de un colegio concreto.
	 * Evita que aparezcan las reservas de otros aulumnos de otro colegio
	 * 
	 * @param id id del colegio
	 * @param nombre nombre del aula
	 * @param pageable pageable
	 * @return reservas de los alumnos bucados por nombre de un colegio concreto
	 */
	@Query("select r from Reserva r where r.usuario.colegio.id = ?1 and lower(r.usuario.nombre) like lower(concat('%',?2,'%')) and r.fecha>=CURRENT_DATE order by r.fecha asc")
	public Page <Reserva> findPendientesByColegioAndNombreUsuario(Long id, String nombre, Pageable pageable);
	
	/**
	 * @param id id del colegio
	 * @param pageable pageable 
	 * @return reservas caducadas de un colegio buscado por nombre
	 */
	@Query ("select r from Reserva r where r.fecha < CURRENT_DATE and r.usuario.colegio.id=?1 order by r.fecha asc")
	public Page <Reserva> findCaducadasByColegio(Long id, Pageable pageable);

	/**
	 * 
	 * @param id id del colegio
	 * @param nombre nombre del usuario
	 * @param pageable pageable
	 * @return reservas caducadas de un coelgio, buscando el nombre de un aumno en concreto
	 */
	@Query("select r from Reserva r where r.usuario.colegio.id = ?1 and lower(r.usuario.nombre) like lower(concat('%',?2,'%')) order by r.fecha asc")
	public Page <Reserva> findCaducadasByColegioAndNombreUsuario(Long id, String nombre, Pageable pageable);
	
	
	//SUPERADMIN
	/**
	 * Devuelve todas las reservas pendientes, sin especificar el colegio 
	 * 
	 * @param pageable pageable
	 * @return reservas pendientes de todos los colegios
	 */
	@Query ("select r from Reserva r where r.fecha >= CURRENT_DATE order by r.fecha asc")
	public Page <Reserva> findPendientes(Pageable pageable);
	
	/**
	 * Todas las reservas pendientes, buscadas por el nombre del colegio
	 * 
	 * @param id id del colegio
	 * @param pageable pageable
	 * @return todas las reservas pendientes del colegio buscado
	 */
	@Query ("select r from Reserva r where r.fecha >= CURRENT_DATE and lower(r.usuario.colegio.nombre) like lower(concat('%',?1,'%')) order by r.fecha asc")
	public Page <Reserva> findPendientesByColegioNombre(String nombre, Pageable pageable);
	
	/**
	 * 
	 * @param pageable pageable
	 * @return todas las reservas caducadas
	 */
	@Query ("select r from Reserva r where r.fecha<CURRENT_DATE order by r.fecha desc")
	public Page <Reserva> findCaducadas(Pageable pageable);
	
	/**
	 * Reservas caducadas, sin especificar el colegio
	 * 
	 * @param pageable pageable
	 * @return
	 */
	@Query ("select r from Reserva r where r.fecha < CURRENT_DATE and lower(r.usuario.colegio.nombre) like lower(concat('%',?1,'%')) order by r.fecha desc")
	public Page <Reserva> findCaducadasByColegioNombre(String nombre, Pageable pageable);
	
	/**
	 * Selecciona las reservas con las fechas que le mandas
	 * 
	 * @param fecha
	 * @return
	 */
	@Query(value = "select r from Reserva r where r.fecha=?1")
	public List <Reserva> findReservasEnFestivos(LocalDate fecha);
	
}
