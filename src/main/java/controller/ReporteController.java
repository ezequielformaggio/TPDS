package controller;

import medicion.Periodicidad;
import medicion.reporte.Reportes;
import medicion.reporte.ReportesHC;
import miembro.Miembro;
import org.eclipse.jetty.util.ajax.JSON;
import org.uqbarproject.jpa.java8.extras.PerThreadEntityManagers;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import organizacion.Organizacion;
import organizacion.Plazo;
import organizacion.SolicitudVinculacion;
import organizacion.sectorterritorial.SectorTerritorial;
import repositorios.RepositorioUsuarios;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import usuario.RolUsuario;
import usuario.Usuario;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ReporteController extends ControllerBase implements WithGlobalEntityManager {



  public ModelAndView mostrar(Request request, Response response) {
    Map<String, Object> modelo = this.inicializarModelo(request, response);
    Boolean esMiembro = (Boolean) modelo.get("esMiembro");

    if (esMiembro) {
      response.redirect("/");
      return null;
    } else {
      return new ModelAndView(modelo, "reportes/reportes.html.hbs");
    }

  }

  public ModelAndView composicion(Request request, Response response) {
    Map<String, Object> modelo = this.inicializarModelo(request, response);
    Boolean esMiembro = (Boolean) modelo.get("esMiembro");

    if (esMiembro) {
      response.redirect("/");
      return null;
    } else {
      return new ModelAndView(modelo, "reportes/composicion.html.hbs");
    }

  }

  public ModelAndView total(Request request, Response response) {
    Map<String, Object> modelo = this.inicializarModelo(request, response);
    Boolean esMiembro = (Boolean) modelo.get("esMiembro");

    if (esMiembro) {
      response.redirect("/");
      return null;
    } else {
      return new ModelAndView(modelo, "reportes/total.html.hbs");
    }

  }

  public ModelAndView evolucion(Request request, Response response) {
    Map<String, Object> modelo = this.inicializarModelo(request, response);
    Boolean esMiembro = (Boolean) modelo.get("esMiembro");

    if (esMiembro) {
      response.redirect("/");
      return null;
    } else {
      return new ModelAndView(modelo, "reportes/evolucion.html.hbs");
    }

  }

  public ModelAndView composicionTerritorial(Request request, Response response) {
    Map<String, Object> modelo = this.inicializarModelo(request, response);
    Usuario usuario = this.getUsuario(request);
    Miembro miembro = usuario.getMiembro();
    Boolean esMiembro = (Boolean) modelo.get("esMiembro");

    List<SectorTerritorial> sectoresTerritoriales = entityManager()
        .createQuery("SELECT s FROM SectorTerritorial s").getResultList();

    SectorTerritorial miSector = sectoresTerritoriales.stream()
        .filter(sectorTerritorial -> sectorTerritorial.getOrganizaciones()
            .contains(usuario.getOrganizacion())).collect(Collectors.toList()).get(0);

    String string = request.queryParams("periodicidad");
    Periodicidad periodicidad = Periodicidad.valueOf(string);

    LocalDate fecha = LocalDate.parse(request.queryParams("fecha"));
    Plazo plazo = new Plazo(periodicidad, fecha);

//    SectorTerritorial sectorTerritorial = new SectorTerritorial("Buenos aires");
//    sectorTerritorial.agregarOrganizacion(usuario.getOrganizacion());

    ReportesHC reportesHC = new ReportesHC();

    modelo.put("sectorTerritorial", miSector);
    modelo.put("valor1", reportesHC.composicionTotalSectorTerritorial(miSector,
        plazo).get(0));
    modelo.put("valor2", reportesHC.composicionTotalSectorTerritorial(miSector,
        plazo).get(1));
    modelo.put("valor3", reportesHC.composicionTotalSectorTerritorial(miSector,
        plazo).get(2));


    if (esMiembro) {
      response.redirect("/");
      return null;
    } else {
      return new ModelAndView(modelo, "reportes/composicionTerritorial.html.hbs");
    }

  }

  public ModelAndView composicionOrganizacion(Request request, Response response) {
    Map<String, Object> modelo = this.inicializarModelo(request, response);
    Usuario usuario = this.getUsuario(request);
    Miembro miembro = usuario.getMiembro();
    Boolean esMiembro = (Boolean) modelo.get("esMiembro");

    String string = request.queryParams("periodicidad");
    Periodicidad periodicidad = Periodicidad.valueOf(string);

    LocalDate fecha = LocalDate.parse(request.queryParams("fecha"));
    Plazo plazo = new Plazo(periodicidad, fecha);

    ReportesHC reportesHC = new ReportesHC();

    modelo.put("valor1", reportesHC.composicionTotalOrganizacion(usuario.getOrganizacion(),
        plazo).get(0));
    modelo.put("valor2", reportesHC.composicionTotalOrganizacion(usuario.getOrganizacion(),
        plazo).get(1));
    modelo.put("valor3", reportesHC.composicionTotalOrganizacion(usuario.getOrganizacion(),
        plazo).get(2));
    modelo.put("organizacion", usuario.getOrganizacion());


    if (esMiembro) {
      response.redirect("/");
      return null;
    } else {
      return new ModelAndView(modelo, "reportes/composicionOrganizacion.html.hbs");
    }
  }

  public ModelAndView totalOrganizacion(Request request, Response response) {
    Map<String, Object> modelo = this.inicializarModelo(request, response);
    Usuario usuario = this.getUsuario(request);
    Miembro miembro = usuario.getMiembro();
    Boolean esMiembro = (Boolean) modelo.get("esMiembro");

    String string = request.queryParams("periodicidad");
    Periodicidad periodicidad = Periodicidad.valueOf(string);

    LocalDate fecha = LocalDate.parse(request.queryParams("fecha"));
    Plazo plazo = new Plazo(periodicidad, fecha);

    ReportesHC reportesHC = new ReportesHC();

    modelo.put("reportes",reportesHC.totalPorTipoDeOrganizacion(plazo));



    if (esMiembro) {
      response.redirect("/");
      return null;
    } else {
      return new ModelAndView(modelo, "reportes/totalOrganizacion.html.hbs");
    }
  }

  public ModelAndView totalTerritorial(Request request, Response response) {
    Map<String, Object> modelo = this.inicializarModelo(request, response);
    Usuario usuario = this.getUsuario(request);
    Miembro miembro = usuario.getMiembro();
    Boolean esMiembro = (Boolean) modelo.get("esMiembro");

    String string = request.queryParams("periodicidad");
    Periodicidad periodicidad = Periodicidad.valueOf(string);

    LocalDate fecha = LocalDate.parse(request.queryParams("fecha"));
    Plazo plazo = new Plazo(periodicidad, fecha);

    ReportesHC reportesHC = new ReportesHC();

    HashMap<SectorTerritorial, Double> resultadoReporte = reportesHC.totalPorSectorTerritorial(plazo);

    List<SectorTerritorial> sectores = new ArrayList<>(resultadoReporte.keySet());
    List<Double> valores = new ArrayList<>(resultadoReporte.values());

    List<Reportes> reportes = new ArrayList<>();


    for(int i = 0; i < sectores.size(); i++) {
      Reportes resultado = new Reportes(sectores.get(i).getDescripcion(), valores.get(i), null, null, null);
      reportes.add(resultado);
    }


    modelo.put("reportes", reportes);


    if (esMiembro) {
      response.redirect("/");
      return null;
    } else {
      return new ModelAndView(modelo, "reportes/totalTerritorial.html.hbs");
    }
  }

  public ModelAndView evolucionOrganizacion(Request request, Response response) {
    Map<String, Object> modelo = this.inicializarModelo(request, response);
    Usuario usuario = this.getUsuario(request);
    Miembro miembro = usuario.getMiembro();
    Boolean esMiembro = (Boolean) modelo.get("esMiembro");

    String string = request.queryParams("periodicidad");
    Periodicidad periodicidad = Periodicidad.valueOf(string);

    LocalDate fecha = LocalDate.parse(request.queryParams("fecha"));
    Plazo plazo = new Plazo(periodicidad, fecha);

    ReportesHC reportesHC = new ReportesHC();

    modelo.put("reportes", reportesHC.evolucionHCOrganizacion(usuario.getOrganizacion(),plazo));


    if (esMiembro) {
      response.redirect("/");
      return null;
    } else {
      return new ModelAndView(modelo, "reportes/evolucionOrganizacion.html.hbs");
    }
  }

  public ModelAndView evolucionTerritorial(Request request, Response response) {
    Map<String, Object> modelo = this.inicializarModelo(request, response);
    Usuario usuario = this.getUsuario(request);
    Miembro miembro = usuario.getMiembro();
    Boolean esMiembro = (Boolean) modelo.get("esMiembro");

    String string = request.queryParams("periodicidad");
    Periodicidad periodicidad = Periodicidad.valueOf(string);

    LocalDate fecha = LocalDate.parse(request.queryParams("fecha"));
    Plazo plazo = new Plazo(periodicidad, fecha);

    List<SectorTerritorial> sectoresTerritoriales = entityManager()
        .createQuery("SELECT s FROM SectorTerritorial s").getResultList();

    SectorTerritorial miSector = sectoresTerritoriales.stream()
        .filter(sectorTerritorial -> sectorTerritorial.getOrganizaciones()
            .contains(usuario.getOrganizacion())).collect(Collectors.toList()).get(0);

    ReportesHC reportesHC = new ReportesHC();

    modelo.put("reportes", reportesHC.evolucionHCSectorTerritorial(miSector,plazo));


    if (esMiembro) {
      response.redirect("/");
      return null;
    } else {
      return new ModelAndView(modelo, "reportes/evolucionTerritorial.html.hbs");
    }
  }
}
