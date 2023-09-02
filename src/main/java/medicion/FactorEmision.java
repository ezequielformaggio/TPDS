package medicion;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
public class FactorEmision {

  @Enumerated(EnumType.STRING)
  UnidadHuellaCarbono unidadNumerador;

  @Enumerated(EnumType.STRING)
  Unidad unidadDenominador;

  @Column(name = "coeficienteFactorEmision")
  double coeficiente;

  protected FactorEmision() {

  }

  public FactorEmision(UnidadHuellaCarbono unidadNumerador,
                       Unidad unidadDenominador, double coeficiente) {
    this.unidadNumerador = unidadNumerador;
    this.unidadDenominador = unidadDenominador;
    this.coeficiente = coeficiente;
  }

  public double getCoeficiente() {
    return this.coeficiente;
  }

  public Unidad getUnidadDenominador() {
    return this.unidadDenominador;
  }

  public UnidadHuellaCarbono getUnidadNumerador() {
    return this.unidadNumerador;
  }

  public void cambiarCoeficiente(double nuevoCoeficiente) {
    this.coeficiente = nuevoCoeficiente;
  }

  public double normalizarAKilogramos() {
    return this.unidadNumerador.normalizarAKilogramos(this.coeficiente);
  }
}
