package medicion;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import javax.persistence.*;

@Entity
public class Medicion {

  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne
  @JoinColumn(name = "idTipoDeConsumo")
  private TipoDeConsumo tipoConsumo;
  private Double valor;
  @Enumerated(EnumType.STRING)
  private Periodicidad periodicidad;

  private LocalDate periodoDeImputacion;

  protected Medicion() {

  }

  public Medicion(TipoDeConsumo tipoConsumo, Double valor,
                  Periodicidad periodicidad, LocalDate periodoDeImputacion) {
    this.tipoConsumo = requireNonNull(tipoConsumo, "Debe especificar un tipo de consumo");
    this.valor = valor;
    this.periodicidad = requireNonNull(periodicidad, "Debe especificar la preiodicidad");
    this.periodoDeImputacion = requireNonNull(periodoDeImputacion,
        "Debe especificar un periodo de imputacion");
  }

  public TipoDeConsumo getTipoConsumo() {
    return this.tipoConsumo;
  }

  public Double getValor() {
    return this.valor;
  }

  public Periodicidad getPeriodicidad() {
    return this.periodicidad;
  }

  public LocalDate getPeriodoDeImputacion() {
    return this.periodoDeImputacion;
  }

  public double obtenerHuellaCarbono() {
    return this.valor * tipoConsumo.getFactorEmision().normalizarAKilogramos();
  }

  public Long getIdMedicion() {
    return this.id;
  }

}


