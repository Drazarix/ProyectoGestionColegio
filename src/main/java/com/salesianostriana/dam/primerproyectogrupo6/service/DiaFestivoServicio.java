package com.salesianostriana.dam.primerproyectogrupo6.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.salesianostriana.dam.primerproyectogrupo6.model.Colegio;
import com.salesianostriana.dam.primerproyectogrupo6.model.DiaFestivo;
import com.salesianostriana.dam.primerproyectogrupo6.repository.DiaFestivoRepository;
import com.salesianostriana.dam.primerproyectogrupo6.service.base.BaseService;

@Service
public class DiaFestivoServicio extends BaseService<DiaFestivo, Long, DiaFestivoRepository>{

	/**
	 * Método que lista todos los dias festivos
	 * @return Todos los dias festivos
	 */
	public Page<DiaFestivo> findDiasFestivosByColegio(Colegio colegio, Pageable pageable){      //SERVICIO
        return repositorio.findByColegio(colegio, pageable);
    }
	
	public List<DiaFestivo> findAllProducts() {
		return repositorio.findAll();
	}

	/**
	 * Método que pagina los dias festivos 
	 * @param pageable
	 * @return Todos los dias festivos
	 */
	public Page<DiaFestivo> findAllPageable(Pageable pageable) {
		return repositorio.findAll(pageable);
	}

	/**
	 * Método que pagina los dias festivos buscados por nombre ignorando si son mayusculas o minúsculas
	 * @param nombre
	 * @param pageable
	 * @return Los dias festivos ordenados por nombre
	 */
	public Page<DiaFestivo> findByNombreContainingIgnoreCasePageable(String nombre, Pageable pageable) {
		return repositorio.findByNombreContainingIgnoreCase(nombre, pageable);
	}
	
	/**
	 * Método que indica si el día festivo es laboral o no.
	 * @param fecha
	 * @return
	 */
	public boolean findDiaFestivo(LocalDate fecha, Colegio colegio) {
		
		if (repositorio.encontrarFiesta(fecha, colegio) == null || repositorio.encontrarFiesta(fecha, colegio) == true) {
			return true;
		}else {
			return false;
		}
		
	}
	
	public Page<DiaFestivo> findDiasFestivosByColegioAndNombreFestivo( String nombre, Colegio colegio,Pageable pageable){
        return repositorio.findByColegioAndNombreFestivo( nombre, colegio,pageable);
    }
	
	public boolean comprobarRepetidos(LocalDate fecha, Colegio colegio) {
		
		if (repositorio.encontrarRepetidos(fecha, colegio).size() == 0)  {
			return false;
		} else {
			return true;
		}
		
	}
}
