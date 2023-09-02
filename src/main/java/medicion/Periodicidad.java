package medicion;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public enum Periodicidad {
  MENSUAL {
    @Override
    public String getString() {
      return "MENSUAL";
    }

    @Override
    public LocalDate siguiente(LocalDate fecha) {
      return  fecha.plusMonths(1);
    }
    @Override
    public double getHCTrayectosSegunPeriodicidad(double HC, int diasHabiles){
      return HC * diasHabiles / 12;
    }
    @Override
    public double getHCOrganizacionSegunPeriodicidad(List<Medicion> mediciones, LocalDate fecha) {
      List<Medicion> medicionesAnuales = mediciones.stream()
          .filter(medicion -> medicion.getPeriodoDeImputacion().getYear() == fecha.getYear()
              && medicion.getPeriodicidad().equals(Periodicidad.ANUAL))
          .collect(Collectors.toList());

      List<Medicion> medicionesMensuales = mediciones.stream()
          .filter(medicion -> medicion.getPeriodoDeImputacion()
              .getYear() == fecha.getYear() && medicion.getPeriodoDeImputacion()
              .getMonthValue() == fecha.getMonthValue() && medicion.getPeriodicidad()
              .equals(Periodicidad.MENSUAL)).collect(Collectors.toList());

      double hcMedicionesAnuales = medicionesAnuales.stream()
          .mapToDouble(medicion -> medicion.obtenerHuellaCarbono()).sum();

      double hcMedicionesMensuales = medicionesMensuales.stream()
          .mapToDouble(medicion -> medicion.obtenerHuellaCarbono()).sum();

      return hcMedicionesAnuales / 12 + hcMedicionesMensuales;
    }
  },
  ANUAL {
    @Override
    public String getString() {
      return "ANUAL";
    }

    @Override
    public LocalDate siguiente(LocalDate fecha) {
      return  fecha.plusYears(1);
    }

    @Override
    public double getHCTrayectosSegunPeriodicidad(double HC, int diasHabiles){
      return HC * diasHabiles;
    }

    @Override
    public double getHCOrganizacionSegunPeriodicidad(List<Medicion> mediciones, LocalDate fecha) {
      List<Medicion> medicionesTotales = mediciones.stream()
          .filter(medicion -> medicion.getPeriodoDeImputacion().getYear() == fecha.getYear())
          .collect(Collectors.toList());

      double HCMedicionesTotales = medicionesTotales.stream()
          .mapToDouble(medicion -> medicion.obtenerHuellaCarbono()).sum();

      return HCMedicionesTotales;
    }
  };

  public abstract String getString();
  public abstract double getHCTrayectosSegunPeriodicidad(double HC, int diasHabiles);
  public abstract double getHCOrganizacionSegunPeriodicidad(List<Medicion> mediciones, LocalDate fecha);

  public abstract LocalDate siguiente(LocalDate fecha);
}
