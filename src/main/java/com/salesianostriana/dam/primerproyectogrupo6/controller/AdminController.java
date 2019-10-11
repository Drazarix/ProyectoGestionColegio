package com.salesianostriana.dam.primerproyectogrupo6.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.salesianostriana.dam.primerproyectogrupo6.formbean.SearchBean;
import com.salesianostriana.dam.primerproyectogrupo6.model.Aula;
import com.salesianostriana.dam.primerproyectogrupo6.model.Colegio;
import com.salesianostriana.dam.primerproyectogrupo6.model.DiaFestivo;
import com.salesianostriana.dam.primerproyectogrupo6.model.Pager;
import com.salesianostriana.dam.primerproyectogrupo6.model.Reserva;
import com.salesianostriana.dam.primerproyectogrupo6.model.Usuario;
import com.salesianostriana.dam.primerproyectogrupo6.service.AulaServicio;
import com.salesianostriana.dam.primerproyectogrupo6.service.ColegioServicio;
import com.salesianostriana.dam.primerproyectogrupo6.service.DiaFestivoServicio;
import com.salesianostriana.dam.primerproyectogrupo6.service.ReservaServicio;
import com.salesianostriana.dam.primerproyectogrupo6.service.UsuarioServicio;

/**
 * Esta clase sirve para gestionar las rutas a las que solo tiene acceso el
 * administrador
 * 
 * @author Lucas Amado
 *
 */

@Controller
@RequestMapping("/admin")
public class AdminController {

	private static final int BUTTONS_TO_SHOW = 5;
	private static final int INITIAL_PAGE = 0;
	private static final int INITIAL_PAGE_SIZE = 10;
	private static final int[] PAGE_SIZES = { 5, 10, 20, 50 };

	@Autowired
	private UsuarioServicio usuarioServicio;

	@Autowired
	private ColegioServicio colegioServicio;

	@Autowired
	private AulaServicio aulaServicio;

	@Autowired
	private ReservaServicio reservaServicio;

	@Autowired
	private DiaFestivoServicio diaFestivoServicio;

	@GetMapping({ "/" })
	public String inicioAdmin(Model model, Principal principal) {
		model.addAttribute("usuario", usuarioServicio.buscarUsuarioLogeado(model, principal));
		return "admin/indexAdmin.html";
	}

	// USUARIOS
	/**
	 * Permite mostrar el html con la tabla de gestión de usuarios
	 * 
	 * @param pageSize  pagesize
	 * @param page      page
	 * @param nombre    nombre a buscar
	 * @param model     model
	 * @param principal principal
	 * @return tabla con los usuarios del colegio del admin
	 */
	@GetMapping("/gestionUsuarios")
	public String gestionarUsuarios(@RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer> page, @RequestParam("nombre") Optional<String> nombre, Model model,
			Principal principal) {

		int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);

		int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

		String evalNombre = nombre.orElse(null);

		Page<String> usuarios = null;

		Usuario logueado = usuarioServicio.buscarUsuarioLogeado(model, principal);

		if (evalNombre == null) {
			usuarios = usuarioServicio.buscarUsuariosValidados(logueado.getColegio().getId(),
					PageRequest.of(evalPage, evalPageSize));
		} else {
			usuarios = usuarioServicio.buscarUsuarios(logueado.getColegio().getId(), evalNombre,
					PageRequest.of(evalPage, evalPageSize));
		}
		Pager pager = new Pager(usuarios.getTotalPages(), usuarios.getNumber(), BUTTONS_TO_SHOW);

		model.addAttribute("lista", usuarios);
		model.addAttribute("selectedPageSize", evalPageSize);
		model.addAttribute("pageSizes", PAGE_SIZES);
		model.addAttribute("pager", pager);
		model.addAttribute("usuario", usuarioServicio.buscarUsuarioLogeado(model, principal));

		return "admin/gestionUsuarios";

	}

	/**
	 * Permite mostrar el fromulario para editar un usuario
	 * 
	 * @param id        id usuario
	 * @param model     model model
	 * @param principal principal
	 * @return html con el formulario para editar al usuario
	 */
	@GetMapping("/gestionUsuarios/editar/{id}")
	public String mostrarFormularioEdicionUsuarios(@PathVariable("id") long id, Model model, Principal principal) {
		model.addAttribute("usuario", usuarioServicio.buscarUsuarioLogeado(model, principal));
		Usuario uEditar = usuarioServicio.findById(id);
		model.addAttribute("colegios", colegioServicio.findAll());
		if (uEditar != null) {
			model.addAttribute("usuario", uEditar);
			return "admin/editarUsuario.html";// aqui le paso el formulario de edición
		} else {
			return "redirect:/admin/gestionUsuarios";
		}
	}

	/**
	 * Guardado del usuario editado
	 * 
	 * @param u usuario editado
	 * @return html con el usuario editado y guardado en la base de datos
	 */
	@PostMapping("/gestionUsuarios/editar/submit")
	public String procesarFormularioEdicionUsuarios(@ModelAttribute("usuario") Usuario u) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		u.setPassword(passwordEncoder.encode(u.getPassword()));
		usuarioServicio.edit(u);
		return "redirect:/admin/gestionUsuarios";
	}

	/**
	 * Permite borrar un usuario, encontrado por su id
	 * 
	 * @param id id del usuario
	 * @return tabla gestión de usuarios sin el usuario borrado
	 */
	@GetMapping("/gestionUsuarios/borrar/{id}")
	public String darBajaUsuario(@PathVariable("id") long id) {
		if (usuarioServicio.findById(id) != null) {
			usuarioServicio.deleteById(id);
		}
		return "redirect:/admin/gestionUsuarios";
	}

	/**
	 * Permite buscar usuarios por su nombre
	 * 
	 * @param principal principal
	 * @param searchBean nombre buscado
	 * @param model model
	 * @return html com los usuarios buscados
	 */
	@PostMapping("/searchUsuarios")
	public String searchUsuario(Principal principal, @ModelAttribute("searchForm") SearchBean searchBean, Model model) {
		model.addAttribute("usuario", usuarioServicio.buscarUsuarioLogeado(model, principal));
		model.addAttribute("lista", usuarioServicio.findByNombre(searchBean.getSearch()));
		return "admin/gestionUsuarios";
	}

	
	
	// VALIDACIONES
	/**
	 * Permite ver los usuarios sin validar
	 * 
	 * @param pageSize pagesize
	 * @param nombre nombre buscado
	 * @param page page
	 * @param model model
	 * @param principal principal
	 * @return html con los usuarios sin validar
	 */
	@GetMapping("/gestionUsuarios/validaciones")
	public String gestionarValidaciones(@RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("nombre") Optional<String> nombre, @RequestParam("page") Optional<Integer> page, Model model,
			Principal principal) {

		int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);

		int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

		Usuario logueado = usuarioServicio.buscarUsuarioLogeado(model, principal);

		String evalNombre = nombre.orElse(null);

		Page<String> sinValidar = null;
		if (evalNombre == null) {
			sinValidar = usuarioServicio.buscarUsuariosNoValidados(logueado.getColegio().getId(),
					PageRequest.of(evalPage, evalPageSize));
		} else {
			sinValidar = usuarioServicio.buscarUsuariosNoValidadosPorNombre(logueado.getColegio().getId(), evalNombre,
					PageRequest.of(evalPage, evalPageSize));
		}

		Pager pager = new Pager(sinValidar.getTotalPages(), sinValidar.getNumber(), BUTTONS_TO_SHOW);

		model.addAttribute("logeado", usuarioServicio.buscarUsuarioLogeado(model, principal));
		model.addAttribute("listaNoValidados", sinValidar);
		model.addAttribute("selectedPageSize", evalPageSize);
		model.addAttribute("pageSizes", PAGE_SIZES);
		model.addAttribute("pager", pager);
		model.addAttribute("usuario", usuarioServicio.buscarUsuarioLogeado(model, principal));

		return "admin/validarUsuarios.html";
	}

	/**
	 * Permite rechazar a un usuario por su id
	 * 
	 * @param id id del usuario
	 * @return html con los usuarios sin validar
	 */
	@GetMapping("/gestionUsuarios/validaciones/rechazar/{id}")
	public String rechazarValidacion(@PathVariable("id") long id) {
		Usuario uEditar = usuarioServicio.findById(id);
		if (uEditar != null) {
			usuarioServicio.deleteById(id);
		}
		return "redirect:/admin/gestionUsuarios/validaciones";
	}

	/**
	 * Permite validar a un usuario por su id
	 * 
	 * @param id id del usuario
	 * @return html de los usuarios sin validar
	 */
	@GetMapping("/gestionUsuarios/validaciones/aceptar/{id}")
	public String noValidar(@PathVariable("id") long id) {
		Usuario uEditar = usuarioServicio.findById(id);
		uEditar.setValidate(true);
		usuarioServicio.edit(uEditar);
		return "redirect:/admin/gestionUsuarios/validaciones";
	}
	
	

	// AULAS
	/**
	 * Permite ver una tabla con las aulas del colegio del admin
	 * 
	 * @param principal principal
	 * @param pageSize pagesize
	 * @param page page
	 * @param nombre nombre buscado
	 * @param model model
	 * @return html con la tabla de aulas del colegio del admin
	 */
	@GetMapping("/gestionAulas")
	public String gestionarAulas(Principal principal, @RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer> page, @RequestParam("nombre") Optional<String> nombre,
			Model model) {

		int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);

		int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

		String evalNombre = nombre.orElse(null);

		Usuario logeado = usuarioServicio.buscarUsuarioLogeado(model, principal);

		Page<Aula> aulas = null;

		if (evalNombre == null) {
			aulas = aulaServicio.findByColegio(logeado.getColegio().getId(), PageRequest.of(evalPage, evalPageSize));
		} else {
			aulas = aulaServicio.findByColegioAndNombreAula(evalNombre, logeado.getColegio().getId(),
					PageRequest.of(evalPage, evalPageSize));
		}

		Pager pager = new Pager(aulas.getTotalPages(), aulas.getNumber(), BUTTONS_TO_SHOW);

		model.addAttribute("listaAulas", aulas);
		model.addAttribute("selectedPageSize", evalPageSize);
		model.addAttribute("pageSizes", PAGE_SIZES);
		model.addAttribute("pager", pager);
		model.addAttribute("usuario", usuarioServicio.buscarUsuarioLogeado(model, principal));

		return "admin/gestionarAulas.html";
	}

	/**
	 * Permite ver el formulario de edición del aula
	 * 
	 * @param id id del aula
	 * @param model model
	 * @param principal principal
	 * @return formulario para editar el aula
	 */
	@GetMapping("/gestionAulas/editar/{id}")
	public String editarAula(@PathVariable("id") long id, Model model, Principal principal) {
		Aula editar = aulaServicio.findById(id);
		model.addAttribute("usuario", usuarioServicio.buscarUsuarioLogeado(model, principal));
		if (editar != null) {
			model.addAttribute("aula", editar);
			model.addAttribute("colegio", usuarioServicio.buscarUsuarioLogeado(model, principal).getColegio());
			return "admin/formNuevaAula";
		}
		return "redirect:/admin/gestionAulas";
	}

	/**
	 * Guarda el aula editada en la base de datos
	 * 
	 * @param a aula
	 * @return html con la tabla de gestion de aulas
	 */
	@PostMapping("/gestionAulas/editar/submit")
	public String guardarCambiosAula(@ModelAttribute("aula") Aula a) {
		aulaServicio.edit(a);
		return "redirect:/admin/gestionAulas";
	}

	/**
	 * Permite crear una nueva aula en el colegio del admin
	 * 
	 * @param model model
	 * @param principal principal
	 * @return html con el formulario para crear una nueva aula
	 */
	@GetMapping("/gestionAulas/nuevaAula")
	public String crearAula(Model model, Principal principal) {
		model.addAttribute("aula", new Aula());
		model.addAttribute("colegios", colegioServicio.findAll());
		model.addAttribute("usuario", usuarioServicio.buscarUsuarioLogeado(model, principal));
		return "/admin/formNuevaAula";
	}

	/**
	 * Permite guardar el aula en la base de datos
	 * 
	 * @param aula nueva aula
	 * @param model model
	 * @return html con la tabla de aulas del colegio del admin
	 */
	@PostMapping("/gestionAulas/nuevaAula/submit")
	public String submit(@ModelAttribute("aula") Aula aula, Model model) {
		aulaServicio.save(aula);
		return "redirect:/admin/gestionAulas";
	}

	/**
	 * Permite borrar un aula de la base de datos
	 * 
	 * @param id id aula
	 * @param model model
	 * @param principal principal
	 * @return html con la tabla de gestion de aulas
	 */
	@GetMapping("/gestionAulas/borrar/{id}")
	public String borrarAula(@PathVariable("id") long id, Model model, Principal principal) {
		if (aulaServicio.findById(id) != null) {
			aulaServicio.deleteById(id);
		}
		return "redirect:/admin/gestionAulas";
	}

	
	
	// RESERVAS
	/**
	 * Permite mostrar la tabla de reservas de todos los usuarios del colegio que aún están pendientes
	 * 
	 * @param principal principal
	 * @param pageSize pagesize
	 * @param page page
	 * @param nombre nombre
	 * @param model nombre
	 * @return html con la tabla de gestión de reservas
	 */
	@GetMapping("/gestionReservas")
	public String gestionarReservas(Principal principal, @RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer> page, @RequestParam("nombre") Optional<String> nombre,
			Model model) {

		int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);

		int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

		String evalNombre = nombre.orElse(null);

		Page<Reserva> reservas = null;

		Usuario logeado = usuarioServicio.buscarUsuarioLogeado(model, principal);

		if (evalNombre == null) {
			reservas = reservaServicio.findPendientesByColegio(logeado.getColegio().getId(),
					PageRequest.of(evalPage, evalPageSize));
		} else {
			reservas = reservaServicio.findPendientesByColegioAndNombreUsuario(logeado.getColegio().getId(), evalNombre,
					PageRequest.of(evalPage, evalPageSize));
		}
		Pager pager = new Pager(reservas.getTotalPages(), reservas.getNumber(), BUTTONS_TO_SHOW);

		model.addAttribute("listaReservas", reservas);
		model.addAttribute("usuario", logeado);
		model.addAttribute("selectedPageSize", evalPageSize);
		model.addAttribute("pageSizes", PAGE_SIZES);
		model.addAttribute("pager", pager);
		model.addAttribute("usuario", usuarioServicio.buscarUsuarioLogeado(model, principal));

		return "admin/gestionarReservas";
	}

	/**
	 * Permite borrar una reserva de la base de datos
	 * 
	 * @param id id reserva
	 * @return html de la tabla de gestión de reservas
	 */
	@GetMapping("/gestionReservas/borrar/{id}")
	public String borrarReserva(@PathVariable("id") long id) {
		Reserva borrar = reservaServicio.findById(id);
		if (borrar != null) {
			reservaServicio.deleteById(id);
		}
		return "redirect:/admin/gestionReservas";
	}

	/**
	 * Permite ver las reservas caducadas del colegio del administrador
	 * 
	 * @param principal principal
	 * @param pageSize pagesize
	 * @param page page
	 * @param nombre nombre reserva
	 * @param model model
	 * @return tabla de reservas caducadas del colegio del admin
	 */
	@GetMapping("/gestionReservas/historico")
	public String historialColegioConcreto(Principal principal, @RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer> page, @RequestParam("nombre") Optional<String> nombre,
			Model model) {

		int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);

		int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

		String evalNombre = nombre.orElse(null);

		Page<Reserva> reservas = null;

		Usuario logeado = usuarioServicio.buscarUsuarioLogeado(model, principal);

		if (evalNombre == null) {
			reservas = reservaServicio.findCaducadasByColegio(logeado.getColegio().getId(),
					PageRequest.of(evalPage, evalPageSize));
		} else {
			reservas = reservaServicio.findCaducadasByColegioAndNombreUsuario(logeado.getColegio().getId(), evalNombre,
					PageRequest.of(evalPage, evalPageSize));
		}
		Pager pager = new Pager(reservas.getTotalPages(), reservas.getNumber(), BUTTONS_TO_SHOW);

		model.addAttribute("listaReservas", reservas);
		model.addAttribute("usuario", logeado);
		model.addAttribute("selectedPageSize", evalPageSize);
		model.addAttribute("pageSizes", PAGE_SIZES);
		model.addAttribute("pager", pager);
		model.addAttribute("usuario", usuarioServicio.buscarUsuarioLogeado(model, principal));

		return "admin/historicoReservasColegio";
	}

	/**
	 * Pemrite ver la tabla con los días festivos del colegio del admin
	 * 
	 * @param principal principal
	 * @param pageSize pagesize
	 * @param page page
	 * @param nombre nombre dia festivo
	 * @param model model
	 * @return tabla de dias festivos de un colegio
	 */
	@GetMapping("/gestionFestivos")
	public String listarFestivos(Principal principal, @RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer> page, @RequestParam("nombre") Optional<String> nombre,
			Model model) {

		int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);

		int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

		String evalNombre = nombre.orElse(null);

		Usuario logeado = usuarioServicio.buscarUsuarioLogeado(model, principal);

		Page<DiaFestivo> productos = null;

		if (evalNombre == null) {
			productos = diaFestivoServicio.findDiasFestivosByColegio(
					usuarioServicio.buscarUsuarioLogeado(model, principal).getColegio(),
					PageRequest.of(evalPage, evalPageSize));
		} else {
			productos = diaFestivoServicio.findDiasFestivosByColegioAndNombreFestivo(evalNombre, logeado.getColegio(),
					PageRequest.of(evalPage, evalPageSize));
		}
		Pager pager = new Pager(productos.getTotalPages(), productos.getNumber(), BUTTONS_TO_SHOW);

		model.addAttribute("festivos", productos);
		model.addAttribute("selectedPageSize", evalPageSize);
		model.addAttribute("pageSizes", PAGE_SIZES);
		model.addAttribute("pager", pager);
		model.addAttribute("usuario", usuarioServicio.buscarUsuarioLogeado(model, principal));

		return "admin/gestionFestivos";
	}

	/**
	 * Permite borrar un día festivo de la base de datos
	 * 
	 * @param id id del dia festivo
	 * @return html tabla de gestion de gestivos
	 */
	@GetMapping("/gestionFestivos/borrar/{id}")
	public String borrarFestivo(@PathVariable("id") long id) {
		DiaFestivo borrar = diaFestivoServicio.findById(id);
		if (borrar != null) {
			diaFestivoServicio.deleteById(id);
		}
		return "redirect:/admin/gestionFestivos";
	}

	/**
	 * Permite mostrar el formulario para crear un nuevo festivo
	 * 
	 * @param model model
	 * @param principal principal
	 * @return html para crear un nuevo día festivo
	 */
	@GetMapping("/gestionFestivos/nuevoFestivo")
	public String nuevoFestivo(Model model, Principal principal) {
		model.addAttribute("festivo", new DiaFestivo());
		model.addAttribute("hoy", LocalDate.now());
		model.addAttribute("usuario", usuarioServicio.buscarUsuarioLogeado(model, principal));
		return "admin/nuevoFestivo";
	}
	
	
	/**
	 * Permite guardar un nuevo día festivo en el colegio del admin
	 * 
	 * @param f dia festivo
	 * @param principal principal
	 * @param model model
	 * @return html de gestion de dias festivos
	 */
	@PostMapping("/gestionFestivos/submit")
	public String crearFestivo(@ModelAttribute("festivo") DiaFestivo f, Principal principal, Model model) {
		model.addAttribute("usuario", usuarioServicio.buscarUsuarioLogeado(model, principal));
		Usuario logeado = usuarioServicio.buscarUsuarioLogeado(model, principal);
		f.setColegio(logeado.getColegio());
		
		if (diaFestivoServicio.comprobarRepetidos(f.getFecha(), f.getColegio()) == false ) {
			diaFestivoServicio.save(f);
			
			List <Reserva> reservasBorrar = reservaServicio.findReservasEnDiasFestivos(f.getFecha());
				for(Reserva r: reservasBorrar){
					System.out.println(r);
					reservaServicio.deleteById(r.getId());
				}
		}
		
		model.addAttribute("usuario", usuarioServicio.buscarUsuarioLogeado(model, principal));

		return "redirect:/admin/gestionFestivos";
	}


	/**
	 * Permite cambiar el estado del fin de semana de un colegio, para que se pueda reservar o no
	 * 
	 * @param id id colegio
	 * @param model model
	 * @param principal principal
	 * @return html principal del admin
	 */
	
	@GetMapping("/findeColegio/{id}")
	public String findeColegio(@PathVariable("id") long id, Model model, Principal principal) {
		Usuario u = usuarioServicio.buscarUsuarioLogeado(model, principal);
		model.addAttribute("usuario", u);
		Colegio finde = u.getColegio();
		if (finde != null && u.getColegio().isFinde() == true) {
			u.getColegio().isFinde();
			finde.setFinde(false);
			colegioServicio.edit(finde);
		} else if (finde != null && u.getColegio().isFinde() == false) {
			finde.setFinde(true);
			colegioServicio.edit(finde);
		}
		return "redirect:/admin/";
	}

}
