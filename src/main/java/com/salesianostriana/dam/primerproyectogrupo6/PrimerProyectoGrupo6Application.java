package com.salesianostriana.dam.primerproyectogrupo6;
/**
 * Esta clase permite encriptar las contraseñas de los usuarios metidos en la base de datos. También sirve para arrancar el programa
 */
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.salesianostriana.dam.primerproyectogrupo6.model.Usuario;
import com.salesianostriana.dam.primerproyectogrupo6.service.UsuarioServicio;

@SpringBootApplication
public class PrimerProyectoGrupo6Application {

	public static void main(String[] args) {
		SpringApplication.run(PrimerProyectoGrupo6Application.class, args);
	}

	@Bean
	public CommandLineRunner init(UsuarioServicio servicio, BCryptPasswordEncoder passwordEncoder) {
		return args -> {
						
			for(Usuario u : servicio.findAll()) {
				u.setPassword(passwordEncoder.encode(u.getPassword()));
				servicio.save(u);
			}
			
		};
	}
	
}
