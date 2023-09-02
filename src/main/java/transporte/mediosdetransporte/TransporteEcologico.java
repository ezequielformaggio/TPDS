package transporte.mediosdetransporte;

import interesadosguiarecomendaciones.GestorDeServicios;
import organizacion.Direccion;
import transporte.tipostransportes.TipoTransporteEcologico;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@DiscriminatorValue("ECOLOGICO")
public class TransporteEcologico extends MedioDeTransporte {

  @Enumerated(EnumType.STRING)
  private TipoTransporteEcologico tipoTransporteEcologico;

  protected TransporteEcologico() {

  }

  public TransporteEcologico(TipoTransporteEcologico tipoTransporteEcologico) {
    this.tipoTransporteEcologico = tipoTransporteEcologico;
  }

  public Double getDistancia(Direccion puntoPartida, Direccion puntoLlegada) {
    return GestorDeServicios.getInstance().getGeolocalizador()
        .distancia(puntoPartida, puntoLlegada);
  }

  public TipoTransporteEcologico getTipoTransporteEcologico() {
    return this.tipoTransporteEcologico;
  }

  public Boolean esCompartible() {
    return false;
  }

  public double calcularHC(Double distancia) {
    return 0;
  }

  public String getDisplayName() {
    return this.tipoTransporteEcologico.toString().toUpperCase();
  }
}