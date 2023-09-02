package transporte.mediosdetransporte.transportepublico;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import organizacion.Direccion;
import transporte.mediosdetransporte.MedioDeTransporte;
import transporte.tipostransportes.TipoTransportePublico;

import javax.persistence.*;

@Entity
@DiscriminatorValue("PUBLICO")
public class TransportePublico extends MedioDeTransporte {

  @Enumerated(EnumType.STRING)
  private TipoTransportePublico tipoTransportePublico;
  private String linea;
  @OneToMany
  @JoinColumn(name = "idMedioDeTransporte")
  private List<Parada> paradas;
  private double consumoPorKM;

  protected TransportePublico() {

  }

  public TransportePublico(TipoTransportePublico tipo, String linea,
                           List<Parada> paradas, double consumoPorKM) {
    this.tipoTransportePublico = tipo;
    this.linea = requireNonNull(linea,
        "Se debe especificar un nombre para la linea de transporte");
    this.paradas = requireNonNull(paradas,
        "Se debe especificar una lista de paradas para la linea de transporte");
    this.consumoPorKM = consumoPorKM;
  }

  public void agregarParada(Parada parada, double nuevaDistancia) {
    this.paradas.get(this.paradas.size() - 1).setDistanciaProxima(nuevaDistancia);
    this.paradas.add(parada);
  }

  public Double getDistancia(Direccion puntoPartida, Direccion puntoLlegada) {
    // acá calculas con las paradas
    List<Parada> paradasDeTramo = new ArrayList<>();
    boolean agregar = false;

    for (int i = 0; i < this.paradas.size(); i++) {
      if (this.paradas.get(i).getDireccion().equals(puntoPartida)) {
        agregar = true;
      } else if (this.paradas.get(i).getDireccion().equals(puntoLlegada)) {
        break;
      }

      if (agregar) {
        paradasDeTramo.add(this.paradas.get(i));
      }
    }

    return paradasDeTramo.stream().mapToDouble(parada -> parada.getDistanciaProxima()).sum();

  }

  public String getLinea() {
    return linea;
  }

  public TipoTransportePublico getTipoTransportePublico() {
    return this.tipoTransportePublico;
  }

  public List<Parada> getParadas() {
    return this.paradas;
  }

  public Boolean esCompartible() {
    return false;
  }

  public double calcularHC(Double distancia) {
    return distancia * this.consumoPorKM;
  }

  public String getDisplayName() {
    return (this.tipoTransportePublico.toString() + " - " + this.linea)
        .toUpperCase();
  }
}