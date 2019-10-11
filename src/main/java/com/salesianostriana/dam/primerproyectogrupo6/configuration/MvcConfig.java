/**
 * 
 */
package com.salesianostriana.dam.primerproyectogrupo6.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * Clase que controla las rutas y permisos
 * @author lamado
 */
@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/login");
		registry.addViewController("/acceso-denegado");
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry
			.addResourceHandler("/images/**")
			.addResourceLocations("file:files/");	
		registry
			.addResourceHandler("/webjars/**")
			.addResourceLocations("classpath:/META-INF/resources/webjars");	
		registry
			.addResourceHandler("/img/**")
			.addResourceLocations("classpath:/static/img/");	
		registry
			.addResourceHandler("/css/**")
			.addResourceLocations("classpath:/static/css/");
		registry
			.addResourceHandler("/fonts/**")
			.addResourceLocations("classpath:/static/fonts/");
		registry
			.addResourceHandler("/js/**")
			.addResourceLocations("classpath:/static/js/");
	}
	
}
