package controller;

import miembro.Miembro;
import org.mozilla.javascript.tools.debugger.treetable.TreeTableModelAdapter;
import org.uqbarproject.jpa.java8.extras.PerThreadEntityManagers;
import organizacion.Direccion;
import repositorios.RepositorioTrayectos;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import transporte.mediosdetransporte.MedioDeTransporte;
import transporte.mediosdetransporte.TransporteContratado;
import transporte.mediosdetransporte.TransporteEcologico;
import transporte.mediosdetransporte.VehiculoParticular;
import transporte.mediosdetransporte.transportepublico.Parada;
import transporte.mediosdetransporte.transportepublico.TransportePublico;
import transporte.tipostransportes.TipoTransporteEcologico;
import trayectosytramos.Tramo;
import trayectosytramos.Trayecto;
import usuario.Usuario;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class TramosController extends ControllerBase{
  Map<String, Object> modelo;
  Usuario usuario;
  Miembro miembro;
  private void validacionBasica(Request request, Response response){
    modelo = this.inicializarModelo(request,response);
    usuario = this.getUsuario(request);

    miembro = usuario.getMiembro();
  }

  public ModelAndView crearTramo(Request request, Response response) {
    validacionBasica(request,response);
    modelo.put("idTrayecto", Long.parseLong(request.params(":idTrayecto")));

    Direccion direccionOrigen = null;
    Direccion direccionDestino = null;

    if(request.queryParams("medioDeTransporte").equalsIgnoreCase("TransportePublico")) {

    } else {
      direccionOrigen = new Direccion(Integer.parseInt(request.queryParams("localidadOrigen")),
          request.queryParams("calleOrigen"), Integer.parseInt(request.queryParams("alturaOrigen")));
      direccionDestino = new Direccion(Integer.parseInt(request.queryParams("localidadDestino")),
          request.queryParams("calleDestino"), Integer.parseInt(request.queryParams("alturaDestino")));
    }

    Tramo tramo = null;
    Trayecto trayecto = null;

    switch(request.queryParams("medioDeTransporte")) {
      case "TransportePublico":

        TransportePublico linea = PerThreadEntityManagers.getEntityManager()
            .find(TransportePublico.class, Long.parseLong(request.queryParams("lineaTransportePublico")));

        List<Parada> paradasChequeadas = linea.getParadas().stream().filter(
            parada -> request.queryParams(parada.getId().toString()) != null
                && request.queryParams(parada.getId().toString()).equalsIgnoreCase("checked"))
            .collect(Collectors.toList());

        Parada paradaOrigen = paradasChequeadas.get(0);
        Parada paradaDestino = paradasChequeadas.get(paradasChequeadas.size() - 1);

        tramo = new Tramo(linea, paradaOrigen, paradaDestino, miembro);

        trayecto = RepositorioTrayectos.getInstance().encontrarTrayectoPorId(
            Long.parseLong(request.params(":idTrayecto")));

        trayecto.agregarTramos(tramo);

        break;
      case "TransporteContratado":
        TransporteContratado transporteContratado = PerThreadEntityManagers
            .getEntityManager().find(TransporteContratado.class,
                Long.parseLong(request.queryParams("medioTransporteContratado")));

        tramo = new Tramo(transporteContratado, direccionOrigen, direccionDestino, miembro);

        trayecto = RepositorioTrayectos.getInstance().encontrarTrayectoPorId(
            Long.parseLong(request.params(":idTrayecto")));

        trayecto.agregarTramos(tramo);

        break;
      case "TransporteEcologico":
        TransporteEcologico transporteEcologico = PerThreadEntityManagers
            .getEntityManager().find(TransporteEcologico.class,
                Long.parseLong(request.queryParams("medioTransporteEcologico")));

        tramo = new Tramo(transporteEcologico, direccionOrigen, direccionDestino, miembro);

        trayecto = RepositorioTrayectos.getInstance().encontrarTrayectoPorId(
            Long.parseLong(request.params(":idTrayecto")));

        trayecto.agregarTramos(tramo);

        break;
      case "TransporteParticular":
        VehiculoParticular transporteParticular = PerThreadEntityManagers
            .getEntityManager().find(VehiculoParticular.class,
                Long.parseLong(request.queryParams("medioTransporteParticular")));

        tramo = new Tramo(transporteParticular, direccionOrigen, direccionDestino, miembro);

        trayecto = RepositorioTrayectos.getInstance().encontrarTrayectoPorId(
            Long.parseLong(request.params(":idTrayecto")));

        trayecto.agregarTramos(tramo);

        break;
    }

    PerThreadEntityManagers.getEntityManager().getTransaction().begin();
    PerThreadEntityManagers.getEntityManager().persist(tramo);
    PerThreadEntityManagers.getEntityManager().persist(trayecto);
    PerThreadEntityManagers.getEntityManager().persist(miembro);
    PerThreadEntityManagers.getEntityManager().getTransaction().commit();

    response.redirect("/trayectos/" + Long.parseLong(request.params(":idTrayecto")) +
        "/tramos");
    return null;
  }

  public ModelAndView mostrarFormularioTramos(Request request, Response response) {
    validacionBasica(request,response);
    modelo.put("idTrayecto", Long.parseLong(request.params(":idTrayecto")));
    modelo.put("medioDeTransporte", request.queryParams("medioDeTransporte"));

    switch(request.queryParams("medioDeTransporte")) {
      case "TransportePublico":
        modelo.put("transportePublico", true);
        modelo.put("transporteContratado", false);
        modelo.put("transporteEcologico", false);
        modelo.put("transporteParticular", false);
        TransportePublico linea = PerThreadEntityManagers.getEntityManager()
            .find(TransportePublico.class, Long.parseLong(request.queryParams("lineaTransportePublico")));
        modelo.put("lineaTransportePublico", linea);
        modelo.put("paradas", linea.getParadas());

        break;
      case "TransporteContratado":
        modelo.put("transportePublico", false);
        modelo.put("transporteContratado", true);
        modelo.put("transporteEcologico", false);
        modelo.put("transporteParticular", false);
        modelo.put("transportesContratados", PerThreadEntityManagers.getEntityManager()
            .createQuery("SELECT t FROM TransporteContratado t", TransporteContratado.class)
            .getResultList());
        break;
      case "TransporteEcologico":
        modelo.put("transportePublico", false);
        modelo.put("transporteContratado", false);
        modelo.put("transporteEcologico", true);
        modelo.put("transporteParticular", false);
        modelo.put("transportesEcologicos", PerThreadEntityManagers.getEntityManager()
            .createQuery("SELECT t FROM TransporteEcologico t", TransporteEcologico.class)
            .getResultList());
        break;
      case "TransporteParticular":
        modelo.put("transportePublico", false);
        modelo.put("transporteContratado", false);
        modelo.put("transporteEcologico", false);
        modelo.put("transporteParticular", true);
        modelo.put("transportesParticulares", PerThreadEntityManagers.getEntityManager()
            .createQuery("SELECT t FROM VehiculoParticular t", VehiculoParticular.class)
            .getResultList());
        break;
    }

    return new ModelAndView(modelo, "trayectosYTramos/nuevoTramo.html.hbs");
  }

  public ModelAndView mostrarFormularioMedioTransporte(Request request, Response response) {
    validacionBasica(request, response);
    modelo.put("idTrayecto", Long.parseLong(request.params(":idTrayecto")));
    modelo.put("lineasTransportePublico", PerThreadEntityManagers.getEntityManager()
        .createQuery("SELECT t FROM TransportePublico t", TransportePublico.class)
        .getResultList());
    return new ModelAndView(modelo, "trayectosYTramos/medioTransporte.html.hbs");
  }

  public ModelAndView verTramo(Request request, Response response) {
    return null;
  }

  public ModelAndView listarTramos(Request request, Response response) {
    validacionBasica(request, response);


    Trayecto trayecto = RepositorioTrayectos.getInstance().encontrarTrayectoPorId(
        Long.parseLong(request.params(":idTrayecto")));
    modelo.put("trayecto", trayecto);
    modelo.put("tramos", trayecto.getTramos());
    return new ModelAndView(modelo, "trayectosYTramos/tramos.html.hbs");
  }

  public ModelAndView mostrarCargarTramos(Request request, Response response) {
    validacionBasica(request, response);

    Long idTrayecto = Long.parseLong(request.params(":idTrayecto"));
    Trayecto trayecto =  PerThreadEntityManagers.getEntityManager().find(Trayecto.class, idTrayecto);
    String descripcion = trayecto.getDescripcion();

    modelo.put("idTrayecto",idTrayecto);
    modelo.put("tramos", trayecto.getTramos());
    modelo.put("descripcionTrayecto",descripcion);
    return new ModelAndView(modelo, "trayectosYTramos/nuevosTramos.html.hbs");
  }
}
