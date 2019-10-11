package com.salesianostriana.dam.primerproyectogrupo6.service;

import java.security.Principal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.salesianostriana.dam.primerproyectogrupo6.model.Usuario;
import com.salesianostriana.dam.primerproyectogrupo6.repository.UsuarioRepository;
import com.salesianostriana.dam.primerproyectogrupo6.service.base.BaseService;

/**
 * Clase que se encarga de los métodos CRUD de Usuario
 * 
 * @author Lucas Amado
 *
 */
@Service
public class UsuarioServicio extends BaseService<Usuario, Long, UsuarioRepository> {

	
	/**
	 * Busca todos los usuarios por email
	 * @param email
	 * @return todos los usuarios ordenados por email
	 */
	public Usuario buscarPorEmail(String email) {
		return repositorio.findFirstByEmail(email);
	}

	/**
	 * Comprueba si un usuario está logeado o no
	 * 
	 * @param model
	 * @param principal
	 * @return usuario que está logueado
	 */
	public Usuario buscarUsuarioLogeado(Model model, Principal principal) {

		if (principal != null) {
			String email = principal.getName();
			Usuario u = buscarPorEmail(email);
			model.addAttribute("logueado", u);

			return u;
		} else {
			return null;
		}

	}

	/**
	 * Método que pagina todos los usuarios
	 * @param pageable
	 * @return todos los usuarios
	 */
	public Page<Usuario> findAllPageable(Pageable pageable) {
		return repositorio.findAll(pageable);
	}

	/**
	 * Pagina los ususarios ordenados por nombre
	 * @param nombre
	 * @param pageable
	 * @return todos los usuarios ordenados por nombre
	 */
	public Page<Usuario> findByNombre(String nombre, Pageable pageable) {
		return repositorio.findByNombre(nombre, pageable);
	}
	
	/**
	 * Lista los usuarios por nombre
	 * @param nombre
	 * @return devuelve todos los usuarios ordenados por nombre
	 */
	public List<Usuario> findByNombre(String nombre){
		return repositorio.findByNombreContainingIgnoreCase(nombre);
	}


	/**
	 * Devuelve los nombre de los usuarios que no están validados
	 * 
	 * @author Lucas Amado
	 * @param pageable
	 * @return
	 */
	public Page<String> buscarUsuariosNoValidados(Long id,Pageable pageable) {
		return repositorio.findNoValidate(id,pageable);
	}
	
	/**
	 * Pagina los nombres de los usuarios no validados
	 * @param id
	 * @param nombre
	 * @param pageable
	 * @return todos los usuarios no validados buscados por nombre
	 */
	public Page<String> buscarUsuariosNoValidadosPorNombre(Long id, String nombre, Pageable pageable){
		return repositorio.findNoValidateByNombre(id, nombre, pageable);
	}
	
	/**
	 * Pagina los nombres de los usuarios validados
	 * @param id
	 * @param pageable
	 * @return todos los usuarios validados
	 */
	public Page<String> buscarUsuariosValidados(Long id,Pageable pageable) {
		return repositorio.findValidate(id,pageable);
	}

	/**
	 * Pagina los nombres de usuarios buscados
	 * @param id
	 * @param nombre
	 * @param pageable
	 * @return usuarios buscados
	 */
	public Page<String> buscarUsuarios(Long id, String nombre, Pageable pageable){
		return repositorio.buscarUsuarios(id, nombre, pageable);
	}

}
