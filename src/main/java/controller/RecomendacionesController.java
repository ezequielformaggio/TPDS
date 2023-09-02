package controller;

import repositorios.RepositorioUsuarios;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import usuario.RolUsuario;
import usuario.Usuario;

import java.util.HashMap;
import java.util.Map;

public class RecomendacionesController extends ControllerBase{
  public ModelAndView mostrar(Request request, Response response) {
    Map<String, Object> modelo = this.inicializarModelo(request,response);
    return new ModelAndView(modelo, "recomendaciones.html.hbs");
  }
}