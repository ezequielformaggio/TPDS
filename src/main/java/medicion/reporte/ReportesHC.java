package medicion.reporte;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import medicion.Periodicidad;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import organizacion.*;
import organizacion.sectorterritorial.SectorTerritorial;

public class ReportesHC implements WithGlobalEntityManager {

  public HashMap<SectorTerritorial, Double>  totalPorSectorTerritorial(Plazo plazo) {

    List<SectorTerritorial> sectoresTerritoriales = entityManager()
        .createQuery("SELECT s FROM SectorTerritorial s").getResultList();

    HashMap<SectorTerritorial, Double> resultadoReporte = new HashMap<>();


    for (SectorTerritorial sector : sectoresTerritoriales) {
      resultadoReporte.put(sector, sector.calcularHC(plazo));
      /*System.out.println("Sector: " + sector.getidSectorTerritorial().toString()
          + " HC: " + Double.toString(sector.calcularHC(plazo)));
    }*/
    }
    return resultadoReporte;
  }

  public  List<Reportes>  totalPorTipoDeOrganizacion(Plazo plazo) {
    double hctotal = 0;

    List<Reportes> reportes = new ArrayList<>();



     List<Organizacion> organizaciones = entityManager()
            .createQuery("SELECT o FROM Organizacion o WHERE o.tipoOrg ="
                + " organizacion.TipoOrganizacion.GUBERNAMENTAL", Organizacion.class).getResultList();

    hctotal = organizaciones.stream()
        .mapToDouble(organizacion -> organizacion.obtenerHuellaCarbono(plazo)).sum();


    Reportes reportes1 = new Reportes(null, hctotal, null, TipoOrganizacion.GUBERNAMENTAL, null);
    reportes.add(reportes1);

    organizaciones = entityManager()
        .createQuery("SELECT o FROM Organizacion o WHERE o.tipoOrg = "
            + "organizacion.TipoOrganizacion.ONG", Organizacion.class).getResultList();

    hctotal = organizaciones.stream()
        .mapToDouble(organizacion -> organizacion.obtenerHuellaCarbono(plazo)).sum();

    Reportes reportes2 = new Reportes(null, hctotal, null, TipoOrganizacion.ONG, null);
    reportes.add(reportes2);

    organizaciones = entityManager()
        .createQuery("SELECT o FROM Organizacion o WHERE o.tipoOrg ="
            + " organizacion.TipoOrganizacion.EMPRESA", Organizacion.class).getResultList();

    hctotal = organizaciones.stream()
        .mapToDouble(organizacion -> organizacion.obtenerHuellaCarbono(plazo)).sum();

    Reportes reportes3 = new Reportes(null, hctotal, null, TipoOrganizacion.EMPRESA, null);
    reportes.add(reportes3);


    organizaciones = entityManager()
        .createQuery("SELECT o FROM Organizacion o WHERE o.tipoOrg = "
            + "organizacion.TipoOrganizacion.INSTITUCION", Organizacion.class).getResultList();

    hctotal = organizaciones.stream()
        .mapToDouble(organizacion -> organizacion.obtenerHuellaCarbono(plazo)).sum();

    Reportes reportes4 = new Reportes(null, hctotal, null, TipoOrganizacion.INSTITUCION, null);
    reportes.add(reportes4);

    return reportes;
  }

  public List<Double> composicionTotalSectorTerritorial(SectorTerritorial sectorTerritorial, Plazo plazo) {

    double hcMiembros = sectorTerritorial.calcularHCMiembros(plazo);

    double hcOrg = sectorTerritorial.calcularHCMediciones(plazo);

    double hcSector = sectorTerritorial.calcularHC(plazo);

    List<Double> resultadoReporte = new ArrayList<>();

    resultadoReporte.add(hcMiembros);
    resultadoReporte.add(hcOrg);
    resultadoReporte.add(hcSector);

    /*System.out.println("Emision total: " + Double.toString(hcSector)
            + " %HC miembros: " + Double.toString(calcularPorcentaje(hcMiembros, hcSector))
            + " %HC emisiones org: " + Double.toString(calcularPorcentaje(hcOrg, hcSector)));*/

    return resultadoReporte;
  }

  public List<Double> composicionTotalOrganizacion(Organizacion  organizacion, Plazo plazo) {

    double hcMiembros = organizacion.obtenerHuellaCarbonoMiembros(plazo);

    double hcOrg = organizacion.obtenerHuellaCarbonoMedicionesOrg(plazo);

    double hc = organizacion.obtenerHuellaCarbono(plazo);

    List<Double> resultadoReporte = new ArrayList<>();

    resultadoReporte.add(hc);
    resultadoReporte.add(calcularPorcentaje(hcMiembros, hc));
    resultadoReporte.add(calcularPorcentaje(hcOrg, hc));

    /*System.out.println("Emision total: " + Double.toString(hc)
            + " %HC miembros: " + Double.toString(calcularPorcentaje(hcMiembros, hc))
            + " %HC emisiones org: " + Double.toString(calcularPorcentaje(hcOrg, hc))
    );*/
    return resultadoReporte;
  }

  public List<Reportes> evolucionHCSectorTerritorial(SectorTerritorial sector,
                                                                 Plazo plazo) {
    LocalDate fecha = plazo.getFecha();
    LocalDate hoy = LocalDate.now();
    Periodicidad periodicidad = plazo.getPeriodicidad();

    List<Reportes> reportes = new ArrayList<>();

    while (fecha.isBefore(hoy)) {
      Reportes reporte = new Reportes(null,
          sector.calcularHC(new Plazo(periodicidad, fecha)), null, null, fecha.toString());

      reportes.add(reporte);

      fecha = periodicidad.siguiente(fecha);
    }
    return reportes;
  }

  public  List<Reportes>  evolucionHCOrganizacion(Organizacion organizacion, Plazo plazo) {
    LocalDate fecha = plazo.getFecha();
    LocalDate hoy = LocalDate.now();
    Periodicidad periodicidad = plazo.getPeriodicidad();

    List<Reportes> reportes = new ArrayList<>();

    while (fecha.isBefore(hoy)) {

      Reportes reporte = new Reportes(null,
          organizacion.obtenerHuellaCarbono(new Plazo(periodicidad, fecha)), null, null, fecha.toString());

      reportes.add(reporte);

      fecha = periodicidad.siguiente(fecha);
    }
    return reportes;
  }

  private double calcularPorcentaje(double numero, double total) {
    return Math.round(numero * 100 / total);
  }

}
