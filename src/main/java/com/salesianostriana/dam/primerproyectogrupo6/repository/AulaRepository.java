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

import com.salesianostriana.dam.primerproyectogrupo6.model.Aula;

/**
 * Este repositorio conecta el servicio de aula con la base de datos
 * 
 * @author Lucas Amado
 *
 */
public interface AulaRepository extends JpaRepository<Aula, Long> {

	/**
	 * Page de aulas pertenecientes a un colegio y ordenadas por nombre
	 * 
	 * @param id       id colegio
	 * @param pageable paginación
	 * @return aulas pageable encontradas por el id del colegio
	 */
	@Query("select a from Aula a where a.colegio.id=?1 order by a.nombre desc")
	public Page<Aula> findByColegio(Long id, Pageable pageable);

	/**
	 * Page de aulas pertenecientes a un colegio y buscadas por el nombre de un aula
	 * 
	 * @param nombre   nombre aula
	 * @param id       id colegio
	 * @param pageable pageable
	 * @return aulas pageable encontradas por colegio y el nombre del aula
	 */
	@Query("select a from Aula a where a.colegio.id=?2 and lower(a.nombre) like lower(concat('%',?1,'%')) order by a.nombre asc")
	public Page<Aula> findByColegioAndNombreAula(String nombre, Long id, Pageable pageable);

	/**
	 * Lista de aulas de un colegio que no están reservas a una hora y fecha
	 * concreta
	 * 
	 * @param id    id colegio
	 * @param fecha fecha reserva
	 * @param hora  hora reserva
	 * @return lista de aulas de un colegio concreto que no estén ocupadas a la hora
	 *         y fecha pasadas
	 */
	@Query("select a from Aula a where a.colegio.id=?1 and a not in (select r.aula from Reserva r where r.fecha=?2 and r.horaI=?3) order by a.nombre asc")
	public List<Aula> findNoReservadosInHora(Long id, LocalDate fecha, String hora);

	/**
	 * Page de aulas de todos los colegios ordenadas por orden alfabético
	 * 
	 * @param pageable pageable
	 * @return page de aulas de los colegios
	 */
	@Query("select a from Aula a order by a.nombre asc")
	public Page<Aula> findByOrdenAlbafetico(Pageable pageable);

	/**
	 * Page de aulas de todos los colegios, buscando por el nombre del colegio y
	 * ordenadado alfabéticamente
	 * 
	 * @param nombre   nombre aula
	 * @param pageable pageable
	 * @return
	 */
	@Query("select a from Aula a where lower(a.nombre) like lower(concat('%',?1,'%')) order by a.nombre asc")
	public Page<Aula> findByOrdenAlbafeticoAndNombreColegio(String nombre, Pageable pageable);

}
