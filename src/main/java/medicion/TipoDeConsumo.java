package medicion;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class TipoDeConsumo {

  @Id
  @GeneratedValue
  private Long id;
  private String descripcion;

  @Enumerated(EnumType.STRING)
  @Column(name = "unidadTipoDeConsumo")
  private Unidad unidad;

  @Enumerated(EnumType.STRING)
  private Actividad actividad;
  @Enumerated(EnumType.STRING)
  private Alcance alcance;
  @Embedded
  private FactorEmision factorEmision;

  protected TipoDeConsumo() {

  }

  public TipoDeConsumo(String descripcion, Unidad unidad, Actividad actividad,
                       Alcance alcance, FactorEmision factorEmision) {
    this.descripcion = descripcion;
    this.unidad = unidad;
    this.actividad = actividad;
    this.alcance = alcance;
    setearFactorEmision(factorEmision);
  }

  public void setearFactorEmision(FactorEmision factor) {
    if (factor.getUnidadDenominador() != this.unidad) {
      throw new FactorEmisionAsociadoException();
    } else {
      this.factorEmision = factor;
    }
  }

  public FactorEmision getFactorEmision() {
    return this.factorEmision;
  }

  public String getDescripcion() {
    return this.descripcion;
  }

  public Unidad getUnidad() {
    return this.unidad;
  }
}