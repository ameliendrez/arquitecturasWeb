package controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import dao.EvaluacionDAO;
import dao.TrabajoDAO;
import dao.UsuarioDAO;
import entidades.Evaluacion;
import entidades.Tematica;
import entidades.Trabajo;
import entidades.Usuario;
import exceptions.RecursoDuplicado;
import exceptions.RecursoNoExiste;


@Path("/usuarios")
public class UsuarioController {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON) 
	public List<Usuario> getAllUsuarios() {
		return UsuarioDAO.getInstance().findAll();
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Usuario getUsuarioById(@PathParam("id") String msg) {
		int id = Integer.valueOf(msg);
		Usuario usuario = UsuarioDAO.getInstance().findById(id);
		if(usuario != null)
			return usuario;
		else
			throw new RecursoNoExiste(id);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createUsuario(Usuario usuario) {
		Usuario result = UsuarioDAO.getInstance().persist(usuario);
		if(result == null) {
			throw new RecursoDuplicado(usuario.getDni());
		}else {
			return Response.status(201).entity(usuario).build();
		}
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteUsuario(@PathParam("id") int id) {
		boolean wasDeleted = UsuarioDAO.getInstance().delete(id);
		if(wasDeleted)
			return Response.status(200).build();
		else
			throw new RecursoNoExiste(id);
	}

	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateUsuario(@PathParam("id") int id, Usuario usuario) {
		Usuario result = UsuarioDAO.getInstance().update(id, usuario);
		if(result == null) {
			throw new RecursoNoExiste(id);
		}else {
			return Response.status(200).entity(usuario).build();
		}
	}

	@GET
	@Path("/{id}/trabajos")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Trabajo> getTrabajosDeUsuarioById(@PathParam("id") String msg) {
		int id = Integer.valueOf(msg);
		List<Trabajo>trabajos = UsuarioDAO.getInstance().findAllTrabajosAsignados(id);
		if(trabajos != null) {
			return trabajos;

		}else
			throw new RecursoNoExiste(id);
	}
	
	@GET
	@Path("/trabajosEnviados/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Trabajo> getTrabajosEnviadosDeUsuarioById(@PathParam("id") String msg) {
		int id = Integer.valueOf(msg);
		List<Trabajo>trabajos = UsuarioDAO.getInstance().findAllTrabajosEnviados(id);
		if(trabajos != null) {
			return trabajos;

		}else
			throw new RecursoNoExiste(id);
	}

	@GET
	@Path("/{id}/evaluaciones")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Evaluacion> getEvaluacionesDeUsuarioById(@PathParam("id") String msg) {
		int id = Integer.valueOf(msg);
		Usuario usuario = UsuarioDAO.getInstance().findById(id);
		if(usuario != null)
			return UsuarioDAO.getInstance().getEvaluaciones(id);
		else
			throw new RecursoNoExiste(id);
	}

	@GET
	@Path("/{id}/tematicas")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Tematica> getConocimientosDeUsuarioById(@PathParam("id") String msg) {
		int id = Integer.valueOf(msg);
		Usuario usuario = UsuarioDAO.getInstance().findById(id);
		if(usuario != null)
			return UsuarioDAO.getInstance().conocimientosDeUnUsuario(usuario);
		else
			throw new RecursoNoExiste(id);
	}

	@POST
	@Path("/asignar/{idUsuario}/{idTrabajo}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response asignarTrabajoRevisor(
			@PathParam("idUsuario") Integer idUsuario, @PathParam("idTrabajo") Integer idTrabajo) throws Exception {
		Boolean result = UsuarioDAO.getInstance().addTrabajo(idUsuario, idTrabajo);
		if (result) {
			Usuario usuario = UsuarioDAO.getInstance().findById(idUsuario);
			return Response.status(201).entity(usuario).build();
		}
		throw new Exception("Ocurrio un problema durante la asignacion del Trabajo con id: " + idTrabajo + " al usuario con id: " + idUsuario);
	}
	
	@POST
	@Path("/aceptar/{idUsuario}/{idTrabajo}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response aceptarTrabajoRevisor(
			@PathParam("idUsuario") Integer idUsuario, @PathParam("idTrabajo") Integer idTrabajo, String observacion) throws Exception {
		Evaluacion evaluacion = new Evaluacion();
		evaluacion.setEvaluador(UsuarioDAO.getInstance().findById(idUsuario));
		evaluacion.setTrabajo(TrabajoDAO.getInstance().findById(idTrabajo));
		evaluacion.setFecha(Calendar.getInstance());
		evaluacion.setObservacion(observacion);

		Evaluacion result = EvaluacionDAO.getInstance().persist(evaluacion);
		if (result != null) {
			Evaluacion ev = EvaluacionDAO.getInstance().findById(1);
			return Response.status(201).entity(ev).build();
		}
		throw new Exception("Ocurrio un problema durante la asignacion del Trabajo con id: " + idTrabajo + " al usuario con id: " + idUsuario);
	}
	
	@GET
	@Path("/revisados/{id}/{from}/{to}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Trabajo> findTrabajosRevisadosEnRangoDeUsuarioById(@PathParam("id") String id, @PathParam("from") String dateFrom,
			@PathParam("to") String dateTo) throws ParseException {
		
		int idUser = Integer.valueOf(id);
		
		Calendar fechaInicio = Calendar.getInstance();
		Calendar fechaFin = Calendar.getInstance();
		fechaInicio.setTime(this.formatDateFromString(dateFrom));
		fechaFin.setTime(this.formatDateFromString(dateTo));

		List<Trabajo>revisiones = UsuarioDAO.getInstance().findAllTrabajosInvestigacionRevisorEnRango(idUser, fechaInicio, fechaFin);
		return revisiones; //Response.status(201).entity(revisiones).build();
	}

	protected Date formatDateFromString(String fecha) throws ParseException {
		String pattern = "dd-MM-yyyy";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		return simpleDateFormat.parse(fecha);
	}
	
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteUsuariso() {
		UsuarioDAO.getInstance().removeAll();
		boolean wasDeleted = UsuarioDAO.getInstance().getCantidadUsuarios() == 0;
		if(wasDeleted)
			return Response.status(200).build();
		else
			return Response.status(500).build();
	}
	
}