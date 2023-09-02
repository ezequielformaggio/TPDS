package controller;

import miembro.Miembro;
import miembro.TipoDocumento;
import repositorios.RepositorioMiembros;
import repositorios.RepositorioUsuarios;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import usuario.RolUsuario;
import usuario.Usuario;

import java.util.HashMap;
import java.util.Map;

public class SesionController {

  public ModelAndView mostrarLogin(Request request, Response response) {
    if(request.session().attribute("user_id") != null) {
      response.redirect("/");
      return null;
    }
    Map<String, Object> modelo = new HashMap<>();
    modelo.put("sesionIniciada", request.session().attribute("user_id") != null);
    return new ModelAndView(modelo, "sesion/login.html.hbs");
  }

  public Void crearSesion(Request request, Response response) {
    try {
      Usuario usuario = RepositorioUsuarios.getInstance().buscarPorUsuarioYContrasenia(
          request.queryParams("username"),
          request.queryParams("password"));

      request.session().attribute("user_id", usuario.getId());
      response.redirect("/");
      return null;
    } catch(Exception e) {
      request.session().attribute("user_id", null);
      response.redirect("/");
      return null;
    }
  }

  public ModelAndView mostrarFormularioRegistro(Request request, Response response) {
    Map<String, Object> modelo = new HashMap<>();
    if(request.session().attribute("primera_vez") == null) {
      modelo.put("primeraVez", true);
    } else {
      modelo.put("primeraVez", request.session().attribute("primera_vez"));
      modelo.put("valorNombre", request.session().attribute("nombre"));
      modelo.put("valorApellido", request.session().attribute("apellido"));
      modelo.put("valorNroDocumento", request.session().attribute("nro_documento"));
      modelo.put("valorUsername", request.session().attribute("username"));
    }

    modelo.put("sesionIniciada", request.session().attribute("user_id") != null);
    return new ModelAndView(modelo, "sesion/registro.html.hbs");
  }

  public Void crearRegistro(Request request, Response response) {
    try {
      Miembro miembro = new Miembro(
          request.queryParams("nombre"),
          request.queryParams("apellido"),
          TipoDocumento.valueOf(request.queryParams("tipoDoc")),
          request.queryParams("nroDocumento"));

      Usuario usuario = new Usuario(
          request.queryParams("username"),
          request.queryParams("password"),
          RolUsuario.MIEMBRO);

      usuario.asignarMiembro(miembro);

      RepositorioMiembros.getInstance().nuevoMiembro(miembro);
      RepositorioUsuarios.getInstance().nuevoUsuario(usuario);

      response.redirect("/");
      return null;
    } catch(Exception e) {
      request.session().attribute("user_id", null);
      request.session().attribute("primera_vez", false);
      request.session().attribute("nombre", request.queryParams("nombre"));
      request.session().attribute("apellido", request.queryParams("apellido"));
      request.session().attribute("nro_documento", request.queryParams("nroDocumento"));
      request.session().attribute("username", request.queryParams("username"));
      response.redirect("/registro");
      return null;
    }
  }

  public Object logout(Request request, Response response) {
    request.session().attribute("user_id", null);
    response.redirect("/");
    return null;
  }
}
