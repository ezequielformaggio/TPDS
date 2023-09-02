package controller;

import com.sun.org.apache.xpath.internal.operations.Or;
import miembro.Miembro;
import org.uqbarproject.jpa.java8.extras.PerThreadEntityManagers;
import organizacion.EstadoSolicitudVinculacion;
import organizacion.Organizacion;
import organizacion.Sector;
import organizacion.SolicitudVinculacion;
import repositorios.RepositorioUsuarios;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import usuario.RolUsuario;
import usuario.Usuario;

import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

public class VinculacionController extends ControllerBase{

  public ModelAndView listar(Request request, Response response) {
    Map<String, Object> modelo = this.inicializarModelo(request,response);
    Usuario usuario = this.getUsuario(request);
    Miembro miembro = usuario.getMiembro();
    //Boolean esMiembro = usuario.getRolUsuario() == RolUsuario.MIEMBRO;
    Boolean esMiembro = (Boolean) modelo.get("esMiembro");

    if(esMiembro) {
      List<Organizacion> organizaciones = PerThreadEntityManagers.getEntityManager()
          .createQuery("SELECT u FROM Organizacion u", Organizacion.class).getResultList();

      List<SolicitudVinculacion> solicitudes = new ArrayList<>();

      organizaciones.stream().forEach(organizacion -> solicitudes.addAll(organizacion.getSolicitudes()
          .stream().filter(solicitudVinculacion -> solicitudVinculacion.getMiembro().getId()== miembro.getId())
          .collect(Collectors.toList())));

      modelo.put("vinculaciones", solicitudes);
    }else {
      modelo.put("vinculacionesPendientes", usuario.getOrganizacion().getSolicitudes().stream()
          .filter(solicitudVinculacion ->
              solicitudVinculacion.getEstado() == EstadoSolicitudVinculacion.PENDIENTE)
          .collect(Collectors.toList()));
      modelo.put("vinculacionesRealizadas", usuario.getOrganizacion().getSolicitudes().stream()
          .filter(solicitudVinculacion ->
              solicitudVinculacion.getEstado() != EstadoSolicitudVinculacion.PENDIENTE)
          .collect(Collectors.toList()));
    }
    modelo.put("sectores", PerThreadEntityManagers.getEntityManager().createQuery("SELECT u FROM Sector u").getResultList());
    return new ModelAndView(modelo, "vinculaciones.html.hbs");
  }

  public Void solicitarVinculacion(Request request, Response response) {
    System.out.println(request.queryParams("sectorid"));

    Usuario usuario = this.getUsuario(request);
    Miembro miembro = usuario.getMiembro();

    Sector sector = PerThreadEntityManagers.getEntityManager().find(Sector.class, Long.parseLong(request.queryParams("sectorid")));
    System.out.println(request.queryParams("CHECKPOINT"));
    Organizacion organizacion = sector.getOrganizacion();

    SolicitudVinculacion nuevaSolicitud = new SolicitudVinculacion(sector, miembro);

    organizacion.agregarSolicitudVinculacion(nuevaSolicitud);
    EntityManager entityManager = PerThreadEntityManagers.getEntityManager();

    entityManager.getTransaction().begin();
    entityManager.persist(nuevaSolicitud);
    entityManager.persist(organizacion);
    entityManager.getTransaction().commit();

    response.redirect("/vinculaciones");
    return null;
  }

  public Void decisionVinculacion(Request request, Response response) {
    Usuario usuario = this.getUsuario(request);

    Organizacion organizacion = usuario.getOrganizacion();

    SolicitudVinculacion solicitudVinculacion = organizacion.getSolicitudes().stream()
        .filter(solicitud -> solicitud.getId() == Long.parseLong(request.queryParams("vinculacionId")))
        .collect(Collectors.toList()).get(0);

    if(request.queryParams("decisionVinculacion").contentEquals("Aceptar")){
      Sector sector = solicitudVinculacion.getSector();
      Miembro miembro = solicitudVinculacion.getMiembro();

      if(sector.getMiembros() == null){
        sector.crearListaMiembro();
      }

      if(miembro.getSectores() == null){
        miembro.crearListaSector();
      }
      solicitudVinculacion.aceptar();
    }else{
      solicitudVinculacion.rechazar();
    }

    EntityManager entityManager = PerThreadEntityManagers.getEntityManager();

    entityManager.getTransaction().begin();
    entityManager.persist(solicitudVinculacion);
    entityManager.persist(organizacion);
    entityManager.getTransaction().commit();

    response.redirect("/vinculaciones");
    return null;

  }
}
