package com.salesianostriana.dam.primerproyectogrupo6.controller;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.salesianostriana.dam.primerproyectogrupo6.model.Aula;
import com.salesianostriana.dam.primerproyectogrupo6.model.Pager;
import com.salesianostriana.dam.primerproyectogrupo6.model.Usuario;
import com.salesianostriana.dam.primerproyectogrupo6.service.AulaServicio;
import com.salesianostriana.dam.primerproyectogrupo6.service.UsuarioServicio;

@Controller
public class UsuarioController {

	private static final int BUTTONS_TO_SHOW = 5;
	private static final int INITIAL_PAGE = 0;
	private static final int INITIAL_PAGE_SIZE = 10;
	private static final int[] PAGE_SIZES = { 5, 10, 20, 50 };

	@Autowired
	private UsuarioServicio usuarioService;
	
	@Autowired
	private AulaServicio aulaService;

	/**
	 * Página de inicio de usuario que nos dirige a las reservas del usuario
	 * @param model model
	 * @param principal principal
	 * @return Nos redirige a la página de reservas
	 */
	@GetMapping({ "/usuario/" })
	public String inicio(Model model, Principal principal) {
		model.addAttribute("usuario", usuarioService.buscarUsuarioLogeado(model, principal));
		return "redirect:/misReservas/";
	}
	
	/**
	 * Nos muestra la lista de aulas del colegio al cual pertenece el usuario.
	 * @param pageSize pageSize
	 * @param nombre nombre de las aulas
	 * @param page page
	 * @param model model
	 * @param principal principal
	 * @return Nos envia al html de la lista de Aulas
	 */
	@GetMapping("/listaAulas")
	public String gestionarAulas(@RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("nombre") Optional<String> nombre, @RequestParam("page") Optional<Integer> page, Model model,
			Principal principal) {

		model.addAttribute("logeado", usuarioService.buscarUsuarioLogeado(model, principal));
		int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);

		int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

		String evalNombre = nombre.orElse(null);

		Usuario logeado = usuarioService.buscarUsuarioLogeado(model, principal);

		Page<Aula> aulas = null;

		if (evalNombre == null) {
			aulas = aulaService.findByColegio(logeado.getColegio().getId(), PageRequest.of(evalPage, evalPageSize));
		} else {
			aulas = aulaService.findByColegioAndNombreAula(evalNombre, logeado.getColegio().getId(),
					PageRequest.of(evalPage, evalPageSize));
		}

		Pager pager = new Pager(aulas.getTotalPages(), aulas.getNumber(), BUTTONS_TO_SHOW);

		model.addAttribute("listaAulas", aulas);
		model.addAttribute("selectedPageSize", evalPageSize);
		model.addAttribute("pageSizes", PAGE_SIZES);
		model.addAttribute("pager", pager);
		model.addAttribute("usuario", usuarioService.buscarUsuarioLogeado(model, principal));

		return "usuario/gestionarAulas";
	}

}
