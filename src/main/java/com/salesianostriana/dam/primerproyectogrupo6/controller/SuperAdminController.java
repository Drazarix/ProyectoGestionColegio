/**
 * 
 */
package com.salesianostriana.dam.primerproyectogrupo6.controller;

import java.security.Principal;
import java.util.Optional;

/**
 * @author Lucas Amado
 *
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.salesianostriana.dam.primerproyectogrupo6.formbean.SearchBean;
import com.salesianostriana.dam.primerproyectogrupo6.model.Aula;
import com.salesianostriana.dam.primerproyectogrupo6.model.Colegio;
import com.salesianostriana.dam.primerproyectogrupo6.model.Pager;
import com.salesianostriana.dam.primerproyectogrupo6.model.Reserva;
import com.salesianostriana.dam.primerproyectogrupo6.model.Usuario;
import com.salesianostriana.dam.primerproyectogrupo6.service.AulaServicio;
import com.salesianostriana.dam.primerproyectogrupo6.service.ColegioServicio;
import com.salesianostriana.dam.primerproyectogrupo6.service.ReservaServicio;
import com.salesianostriana.dam.primerproyectogrupo6.service.UsuarioServicio;

/**
 * @author Lucas Amado
 *
 */

@Controller
@RequestMapping("/superAdmin")
public class SuperAdminController {

	private static final int BUTTONS_TO_SHOW = 5;
	private static final int INITIAL_PAGE = 0;
	private static final int INITIAL_PAGE_SIZE = 10;
	private static final int[] PAGE_SIZES = { 5, 10, 20, 50 };

	@Autowired
	ReservaServicio reservaServicio;

	@Autowired
	ColegioServicio colegioServicio;

	@Autowired
	UsuarioServicio usuarioServicio;

	@Autowired
	AulaServicio aulaServicio;

	// COLEGIOS
	/**
	 * Página principal de superAdmin en la que se nos muestra la lista de colegios registrados.
	 * @param principal principal
	 * @param pageSize pageSize
	 * @param page page
	 * @param nombre nombre de los colegios
	 * @param model model 
	 * @return Te devuelve el html de gestion Colegios.
	 */
	@GetMapping({ "/", "/gestionColegios" })
	public String showProductsPage(Principal principal, @RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer> page, @RequestParam("nombre") Optional<String> nombre,
			Model model) {

		int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);

		int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

		String evalNombre = nombre.orElse(null);

		Page<Colegio> colegios = null;

		if (evalNombre == null) {
			colegios = colegioServicio.findAllPageable(PageRequest.of(evalPage, evalPageSize));
		} else {
			colegios = colegioServicio.findByNombreContainingIgnoreCasePageable(evalNombre,
					PageRequest.of(evalPage, evalPageSize));
		}
		Pager pager = new Pager(colegios.getTotalPages(), colegios.getNumber(), BUTTONS_TO_SHOW);

		model.addAttribute("colegios", colegios);
		model.addAttribute("selectedPageSize", evalPageSize);
		model.addAttribute("pageSizes", PAGE_SIZES);
		model.addAttribute("pager", pager);
		model.addAttribute("usuario", usuarioServicio.buscarUsuarioLogeado(model, principal));
		
		boolean[] finde = new boolean[] { true, false };
		model.addAttribute("finde", finde);
		
		return "super/gestionColegios";

	}
	
	/*COLEGIO FINDE*/
	/**
	 * Este método nos permite seleccionar en la lista si queremos que los fines de semana esten activos para un determinado colegio.
	 * @param id id del colegio seleccionado
	 * @return Te redirige a gestión colegios.
	 */
	@GetMapping("/gestionColegios/findeColegio/{id}")
	public String findeColegio(@PathVariable("id") long id) {
		Colegio finde = colegioServicio.findById(id);
		if (finde != null && finde.isFinde()==true) {
			finde.setFinde(false);
			colegioServicio.edit(finde);
		}else if( finde != null && finde.isFinde()==false) {
			finde.setFinde(true);
			colegioServicio.edit(finde);
		}
		return "redirect:/superAdmin/gestionColegios";
	}

	/* BUSCAR-COLEGIOS */

	/**
	 * Buscador de colegio
	 * @param principal principal
	 * @param searchBean searchBean
	 * @param model model 
	 * @return Nos envia al html de gestion colegios con el nombre de la búsqueda realizada.
	 */
	@PostMapping("/searchColegios")
	public String searchProducto(Principal principal, @ModelAttribute("searchForm") SearchBean searchBean,
			Model model) {
		model.addAttribute("colegios", colegioServicio.findByNombre(searchBean.getSearch()));
		model.addAttribute("usuario", usuarioServicio.buscarUsuarioLogeado(model, principal));
		return "super/gestionColegios";
	}

	/* AÑADIR-COLEGIO */
	
	/**
	 * Con este método creamos un nuevo colegio.
	 * @param principal principal
	 * @param model model 
	 * @return Nos envia al html del formulario Nuevo colegio.
	 */
	@GetMapping("/gestionColegios/nuevoColegio")
	public String showForm(Principal principal, Model model) {
		Colegio c = new Colegio();
		model.addAttribute("colegio", c);
		model.addAttribute("usuario", usuarioServicio.buscarUsuarioLogeado(model, principal));
		return "super/formNuevoColegio";
	}

	/**
	 * Este método envia los datos del colegio y los guarda.
	 * @param colegio
	 * @param model
	 * @return Redirige hacia gestion colegios.
	 */
	@PostMapping("/gestionColegios/nuevoColegio/submit")
	public String submit(@ModelAttribute("colegio") Colegio colegio, Model model) {
		colegioServicio.save(colegio);
		return "redirect:/superAdmin/gestionColegios";
	}

	/* ELIMINAR */
	/**
	 * Este método elimina el colegio seleccionado por la id. 
	 * @param id id del colegio a eliminar
	 * @return Nos envia al html de gestión colegios.
	 */
	@GetMapping("/gestionColegios/eliminarColegio/{id}")
	public String borrarColegio(@PathVariable("id") long id) {
		Colegio borrar = colegioServicio.findById(id);
		if (borrar != null) {
			colegioServicio.deleteById(id);
		}
		return "redirect:/superAdmin/gestionColegios";
	}

	/**
	 * Edita el colegio seleccionado por la id.
	 * @param id id del colegio a editar
	 * @param model model
	 * @param principal principal
	 * @return Envia al html de nuevo colegio.
	 */
	@GetMapping("/gestionColegios/editar/{id}")
	public String editarColegio(@PathVariable("id") long id, Model model, Principal principal) {
		Colegio editar = colegioServicio.findById(id);
		model.addAttribute("usuario", usuarioServicio.buscarUsuarioLogeado(model, principal));
		if (editar != null) {
			model.addAttribute("colegio", editar);
			return "super/formNuevoColegio";
		}

		return "redirect:/superAdmin/gestionColegios";
	}

	/**
	 * Envia los datos editados y los guarda.
	 * @param c colegio editado
	 * @return redirige hacia gestión colegios.
	 */
	@PostMapping("/gestionColegios/editar/submit")
	public String guardarEditadoColegio(@ModelAttribute("colegio") Colegio c) {
		colegioServicio.edit(c);
		return "redirect:/superAdmin/gestionColegios";
	}

	// RESERVAS
	/**
	 * Nos muestra las reservas de todos los usuarios de todos los colegios.
	 * @param principal principal
	 * @param pageSize pageSize
	 * @param page page 
	 * @param nombre nombre de las reservas
	 * @param model model 
	 * @return Devuelve el html de gestión reservas.
	 */
	@GetMapping("/gestionReservas")
	public String gestionarReservas(Principal principal, @RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer> page, @RequestParam("nombre") Optional<String> nombre,
			Model model) {

		int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);

		int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

		String evalNombre = nombre.orElse(null);

		Usuario logeado = usuarioServicio.buscarUsuarioLogeado(model, principal);

		Page<Reserva> reservas = null;

		if (evalNombre == null) {
			reservas = reservaServicio.findPendientes(PageRequest.of(evalPage, evalPageSize));
		} else {
			reservas = reservaServicio.findPendientesByColegioNombre(evalNombre,
					PageRequest.of(evalPage, evalPageSize));
		}

		Pager pager = new Pager(reservas.getTotalPages(), reservas.getNumber(), BUTTONS_TO_SHOW);

		model.addAttribute("listaReservas", reservas);
		model.addAttribute("usuario", logeado);
		model.addAttribute("selectedPageSize", evalPageSize);
		model.addAttribute("pageSizes", PAGE_SIZES);
		model.addAttribute("pager", pager);
		model.addAttribute("usuario", usuarioServicio.buscarUsuarioLogeado(model, principal));

		return "super/gestionReservas.html";
	}

	/**
	 * Elimina las reservas seleccionadas por la id.
	 * @param id id de la reserva buscada para borrar.
	 * @return Nos redirige hacia gestión reservas
	 */
	@GetMapping("/gestionReservas/eliminar/{id}")
	public String borrarReserva(@PathVariable("id") long id) {
		Reserva borrar = reservaServicio.findById(id);
		if (borrar != null) {
			reservaServicio.delete(borrar);
		}
		return "redirect:/superAdmin/gestionReservas";
	}

	/**
	 * Este método nos enseña el historico de todas las reservas realizadas.
	 * @param principal principal
	 * @param pageSize pageSize
	 * @param page page
	 * @param nombre nombre de las reservas
	 * @param model model
	 * @return Nos envia al html de el historial de reservas.
	 */
	@GetMapping("/gestionReservas/historico")
	public String gestionarHistoricoReservas(Principal principal, @RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer> page, @RequestParam("nombre") Optional<String> nombre,
			Model model) {

		int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);

		int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

		String evalNombre = nombre.orElse(null);

		Page<Reserva> reservas = null;

		if (evalNombre == null) {
			reservas = reservaServicio.findCaducadas(PageRequest.of(evalPage, evalPageSize));
		} else {
			reservas = reservaServicio.findCaducadasByColegioNombre(evalNombre, PageRequest.of(evalPage, evalPageSize));
		}

		Pager pager = new Pager(reservas.getTotalPages(), reservas.getNumber(), BUTTONS_TO_SHOW);

		model.addAttribute("listaReservas", reservas);
		model.addAttribute("selectedPageSize", evalPageSize);
		model.addAttribute("pageSizes", PAGE_SIZES);
		model.addAttribute("pager", pager);
		model.addAttribute("usuario", usuarioServicio.buscarUsuarioLogeado(model, principal));

		return "super/historicoReservas.html";
	}

	// AULAS
	/**
	 * Este método nos muestra todas las aulas de todos los colegios.
	 * @param pageSize pageSize
	 * @param page page
	 * @param nombre nombre de las aulas de la lista
	 * @param model model
	 * @param principal principal
	 * @return Devuelve el html de gestión Aulas
	 */
	@GetMapping("/gestionAulas")
	public String gestionarAulas(@RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer> page, @RequestParam("nombre") Optional<String> nombre, Model model,
			Principal principal) {

		int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);

		int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

		String evalNombre = nombre.orElse(null);

		Page<Aula> aulas = null;

		if (evalNombre == null) {
			aulas = aulaServicio.findByOrdenAlbafetico(PageRequest.of(evalPage, evalPageSize));
		} else {
			aulas = aulaServicio.findByOrdenAlbafeticoAndNombreColegio(evalNombre,
					PageRequest.of(evalPage, evalPageSize));
		}

		Pager pager = new Pager(aulas.getTotalPages(), aulas.getNumber(), BUTTONS_TO_SHOW);

		model.addAttribute("listaAulas", aulas);
		model.addAttribute("selectedPageSize", evalPageSize);
		model.addAttribute("pageSizes", PAGE_SIZES);
		model.addAttribute("pager", pager);
		model.addAttribute("usuario", usuarioServicio.buscarUsuarioLogeado(model, principal));

		return "super/gestionAulas.html";
	}

	/**
	 * Este método crea una nueva aula.
	 * @param model model 
	 * @param principal principal
	 * @return Devuelve el formulario de nueva aula.
	 */
	@GetMapping("/gestionAulas/nuevaAula")
	public String crarAula(Model model, Principal principal) {
		model.addAttribute("aula", new Aula());
		model.addAttribute("colegios", colegioServicio.findAll());
		model.addAttribute("usuario", usuarioServicio.buscarUsuarioLogeado(model, principal));
		return "super/formAula.html";
	}
	
	/**
	 * Envia los datos de la aula y los guarda.
	 * @param aula aula creada
	 * @param model model
	 * @return Nos redirige hacia gestión aulas.
	 */
	@PostMapping("/gestionAulas/nuevaAula/submit")
	public String submit(@ModelAttribute("Aula") Aula aula, Model model) {
		aulaServicio.save(aula);
		return "redirect:/superAdmin/gestionAulas";
	}

	/**
	 * Este método sirve para editar una aula seleccionada por la id.
	 * @param id
	 * @param model model
	 * @param principal principal
	 * @return 
	 */
	@GetMapping("/gestionAulas/editar/{id}")
	public String mostrarEdicionAula(@PathVariable("id") long id, Model model, Principal principal) {
		Aula buscada = aulaServicio.findById(id);
		model.addAttribute("usuario", usuarioServicio.buscarUsuarioLogeado(model, principal));
		if (buscada != null) {
			model.addAttribute("aula", buscada);
			return "super/formAula.html";
		}
		return "redirect:/superAmin/gestionAulas";
	}

	/**
	 * Envia los datos del aula seleccionada por los nuevos.
	 * @param a aula que se edita
	 * @return
	 */
	@PostMapping("/gestionAulas/editar/submit")
	public String guardarEditadoAula(@ModelAttribute("aula") Aula a) {
		aulaServicio.edit(a);
		return "redirect:/superAdmin/gestionAulas";
	}

	/**
	 * Borra el aula seleccionada por id.
	 * 	@GetMapping("/gestionAulas/borrar/{id}")
	 * @param id para saber el aula que borrar.
	 * @param model model
	 * @return Nos redirige a gestion aulas.
	 */
	public String borrarAula(@PathVariable("id") long id, Model model) {
		Aula buscada = aulaServicio.findById(id);
		if(buscada!=null) {
			aulaServicio.delete(buscada);
		}
		return "redirect:/superAdmin/gestionAulas";
	}

}
