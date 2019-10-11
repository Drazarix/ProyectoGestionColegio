/**
 * 
 */
package com.salesianostriana.dam.primerproyectogrupo6.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.salesianostriana.dam.primerproyectogrupo6.model.Usuario;

/**
 * Este epositorio conecta el servicio de un usuario con la base de datos
 * 
 * @author Lucas Amado
 *
 */

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	/**
	 * Permite encontrar un usaurio concreto a partir de su email
	 * 
	 * @param email email del usuario
	 * @return usuario asociado a ese email
	 */
	public Usuario findFirstByEmail(String email);

	/**
	 * Permite encotrar los uaurios cuyo nombre o apellidos contengan el nombre
	 * buscado
	 * 
	 * @param nombre nombre buscado
	 * @param pageable
	 * @return usuarios con ese nombre buscado
	 */
	@Query("select u from Usuario u where lower(u.nombre) like lower(concat('%',?1,'%')) or lower(u.apellidos) like lower(concat('%',?1,'%'))")
	public Page<Usuario> findByNombre(String nombre, Pageable pageable);
	
	
	/**
	 * Page de nombres de usuarios que aún no han sido validados
	 * 
	 * @author Lucas Amado
	 * @param pageable
	 * @return page de nombres de usuarios sin validar
	 */
	@Query("select u from Usuario u where u.validate != true and u.colegio.id = ?1")
	public Page<String> findNoValidate(Long id, Pageable pageable);
	
	/**
	 * Page de nombre de usuarios sin validar
	 * 
	 * @param id id colegio
	 * @param nombre nombre usuario
	 * @param pageable pageable
	 * @return
	 */
	@Query("select u from Usuario u where u.validate != true and u.colegio.id = ?1 and lower(u.nombre) like lower(concat('%',?2,'%'))")
	public Page<String> findNoValidateByNombre(Long id, String nombre, Pageable pageable);
	
	/**
	 * Busca una lista de nombres de usuarios validados
	 * 
	 * @param id id colegio
	 * @param pageable pageable
	 * @return nombre de usuarios validados de un colegio
	 */
	@Query("select u from Usuario u where u.validate != false and u.colegio.id = ?1 and u.admin != true")
	public Page<String> findValidate(Long id, Pageable pageable);
	
	/**
	 * Busca una lista de usuarios por su nombre
	 * 
	 * @param nombre nombre usuario
	 * @return lista de usuarios buacdos por su nombre
	 */
	public List<Usuario> findByNombreContainingIgnoreCase(String nombre);
	
	/**
	 * Page de nombres de suarios sin validar de un colegio en concreto. Buscados por su nombre 
	 * 
	 * @param id id colegio
	 * @param nombre nombre usuario
	 * @param pageable pageable
	 * @return nombres de usuarios buscados por su nombre
	 */
	@Query("select u from Usuario u where u.validate != false and u.colegio.id = ?1 and u.admin != true and lower(u.nombre) like lower(concat('%',?2,'%')) or lower(u.apellidos) like lower(concat('%',?2,'%'))")
	public Page<String> buscarUsuarios(Long id, String nombre, Pageable pageable);

}
