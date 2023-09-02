package csvreader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.EntityManager;
import medicion.Medicion;
import medicion.Periodicidad;
import medicion.TipoDeConsumo;
import org.uqbarproject.jpa.java8.extras.PerThreadEntityManagers;

public class CsvReader {
  private final String path;
  private static final String caracterDelimitante = ";";
  private List<String> contenidoCsv;

  public CsvReader(String path) {
    this.path = path;
  }

  public List<Medicion> extraerMediciones(){
    try {
      this.contenidoCsv = Files.readAllLines(
          Paths.get(this.path),
          StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new ArchivoCsvException(this.path);
    }

    this.validarHeader();
    return this.cargarCsv();
  }

  private List<Medicion> cargarCsv() {
    List<String> tipoDeConsumoStrings = new ArrayList<>();
    List<String> valorStrings = new ArrayList<>();
    List<String> periodicidadStrings = new ArrayList<>();
    List<String> periodoImputacionStrings = new ArrayList<>();

    for (int i = 0; i < contenidoCsv.size(); i++) {
      String[] linea = contenidoCsv.get(i).split(caracterDelimitante);

      if (linea.length != 4) {
        throw new RegistroCsvException(i + 1);
      }

      tipoDeConsumoStrings.add(linea[0]);
      valorStrings.add(linea[1].replaceAll("\\s", ""));
      periodicidadStrings.add(linea[2].replaceAll("\\s", ""));
      periodoImputacionStrings.add(linea[3].replaceAll("\\s", ""));
    }

    return
        this.generarMediciones(validarTipoDeConsumo(tipoDeConsumoStrings),
          validarValor(valorStrings),
          validarPeriodicidad(periodicidadStrings),
          validarPeriodoImputacion(periodoImputacionStrings));
  }

  private List<Medicion> generarMediciones(List<TipoDeConsumo> tiposDeConsumo, List<Double> valores,
                                           List<Periodicidad> periodicidades, List<LocalDate> periodosImp) {
    List<Medicion> mediciones = new ArrayList<>();

    for (int i = 0; i < tiposDeConsumo.size(); i++) {
      Medicion medicion = new Medicion(tiposDeConsumo.get(i), valores.get(i),
          periodicidades.get(i), periodosImp.get(i));

      mediciones.add(medicion);
    }

    return mediciones;
  }

  private void validarHeader() {
    String[] linea1 = contenidoCsv.get(0).split(caracterDelimitante);
    String[] linea2 = contenidoCsv.get(1).split(caracterDelimitante);

    if (linea1.length != 4 || linea2.length != 3) {
      throw new HeaderCsvException();
    }

    boolean valid =
        linea1[0].replaceAll("\\s", "").equalsIgnoreCase("tipodeconsumo")
        || linea1[1].replaceAll("\\s", "").equalsIgnoreCase("consumo")
        || linea1[2].replaceAll("\\s", "").equalsIgnoreCase("")
        || linea1[3].replaceAll("\\s", "").equalsIgnoreCase("periododeimputacion")
        || linea2[0].replaceAll("\\s", "").equalsIgnoreCase("")
        || linea2[1].replaceAll("\\s", "").equalsIgnoreCase("valor")
        || linea2[2].replaceAll("\\s", "").equalsIgnoreCase("periodicidad");

    if (!valid) {
      throw new HeaderCsvException();
    }

    contenidoCsv.remove(0);
    contenidoCsv.remove(0);
  }

  private List<TipoDeConsumo> validarTipoDeConsumo(List<String> tipoDeConsumoStrings) {
    List<TipoDeConsumo> tipoDeConsumo = new ArrayList<>();
    for (int i = 0; i < tipoDeConsumoStrings.size(); i++) {
      boolean asignado = false;

      EntityManager entityManager = PerThreadEntityManagers.getEntityManager();

      List<TipoDeConsumo> tiposDeConsumoValidos =  entityManager.createQuery(
          "SELECT tc FROM TipoDeConsumo tc").getResultList();

      for (int j = 0; j < tiposDeConsumoValidos.size(); j++) {
        TipoDeConsumo tipo = tiposDeConsumoValidos.get(j);

        if (tipoDeConsumoStrings.get(i).equalsIgnoreCase(tipo.getDescripcion())) {
          tipoDeConsumo.add(tipo);
          asignado = true;
          break;
        }
      }

      if (!asignado) {
        throw new CampoCsvException("Error de carga en el campo tipo de consumo "
            + "para el registro " + (i + 1) + ". " + tipoDeConsumoStrings.get(i) + " "
            + "no es un tipo de consumo existente.");
      }

    }

    return tipoDeConsumo;
  }

  private List<Double> validarValor(List<String> valorStrings) {
    List<Double> valor = new ArrayList<>();
    for (int i = 0; i < valorStrings.size(); i++) {

      try {
        double d = Double.parseDouble(valorStrings.get(i));
        valor.add(d);
      } catch (NumberFormatException nfe) {
        throw new CampoCsvException("Error de formato en el campo valor para "
            + "el registro de la linea " + (i + 1) + ". " + valorStrings.get(i) + " no "
            + "es un valor valido.");
      }
    }

    return valor;
  }

  private List<Periodicidad> validarPeriodicidad(List<String> periodicidadStrings) {
    List<Periodicidad> periodicidad = new ArrayList<>();
    for (int i = 0; i < periodicidadStrings.size(); i++) {
      boolean asignado = false;

      Periodicidad[] periodicidades = Periodicidad.values();

      for (int j = 0; j < periodicidades.length; j++) {
        if (periodicidades[j].getString().equalsIgnoreCase(periodicidadStrings.get(i))) {
          periodicidad.add(periodicidades[j]);
          asignado = true;
          break;
        }
      }

      if (!asignado) {
        throw new CampoCsvException("Error de carga en el campo periodicidad para "
            + "el registro " + (i + 1) + ". " + periodicidadStrings.get(i) + " no es "
            + "una periodicidad valida.");
      }
    }

    return periodicidad;
  }

  private List<LocalDate> validarPeriodoImputacion(List<String> periodoImputacionStrings) {
    List<LocalDate> periodoImputacion = new ArrayList<>();
    Pattern patternMesAnio = Pattern.compile("^[0-1]?[0-9]/[0-9]{4}$");
    Pattern patternSoloAnio = Pattern.compile("^[0-9]{4}$");

    for (int i = 0; i < periodoImputacionStrings.size(); i++) {
      Matcher matcherMesAnio = patternMesAnio.matcher(periodoImputacionStrings.get(i));
      Matcher matcherSoloAnio = patternSoloAnio.matcher(periodoImputacionStrings.get(i));

      if (matcherMesAnio.matches()) {
        String[] mesAnio = periodoImputacionStrings.get(i).split("/");
        int mes = Integer.parseInt(mesAnio[0]);
        int anio = Integer.parseInt(mesAnio[1]);

        if (mes < 1 || mes > 12) {
          throw new CampoCsvException("Error de carga en el campo periodo de imputacion "
              + "para el registro de la linea " + (i + 1) + ". " + mes + " no es un valor "
              + "valido para un mes. Min = 1 / Max = 12.");
        } else if (anio < 1900 || anio > 2100) {
          throw new CampoCsvException("Error de carga en el campo periodo de imputacion "
              + "para el registro de la linea " + (i + 1) + ". " + anio + " no es un valor "
              + "valido para el año. Min = 1900 / Max = 2100.");
        }

        periodoImputacion.add(LocalDate.of(anio, mes, 1));
      } else if (matcherSoloAnio.matches()) {
        int anio = Integer.parseInt(periodoImputacionStrings.get(i));

        if (anio < 1900 || anio > 2100) {
          throw new CampoCsvException("Error de carga en el campo periodo de imputacion "
              + "para el registro de la linea " + (i + 1) + ". " + anio + " no es un valor "
              + "valido para el año. Min = 1900 / Max = 2100.");
        }

        periodoImputacion.add(LocalDate.of(anio, 1, 1));

      } else {
        throw new CampoCsvException("Error de formato en el campo periodo de imputacion "
            + "para el registro de la linea " + (i + 1) + ". Los formatos posibles son MM/AAAA "
            + "para periodicidad mensual y AAAA para periodicidad anual.");
      }
    }

    return periodoImputacion;
  }
}
