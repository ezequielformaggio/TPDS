package transporte.mediosdetransporte;

import organizacion.Direccion;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipoDeTransporte")
public abstract class MedioDeTransporte {

  @Id
  @GeneratedValue
  private Long id;

  public abstract Double getDistancia(Direccion puntoPartida, Direccion puntoLlegada);

  public abstract Boolean esCompartible();

  public abstract double calcularHC(Double distancia);

  public abstract String getDisplayName();

  public Long getId() {
    return this.id;
  }
}