/**
 * 
 */
package com.salesianostriana.dam.primerproyectogrupo6.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.salesianostriana.dam.primerproyectogrupo6.model.Colegio;
import com.salesianostriana.dam.primerproyectogrupo6.repository.ColegioRepository;
import com.salesianostriana.dam.primerproyectogrupo6.service.base.BaseService;

/**
 * Clase que contiene los servicios de los colegios
 * 
 * @author Lucas Amado
 *
 */

@Service
public class ColegioServicio extends BaseService<Colegio, Long, ColegioRepository> {

	/**
	 * Lista todos los colegios 
	 * @return todos los colegios
	 */

	public List<Colegio> findAllProducts() {
		return repositorio.findAll();
	}

	/**
	 * Pagina todos los colegios
	 * @param pageable
	 * @return todos los colegios
	 */
	public Page<Colegio> findAllPageable(Pageable pageable) {
		return repositorio.findAll(pageable);
	}

	/**
	 * Pagina los colegios buscados por nombre
	 * @param nombre
	 * @param pageable
	 * @return todos los colegios buscados por nombre
	 */
	public Page<Colegio> findByNombreContainingIgnoreCasePageable(String nombre, Pageable pageable) {
		return repositorio.findByNombreContainingIgnoreCase(nombre, pageable);
	}
	
	/**
	 * Comprueba que la fecha es fin de semana y en caso de que este en false bloquea los dias
	 * @param fecha
	 * @param permitido
	 * @return
	 */
	public List <Colegio> findByNombre(String nombre){
		return repositorio.findByNombreContainingIgnoreCase(nombre);
	}
	
	public boolean findDiaDeSemana(LocalDate fecha, boolean permitido) {
		
		if (permitido == false && repositorio.findDiaDeSemana(fecha) == 1 || permitido == false && repositorio.findDiaDeSemana(fecha) == 7) {
			return false;
		} else {
			return true;
		}
		
	}

}
