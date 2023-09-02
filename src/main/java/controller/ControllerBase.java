package controller;

import repositorios.RepositorioUsuarios;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import usuario.RolUsuario;
import usuario.Usuario;

import java.util.HashMap;
import java.util.Map;

public class ControllerBase {

  public Map<String, Object> inicializarModelo(Request request, Response response) {
    Map<String, Object> modelo = new HashMap<>();

    Usuario usuario = getUsuario(request);

    modelo.put("sesionIniciada", request.session().attribute("user_id") != null);
    modelo.put("esMiembro", usuario.getRolUsuario() == RolUsuario.MIEMBRO);

    return modelo;
  }

  public Usuario getUsuario(Request request) {
    long user_id = request.session().attribute("user_id");
    Usuario usuario = RepositorioUsuarios.getInstance().buscarUsuarioPorIdUsuario(user_id);
    return usuario;
  }
}
