package controller;

import csvreader.CsvReader;
import medicion.*;
import org.uqbarproject.jpa.java8.extras.PerThreadEntityManagers;
import organizacion.Organizacion;
import repositorios.RepositorioMediciones;
import repositorios.RepositorioUsuarios;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;
import usuario.RolUsuario;
import usuario.Usuario;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.staticFiles;

public class MedicionesController extends ControllerBase{


  public ModelAndView mostrarCargarMedicion(Request request, Response response) {
    Map<String, Object> modelo = this.inicializarModelo(request,response);
    Usuario usuario = this.getUsuario(request);

    EntityManager entity = PerThreadEntityManagers.getEntityManager();

    List<TipoDeConsumo> tiposDeConsumo = entity.createQuery(
        "SELECT T from TipoDeConsumo T", TipoDeConsumo.class).getResultList();

    modelo.put("tiposDeConsumo", tiposDeConsumo);
    modelo.put("mediciones", usuario.getOrganizacion().getMediciones());
    return new ModelAndView(modelo, "nuevaMedicion.html.hbs");
  }

  public ModelAndView listar(Request request, Response response){
    Map<String, Object> modelo = this.inicializarModelo(request,response);
    Usuario usuario = this.getUsuario(request);

    List<Medicion> mediciones = usuario.getOrganizacion().getMediciones();
    List<Map<String,String>> medicionesMostrar = new ArrayList<>();

    mediciones.forEach(medicion -> {
      Map<String,String> medicionMostrar = new HashMap<>();
      medicionMostrar.put("tipoConsumo",medicion.getTipoConsumo().getDescripcion());
      medicionMostrar.put("valor",medicion.getValor().toString());
      medicionMostrar.put("unidadTipoConsumo", medicion.getTipoConsumo().getUnidad().toString());
      medicionMostrar.put("periodicidad",medicion.getPeriodicidad().toString());
      medicionMostrar.put("periodoDeImputacion",medicion.getPeriodoDeImputacion().toString());

      medicionesMostrar.add(medicionMostrar);
    });

    modelo.put("mediciones", medicionesMostrar);

    return new ModelAndView(modelo, "mediciones.html.hbs");
  }

  public ModelAndView guardarMedicion(Request request, Response response) throws ServletException, IOException {

    EntityManager entityManager = PerThreadEntityManagers.getEntityManager();
    Usuario usuario = this.getUsuario(request);

    File uploadDir = new File("upload");
    uploadDir.mkdir(); // create the upload directory if it doesn't exist
    Path tempFile = Files.createTempFile(uploadDir.toPath(), "", "");

    request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));


    try (InputStream input = request.raw().getPart("archivoMediciones").getInputStream()) {// getPart needs to use same "name" as input field in form
      Files.copy(input, tempFile, StandardCopyOption.REPLACE_EXISTING);
      CsvReader reader = new CsvReader(tempFile.toAbsolutePath().toString());
      List<Medicion> mediciones = reader.extraerMediciones();
      Organizacion organizacion = usuario.getOrganizacion();
      mediciones.stream().forEach(medicion -> organizacion.agregarMedicionCsv(medicion));
    }catch(Exception e){
      TypedQuery<TipoDeConsumo> query = entityManager.createQuery(
          "SELECT t FROM TipoDeConsumo t WHERE t.descripcion = '" +
              request.queryParams("tipoDeConsumo") + "'", TipoDeConsumo.class);
      TipoDeConsumo tipo = query.getSingleResult();

      Medicion medicion = new Medicion(
          tipo,
          Double.parseDouble(request.queryParams("valor")),
          Periodicidad.valueOf(request.queryParams("periodicidad")),
          LocalDate.parse(request.queryParams("periodoDeImputacion")));

      // para persistir cuando ya sabemos que va
      usuario.getOrganizacion().agregarMedicion(medicion);
      RepositorioMediciones.getInstance().nuevaMedicion(medicion);
    }
    
    response.redirect("/mediciones");
    return null;
  }
}
