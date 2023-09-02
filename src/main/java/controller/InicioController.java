package controller;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import java.util.Map;

public class InicioController extends ControllerBase{
  public ModelAndView mostrar(Request request, Response response) {
    Map<String, Object> modelo = this.inicializarModelo(request,response);
    return new ModelAndView(modelo, "index.html.hbs");
  }
}
