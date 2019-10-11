package com.salesianostriana.dam.primerproyectogrupo6.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.stereotype.Service;

import com.salesianostriana.dam.primerproyectogrupo6.model.Usuario;
import com.salesianostriana.dam.primerproyectogrupo6.service.UsuarioServicio;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

	UsuarioServicio usuarioServicio;
	
	public UserDetailsServiceImpl(UsuarioServicio servicio) {
		this.usuarioServicio = servicio;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Usuario usuario = usuarioServicio.buscarPorEmail(username);
		
		UserBuilder userBuilder = null;
		
		if (usuario != null) {
			userBuilder = User.withUsername(usuario.getEmail());
			userBuilder.disabled(false);
			userBuilder.password(usuario.getPassword());
			
			if(usuario.isValidate()) {
				if(usuario.isSuperAdmin()) {
					userBuilder.authorities(new SimpleGrantedAuthority("ROLE_SUPER"));
				}else if (usuario.isAdmin()) {
					userBuilder.authorities(new SimpleGrantedAuthority("ROLE_ADMIN"));				
				} else {
					userBuilder.authorities(new SimpleGrantedAuthority("ROLE_USER"));				
				}
			}else {
				userBuilder.authorities(new SimpleGrantedAuthority("ROLE_NO_VALIDATE"));
			}
		}else {
			throw new UsernameNotFoundException("Usuario no encontrado");
		}
				
		return userBuilder.build();
		
		
	}
}
