package transporte.mediosdetransporte;

import interesadosguiarecomendaciones.GestorDeServicios;
import organizacion.Direccion;
import transporte.Combustible;
import transporte.tipostransportes.TipoVehiculo;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@DiscriminatorValue("PARTICULAR")
public class VehiculoParticular extends MedioDeTransporte {

  @Enumerated(EnumType.STRING)
  private TipoVehiculo tipoVehiculo;
  @Enumerated(EnumType.STRING)
  private Combustible combustible;
  private double consumoPorKM;

  protected VehiculoParticular() {

  }

  public VehiculoParticular(TipoVehiculo tipoVehiculo, Combustible combustible,
                            double consumoPorKM) {

    this.tipoVehiculo = tipoVehiculo;
    this.combustible = combustible;
    this.consumoPorKM = consumoPorKM;

  }

  public Double getDistancia(Direccion puntoPartida, Direccion puntoLlegada) {
    return GestorDeServicios.getInstance().getGeolocalizador()
        .distancia(puntoPartida, puntoLlegada);
  }

  public TipoVehiculo getTipoVehiculo() {
    return this.tipoVehiculo;
  }

  public Combustible getTipoCombustible() {
    return this.combustible;
  }

  public Boolean esCompartible() {
    return true;
  }

  public String getDisplayName() {
    return (this.tipoVehiculo.toString() + " particular (" + this.combustible + ")").toUpperCase();
  }

  public double calcularHC(Double distancia) {
    return distancia * this.consumoPorKM;
  }
}
