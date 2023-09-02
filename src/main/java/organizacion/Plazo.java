package organizacion;


import java.time.LocalDate;
import java.time.LocalDateTime;
import medicion.Periodicidad;

public class Plazo {

  private Periodicidad periodicidad;
  private LocalDate fecha;

  public Plazo(Periodicidad periodicidad, LocalDate fecha) {
    this.periodicidad = periodicidad;
    this.fecha = fecha;
  }

  public Periodicidad getPeriodicidad() {
    return this.periodicidad;
  }

  public LocalDate getFecha() {
    return this.fecha;
  }
}
