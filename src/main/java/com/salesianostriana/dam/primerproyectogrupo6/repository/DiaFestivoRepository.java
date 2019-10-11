package com.salesianostriana.dam.primerproyectogrupo6.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.salesianostriana.dam.primerproyectogrupo6.model.Colegio;
import com.salesianostriana.dam.primerproyectogrupo6.model.DiaFestivo;

@Repository
public interface DiaFestivoRepository extends JpaRepository<DiaFestivo, Long>{

	@Query("select d.laborable from Dia_Festivo d where d.fecha = ?1 and d.colegio=?2")
	public Boolean encontrarFiesta(LocalDate fecha, Colegio colegio);
	
	public Page <DiaFestivo> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);
	
	public Page <DiaFestivo> findByColegio(Colegio colegio, Pageable pageable);
	
	@Query("select d from Dia_Festivo d where lower(d.nombre) like lower(concat('%',?1,'%')) and d.colegio=?2")
    public Page <DiaFestivo>  findByColegioAndNombreFestivo(String nombre, Colegio colegio,Pageable pageable);
	
	@Query("select d from Dia_Festivo d where d.fecha = ?1 and d.colegio = ?2")
	public List <DiaFestivo> encontrarRepetidos(LocalDate fecha, Colegio colegio);
}
