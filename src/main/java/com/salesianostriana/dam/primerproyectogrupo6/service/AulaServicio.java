/**
 * 
 */
package com.salesianostriana.dam.primerproyectogrupo6.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.salesianostriana.dam.primerproyectogrupo6.model.Aula;
import com.salesianostriana.dam.primerproyectogrupo6.repository.AulaRepository;
import com.salesianostriana.dam.primerproyectogrupo6.service.base.BaseService;

/**
 * Clase que se encarga de los CRUD de Aula
 * 
 * @author Lucas Amado
 *
 */

@Service
public class AulaServicio extends BaseService<Aula, Long, AulaRepository> {

	/**
	 * Método que pagina las aulas, buscando los colegios 
	 * @param id
	 * @param pageable
	 * @return lista de los colegios buscados por id
	 */
	public Page<Aula> findByColegio(Long id, Pageable pageable) {
		return repositorio.findByColegio(id, pageable);
	}

	/**
	 * Método que  pagina las aulas, buscando los colegios y los nombres de las aulas
	 * @param nombre
	 * @param id
	 * @param pageable
	 * @return lista de los colegios y sus aulas
	 */
	public Page<Aula> findByColegioAndNombreAula(String nombre, Long id, Pageable pageable) {
		return repositorio.findByColegioAndNombreAula(nombre, id, pageable);
	}

	/**
	 * Método que lista las aulas que no se han reservado
	 * @param id
	 * @param fecha
	 * @param hora
	 * @return lista de aulas 
	 */
	public List<Aula> buscarNoReservadosHora(Long id, LocalDate fecha, String hora) {
		return repositorio.findNoReservadosInHora(id, fecha, hora);
	}
	
	/**
	 * Método que pagina las aulas por orden alfabético
	 * @param pageable
	 * @return lista de las aulas
	 */
	public Page<Aula> findByOrdenAlbafetico(Pageable pageable){
		return repositorio.findByOrdenAlbafetico(pageable);
	}
	
	/**
	 * Método que pagina las aulas por orden alfabético y por el nombre del colegio
	 * @param nombre
	 * @param pageable
	 * @return lista de las aulas
	 */
	public Page <Aula> findByOrdenAlbafeticoAndNombreColegio(String nombre, Pageable pageable){
		return repositorio.findByOrdenAlbafeticoAndNombreColegio(nombre, pageable);
	}

}
