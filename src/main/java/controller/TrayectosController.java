package controller;

import miembro.Miembro;
import org.uqbarproject.jpa.java8.extras.PerThreadEntityManagers;
import repositorios.RepositorioMiembros;
import repositorios.RepositorioTrayectos;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import trayectosytramos.Trayecto;
import usuario.Usuario;
import java.util.Map;

public class TrayectosController extends ControllerBase{
  Map<String, Object> modelo;
  Usuario usuario;
  Miembro miembro;
  private void validacionBasica(Request request, Response response){
    modelo = this.inicializarModelo(request,response);
    usuario = this.getUsuario(request);

    miembro = usuario.getMiembro();
  }

  public ModelAndView listarTrayectos(Request request, Response response) {
    validacionBasica(request,response);
    modelo.put("trayectos", miembro.getTrayectos());
    return new ModelAndView(modelo, "trayectosYTramos/trayectos.html.hbs");
  }

  public ModelAndView mostrarFormularioTrayectos(Request request, Response response) {
    validacionBasica(request,response);
    return new ModelAndView(modelo, "trayectosYTramos/nuevoTrayecto.html.hbs");
  }

  public ModelAndView crearTrayecto(Request request, Response response) {
    validacionBasica(request,response);
    Trayecto trayecto = new Trayecto(request.queryParams("descripcionTrayecto"));
    miembro.agregarTrayecto(trayecto);

    PerThreadEntityManagers.getEntityManager().getTransaction().begin();
    PerThreadEntityManagers.getEntityManager().persist(trayecto);
    PerThreadEntityManagers.getEntityManager().persist(miembro);
    PerThreadEntityManagers.getEntityManager().getTransaction().commit();

    response.redirect("/trayectos/" + trayecto.getIdTrayecto());
    return null;
  }

  public ModelAndView verTrayecto(Request request, Response response) {
    validacionBasica(request,response);

    Trayecto trayecto = RepositorioTrayectos.getInstance().encontrarTrayectoPorId(
        Long.parseLong(request.params(":idTrayecto")));

    modelo.put("tramos", trayecto.getTramos());
    modelo.put("trayecto", trayecto);
    return new ModelAndView(modelo, "trayectosYTramos/tramos.html.hbs");
  }
}
