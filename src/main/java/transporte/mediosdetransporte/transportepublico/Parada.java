package transporte.mediosdetransporte.transportepublico;

import static java.util.Objects.requireNonNull;

import organizacion.Direccion;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Parada {

  @Id
  @GeneratedValue
  private Long id;

  private String nombre;

  @Embedded
  private Direccion direccion;
  private double distanciaProxima;

  protected Parada() {

  }

  public Parada(String nombre, Direccion direccion, double distancia) {
    this.nombre = requireNonNull(nombre,
        "Se debe especificar un nombre para la parada");
    this.direccion = requireNonNull(direccion,
        "Se debe especificar una ubicacion para la parada");
    this.distanciaProxima = requireNonNull(distancia,
        "Se debe especificar la distancia a la proxima parada");
  }

  public Direccion getDireccion() {
    return this.direccion;
  }

  public String getNombre() {
    return this.nombre;
  }

  public double getDistanciaProxima() {
    return this.distanciaProxima;
  }

  public void setDistanciaProxima(double distanciaProxima) {
    this.distanciaProxima = distanciaProxima;
  }

  public Long getId() {
    return this.id;
  }
}