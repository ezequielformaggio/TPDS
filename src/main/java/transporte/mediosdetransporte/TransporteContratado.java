package transporte.mediosdetransporte;

import static java.util.Objects.requireNonNull;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import interesadosguiarecomendaciones.GestorDeServicios;
import organizacion.Direccion;

@Entity
@DiscriminatorValue("CONTRATADO")
public class TransporteContratado extends MedioDeTransporte {

  private String nombre;
  private double consumoPorKM;

  protected TransporteContratado() {

  }

  public TransporteContratado(String nombre, double consumoPorKM) {
    this.nombre = requireNonNull(nombre,
        "Se debe especificar un nombre para el servicio contratado");
    this.consumoPorKM = consumoPorKM;
  }

  public Double getDistancia(Direccion puntoPartida, Direccion puntoLlegada) {
    return GestorDeServicios.getInstance().getGeolocalizador()
        .distancia(puntoPartida, puntoLlegada);
  }

  public String getNombre() {
    return this.nombre;
  }

  public Boolean esCompartible() {
    return true;
  }

  public double calcularHC(Double distancia) {
    return distancia * this.consumoPorKM;
  }

  public String getDisplayName() {
    return this.nombre.toUpperCase();
  }

}
