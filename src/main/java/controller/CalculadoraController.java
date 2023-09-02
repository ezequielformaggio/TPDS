package controller;

import com.sun.org.apache.xpath.internal.operations.Or;
import medicion.Periodicidad;
import miembro.Miembro;
import org.uqbarproject.jpa.java8.extras.PerThreadEntityManagers;
import organizacion.Organizacion;
import organizacion.Plazo;
import organizacion.Sector;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import usuario.RolUsuario;
import usuario.Usuario;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CalculadoraController extends ControllerBase{

  public ModelAndView listar(Request request, Response response){
    Map<String, Object> modelo = this.inicializarModelo(request,response);
    Usuario usuario = this.getUsuario(request);
    if(usuario.getRolUsuario() == RolUsuario.MIEMBRO){
      Miembro miembro = usuario.getMiembro();
      List<Sector> sectores = miembro.getSectores();
      List<Organizacion> organizaciones = new ArrayList<>();
      sectores.stream().forEach(sector -> organizaciones.add(sector.getOrganizacion()));
      modelo.put("organizaciones", organizaciones);
      if(organizaciones.size() > 0){
        modelo.put("hayOrganizaciones", true);
      }else{
        modelo.put("hayOrganizaciones", false);
      }
      return new ModelAndView(modelo, "calculadoraMiembro.html.hbs");
    }else{
      return new ModelAndView(modelo, "calculadoraOrganizacion.html.hbs");
    }
  }

  public ModelAndView calcularHCMiembro(Request request, Response response){
    Map<String, Object> modelo = this.inicializarModelo(request,response);
    Usuario usuario = this.getUsuario(request);
    Miembro miembro = usuario.getMiembro();

    Long idOrg = Long.parseLong(request.queryParams("organizacion"));
    String string = request.queryParams("periodicidad");
    Periodicidad periodicidad = Periodicidad.valueOf(string);


    Organizacion organizacion = PerThreadEntityManagers.getEntityManager().find(Organizacion.class, idOrg);

    double hc = miembro.obtenerHuellaCarbono(organizacion, periodicidad);

    modelo.put("resultado", hc);

    return new ModelAndView(modelo, "resultadohc.html.hbs");
  }

  public ModelAndView calcularHCOrganizacion(Request request, Response response){
    Map<String, Object> modelo = this.inicializarModelo(request,response);
    Usuario usuario = this.getUsuario(request);
    Organizacion organizacion = usuario.getOrganizacion();

    String string = request.queryParams("periodicidad");
    Periodicidad periodicidad = Periodicidad.valueOf(string);

    LocalDate fecha = LocalDate.parse(request.queryParams("fecha"));
    Plazo plazo = new Plazo(periodicidad, fecha);

    double hc = organizacion.obtenerHuellaCarbono(plazo);

    modelo.put("resultado", hc);

    return new ModelAndView(modelo, "resultadohc.html.hbs");
  }
}
