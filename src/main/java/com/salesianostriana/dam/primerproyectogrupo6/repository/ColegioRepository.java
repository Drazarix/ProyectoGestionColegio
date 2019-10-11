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
import org.springframework.stereotype.Repository;

import com.salesianostriana.dam.primerproyectogrupo6.model.Colegio;

/**
 * @author Lucas Amado
 *
 */
@Repository
public interface ColegioRepository extends JpaRepository <Colegio, Long>{
	
	/**
	 * Page de colegios por su nombre
	 * 
	 * @param nombre nombre
	 * @param pageable
	 * @return colegios con el nombre buscado
	 */
	public Page <Colegio> findByNombre(String nombre, Pageable pageable);
	
	/**
	 * Lista de colegios buscados por nombre
	 * 
	 * @param nombre nombre colegio
	 * @return lista de colegios por el nombre buscado
	 */
	public List<Colegio> findByNombreContainingIgnoreCase(String nombre);	
	
	/**
	 * Page de colegios buscados
	 * 
	 * @param nombre nombre colegio
	 * @param pageable pageable
	 * @return page de colegios buscados por nombre
	 */
	public Page <Colegio> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);

	/**
	 * Coge el día de la fecha en la que se quiere hacer una reserva, y averígua en que día cae
	 * 
	 * @param fecha dia de la semana en que se quiere hacer la reserva
	 * @return int del día de la semana
	 */
	@Query(value = "select DAYOFWEEK(?1)", nativeQuery = true)
	public int findDiaDeSemana(LocalDate fecha);
}
