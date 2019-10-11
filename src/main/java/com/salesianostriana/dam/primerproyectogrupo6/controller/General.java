/**
 * 
 */
package com.salesianostriana.dam.primerproyectogrupo6.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.salesianostriana.dam.primerproyectogrupo6.exception.ExcepcionEmailRepetido;
import com.salesianostriana.dam.primerproyectogrupo6.model.Pager;
import com.salesianostriana.dam.primerproyectogrupo6.model.Reserva;
import com.salesianostriana.dam.primerproyectogrupo6.model.Usuario;
import com.salesianostriana.dam.primerproyectogrupo6.service.AulaServicio;
import com.salesianostriana.dam.primerproyectogrupo6.service.ColegioServicio;
import com.salesianostriana.dam.primerproyectogrupo6.service.DiaFestivoServicio;
import com.salesianostriana.dam.primerproyectogrupo6.service.ReservaServicio;
import com.salesianostriana.dam.primerproyectogrupo6.service.UsuarioServicio;

/**
 * Permite gestionar las rutas generales para los usuarios
 * 
 * @author Victor
 *
 */
@Controller
public class General {

	private static final int BUTTONS_TO_SHOW = 5;
	private static final int INITIAL_PAGE = 0;
	private static final int INITIAL_PAGE_SIZE = 10;
	private static final int[] PAGE_SIZES = { 5, 10, 20, 50 };

	@Autowired
	private UsuarioServicio usuarioService;

	@Autowired
	private ColegioServicio colegioService;

	@Autowired
	private ReservaServicio reservaService;

	@Autowired
	private AulaServicio aulaService;
	
	@Autowired
	private DiaFestivoServicio diaFestivoService;
	
	/**
	 * Permite mostrar el formulario para que se registre un usuario nuevo
	 * 
	 * @param model model
	 * @return html de nuevo usuario
	 */
	@GetMapping("/registro")
	public String showForm(Model model) {
		model.addAttribute("usuario", new Usuario());
		model.addAttribute("colegios", colegioService.findAll());

		return "usuario/registro";
	}

	/**
	 * Permite guardar el usuario nuevo en la base de datos
	 * 
	 * @param model model model
	 * @param usuario usuario nuevo
	 * @return html de login
	 */
	@PostMapping("/registro/submit")
	public String procesarFormulario(Model model, @ModelAttribute("usuario") Usuario usuario) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

		for (Usuario user : usuarioService.findAll()) {
			if (user.getEmail().equalsIgnoreCase(usuario.getEmail())) {
				throw new ExcepcionEmailRepetido("mismoUsername");
			}
		}

		usuarioService.save(usuario);

		return "redirect:/login";
	}
	
	/**
	 * Permite que ningún usuario se meta en rutas de roles que no le corresponden
	 * 
	 * @param model model
	 * @param principal principal
	 * @return html de acceso denegado
	 */
	@GetMapping("/acceso-denegado")
	public String restringirAcceso(Model model, Principal principal) {
		model.addAttribute("usuario", usuarioService.buscarUsuarioLogeado(model, principal));
		return "acceso-denegado.html";
	}

	/**
	 * Muestra los datos necesarios para crear una nueva reserva
	 * 
	 * @param model model
	 * @param principal principal
	 * @return html para crear una nueva reserva
	 */
	@GetMapping("/nuevaReserva")
	public String realizarReserva(Model model, Principal principal) {
		if (usuarioService.buscarUsuarioLogeado(model, principal).isSuperAdmin()) {
			return "redirect:/superAdmin/";
		}
		model.addAttribute("reserva", new Reserva());
		model.addAttribute("hoy", LocalDate.now());
		model.addAttribute("listaHorarios", reservaService.getRangoHoras());
		model.addAttribute("usuario", usuarioService.buscarUsuarioLogeado(model, principal));
		return "usuario/fechaReserva.html";
	}

	/**
	 * Muestra las aulas disponibles del colegio del usuario logeado, en un día y hora en concreto
	 * 
	 * @param reserva reserva a crear
	 * @param model model
	 * @param principal principal
	 * @return html con las aulas disponibles
	 */
	@PostMapping("/nuevaReserva/aulasDisponibles")
	public String buscarAulas(@ModelAttribute("reserva") Reserva reserva, Model model, Principal principal) {
		Usuario logeado = usuarioService.buscarUsuarioLogeado(model, principal);
		reserva.setUsuario(logeado);
		model.addAttribute("reserva", reserva);
		model.addAttribute("usuario", usuarioService.buscarUsuarioLogeado(model, principal));
		if (colegioService.findDiaDeSemana(reserva.getFecha(), logeado.getColegio().isFinde()) == false) {
			
			return "usuario/errorFinde.html";
		} else {
			if (diaFestivoService.findDiaFestivo(reserva.getFecha(), logeado.getColegio()) == false) {
				
				return "usuario/errorFestivo.html";
			}else {
				model.addAttribute("listaAulas", aulaService.buscarNoReservadosHora(logeado.getColegio().getId(),
						reserva.getFecha(), reserva.getHoraI()));
				return "usuario/aulasDisponibles.html";
			}
	
		}
	}

	/**
	 * Guarda la nueva reserva en la base de datos
	 * 
	 * @param r reserva
	 * @param principal principal
	 * @param model model
	 * @return html de una tabla de reservas
	 */
	@PostMapping("/nuevaReserva/submit")
	public String reservarAula(@ModelAttribute("reserva") Reserva r, Principal principal, Model model) {
		reservaService.save(r);
		model.addAttribute("usuario", usuarioService.buscarUsuarioLogeado(model, principal));
		return "redirect:/misReservas";
	}

	/**
	 * Muestra una tabla con las reservas pendientes, hechas por el usuario logeado
	 * 
	 * @param pageSize pagesize
	 * @param nombre nombre reserva buscado
	 * @param page page
	 * @param model model
	 * @param principal principal
	 * @return tabla de reservas propias
	 */
	@GetMapping("/misReservas")
	public String verReservasPropias(@RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("nombre") Optional<String> nombre, @RequestParam("page") Optional<Integer> page, Model model,
			Principal principal) {
		Usuario logeado = usuarioService.buscarUsuarioLogeado(model, principal);

		if (logeado.isSuperAdmin()) {
			return "redirect:/superAdmin/";
		}

		int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);

		int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

		String evalNombre = nombre.orElse(null);

		Page<Reserva> reservas = null;

		if (evalNombre == null) {
			reservas = reservaService.findPendientesByUsuario(logeado, PageRequest.of(evalPage, evalPageSize));
		} else {
			reservas = reservaService.findPendientesByUsuarioAndNombreAula(logeado, evalNombre,
					PageRequest.of(evalPage, evalPageSize));
		}

		Pager pager = new Pager(reservas.getTotalPages(), reservas.getNumber(), BUTTONS_TO_SHOW);

		model.addAttribute("listaReservas", reservas);
		model.addAttribute("selectedPageSize", evalPageSize);
		model.addAttribute("pageSizes", PAGE_SIZES);
		model.addAttribute("pager", pager);
		model.addAttribute("usuario", usuarioService.buscarUsuarioLogeado(model, principal));

		return "usuario/misReservas";
	}

	/**
	 * Borrar una reserva de la base de datos
	 * 
	 * @param id id reserva
	 * @param model model
	 * @param principal principal
	 * @return tabla de reservas propias pendientes
	 */
	@GetMapping("/misReservas/borrar/{id}")
	public String borrarReserva(@PathVariable("id") Long id, Model model, Principal principal) {
		Reserva buscada = reservaService.findById(id);
		Usuario logeado = usuarioService.buscarUsuarioLogeado(model, principal);
		if (buscada != null && logeado.getId() == buscada.getUsuario().getId()) {
			reservaService.deleteById(id);
		}
		return "redirect:/misReservas";
	}

	/**
	 * Muestra una tabla de reservas caducadas, hechas por el usuario logeado
	 * 
	 * @param pageSize pagesize
	 * @param nombre nombre buscado
	 * @param page page
	 * @param model model
	 * @param principal principal
	 * @return tabla de reservas propias caducadas
	 */
	@GetMapping("/misReservas/historico")
	public String verHistoricoReservasPropias(@RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("nombre") Optional<String> nombre, @RequestParam("page") Optional<Integer> page, Model model,
			Principal principal) {
		Usuario logeado = usuarioService.buscarUsuarioLogeado(model, principal);
		
		if (logeado.isSuperAdmin()) {
			return "redirect:/superAdmin/";
		}
		
		int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);

		int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

		String evalNombre = nombre.orElse(null);

		Page<Reserva> reservas = null;

		if (evalNombre == null) {
			reservas = reservaService.findCaducadasByUsuario(logeado, PageRequest.of(evalPage, evalPageSize));
		} else {
			reservas = reservaService.findCaducadasByUsuarioAndNombreAula(logeado, evalNombre,
					PageRequest.of(evalPage, evalPageSize));
		}

		Pager pager = new Pager(reservas.getTotalPages(), reservas.getNumber(), BUTTONS_TO_SHOW);

		model.addAttribute("listaReservas", reservas);
		model.addAttribute("selectedPageSize", evalPageSize);
		model.addAttribute("pageSizes", PAGE_SIZES);
		model.addAttribute("pager", pager);
		model.addAttribute("usuario", usuarioService.buscarUsuarioLogeado(model, principal));

		return "usuario/miHistoricoReservas";
	}

	/**
	 * Permite cambiar el idioma de las plantillas
	 * 
	 * @return plantilla con el idioma cambiado
	 */
	@GetMapping("/international")
	public String getInternationalPage() {
		return "international";
	}
	
	/**
	 * Permite ver los datos del usuario logeado para que pueda cambiarlos
	 * 
	 * @param model model
	 * @param principal principal
	 * @return html con los datos del usuario logeado
	 */
	@GetMapping("/perfil")
	public String verPerfil(Model model, Principal principal) {
		Usuario u = new Usuario();
		Usuario usuario= usuarioService.buscarUsuarioLogeado(model, principal);
		model.addAttribute("usuario", usuario);
		model.addAttribute("admin", u);
		return "fragments/perfil";
	}

}
