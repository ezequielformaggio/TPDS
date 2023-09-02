package main;

import controller.SesionController;
import controller.VinculacionController;

import controller.*;

import org.uqbarproject.jpa.java8.extras.PerThreadEntityManagers;
import spark.Spark;
import spark.template.handlebars.HandlebarsTemplateEngine;

import javax.persistence.PersistenceException;

public class Routes {

  @SuppressWarnings("checkstyle:CommentsIndentation")
  public static void main(String[] args) {
    System.out.println("Iniciando servidor");

    // no queremos que genere cosas nuevas, que trabaje con lo que hay
     Bootstrap.main();

    Spark.port(8080);
    Spark.staticFileLocation("/public");

    HandlebarsTemplateEngine engine = new HandlebarsTemplateEngine();
    SesionController sesionController = new SesionController();

    VinculacionController vinculacionController = new VinculacionController();

    MedicionesController medicionesController = new MedicionesController();
    TramosController tramosController = new TramosController();
    TrayectosController trayectosController = new TrayectosController();
    CalculadoraController calculadoraController = new CalculadoraController();
    RecomendacionesController recomendacionesController = new RecomendacionesController();

    ReporteController reporteController = new ReporteController();

    /*-----------------------RECOMENDACIONES-----------------------------*/
    Spark.get("/recomendaciones", recomendacionesController::mostrar, engine);
    Spark.get("/", recomendacionesController::mostrar, engine);
    /*-----------------------SESION-----------------------------*/
    //Spark.get("/", inicioController::mostrar, engine); POR ESTA ENTREGA QUEDA INHABILITADA
    Spark.post("/login", sesionController::crearSesion);
    Spark.get("/login", sesionController::mostrarLogin, engine);
    Spark.get("/logout", sesionController::logout);

    Spark.post("/registro", sesionController::crearRegistro);
    Spark.get("/registro", sesionController::mostrarFormularioRegistro, engine);

    /*-----------------------VINCULACIONES-----------------------------*/
    Spark.post("/vinculaciones/nueva", vinculacionController::solicitarVinculacion);
    Spark.post("/vinculaciones", vinculacionController::decisionVinculacion);
    Spark.get("/vinculaciones", vinculacionController::listar, engine);


    /*-----------------------MEDICIONES-----------------------------*/
    Spark.get("/mediciones", medicionesController::listar, engine);
    Spark.get("/mediciones/nueva", medicionesController::mostrarCargarMedicion, engine);
    Spark.post("/mediciones/nueva", medicionesController::guardarMedicion);

    /*-----------------------TRAYECTOS-----------------------------*/
    Spark.get("/trayectos", trayectosController::listarTrayectos, engine);
    Spark.get("/trayectos/nuevo", trayectosController::mostrarFormularioTrayectos, engine);
    Spark.post("/trayectos", trayectosController::crearTrayecto, engine);
    Spark.get("/trayectos/:idTrayecto", trayectosController::verTrayecto, engine);

    Spark.get("/trayectos/:idTrayecto/tramos", tramosController::listarTramos, engine);
    Spark.post("/trayectos/:idTrayecto/tramos", tramosController::crearTramo, engine);
    Spark.get("/trayectos/:idTrayecto/tramos/medioTransporte", tramosController::mostrarFormularioMedioTransporte, engine);
    Spark.get("/trayectos/:idTrayecto/tramos/nuevo", tramosController::mostrarFormularioTramos, engine);
    Spark.get("/trayectos/:idTrayecto/tramos/:idTramo", tramosController::verTramo, engine);


    /*-----------------------CALCULADORA-----------------------------*/
    Spark.get("/calculadora", calculadoraController::listar, engine);
    Spark.get("/calculadora/hcmiembro", calculadoraController::calcularHCMiembro, engine);
    Spark.get("/calculadora/hcorganizacion", calculadoraController::calcularHCOrganizacion, engine);

    /*-----------------------REPORTES-----------------------------*/
    Spark.get("/reportes", reporteController::mostrar, engine);
    Spark.get("/reportes/composicion", reporteController::composicion, engine);
    Spark.get("/reportes/total", reporteController::total, engine);
    Spark.get("/reportes/evolucion", reporteController::evolucion, engine);
                      /*---reportes territoriales--*/
    Spark.post("/reportes/composicion/territorial", reporteController::composicionTerritorial, engine);
    Spark.post("/reportes/total/territorial", reporteController::totalTerritorial, engine);
    Spark.post("/reportes/evolucion/territorial", reporteController::evolucionTerritorial, engine);
                      /*---reportes organizacion--*/
    Spark.post("/reportes/composicion/organizacion", reporteController::composicionOrganizacion, engine);
    Spark.post("/reportes/total/organizacion", reporteController::totalOrganizacion, engine);
    Spark.post("/reportes/evolucion/organizacion", reporteController::evolucionOrganizacion, engine);


    /*-----------------------LOGICA GENERAL A VARIAS RUTAS-----------------------------*/
    Spark.exception(PersistenceException.class, (e, request, response) -> {
      response.redirect("/500"); //TODO
    });

    Spark.before((request, response) -> {
      if(!(request.pathInfo().startsWith("/login")) & !(request.pathInfo().startsWith("/registro")) &
              request.session().attribute("user_id") == null) {
        response.redirect("/login");
      }
    });

    Spark.after((request, response) -> {
      PerThreadEntityManagers.getEntityManager().clear();
    });
  }

}
