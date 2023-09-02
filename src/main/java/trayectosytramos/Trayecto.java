package trayectosytramos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import organizacion.Organizacion;
import serviciosgeolocalizacion.ObtenerDistanciasException;

@Entity
public class Trayecto {

  @Id
  @GeneratedValue
  private Long idTrayecto;
  private String descripcion;

  @OneToMany
  @JoinColumn(name = "idTrayecto")
  private List<Tramo> tramos = new ArrayList<>();

  protected Trayecto() {

  }

  public Trayecto(String descripcion) {
    this.descripcion = descripcion;
  }

  public List<Tramo> getTramos() {
    return tramos;
  }

  public void agregarTramos(Tramo tramo) {
    tramos.add(tramo);
  }

  public void eliminarTramos (){
    tramos = null;
  }

  public double getDistanciaTotal() {
    return tramos.stream().mapToDouble(tramo -> {
      try {
        return tramo.getDistancia();
      } catch (Exception e) {
        throw new RuntimeException("Fallo al obtener distancia");
      }
    }).sum();
  }

  public HashMap<Tramo, Double> getDistanciaPuntosIntermedios() {
    HashMap<Tramo, Double> map = new HashMap<>();
    tramos.forEach(tramo -> {
      try {
        map.put(tramo, tramo.getDistancia());
      } catch (Exception e) {
        throw new ObtenerDistanciasException("Fallo al obtener distancia");
      }
    });
    return map;
  }

  public Double obtenerHuellaCarbono() {
    return this.tramos.stream().mapToDouble(tramo -> {
      try {
        return tramo.calcularHuella();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }).sum();
  }

  public boolean esDe(Organizacion organizacion) {
    return this.tramos.get(this.tramos.size() - 1).getDestino().esIgual(organizacion.getDireccion())
        || this.tramos.get(0).getOrigen().esIgual(organizacion.getDireccion());
  }

  public String getDescripcion() {
    return this.descripcion;
  }

  public Long getIdTrayecto() {
    return this.idTrayecto;
  }
}
