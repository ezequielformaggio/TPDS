package organizacion;

import javax.persistence.Embeddable;

@Embeddable
public class Direccion {

  private int localidad;
  private String calle;
  private int altura;

  protected Direccion() {

  }

  public Direccion(int idLocalidad, String calle, int altura) {
    this.localidad = idLocalidad;
    this.calle = calle;
    this.altura = altura;
  }

  public void setLocalidad(int localidad) {
    this.localidad = localidad;
  }

  public int getLocalidad() {
    return localidad;
  }

  public void setCalle(String calle) {
    this.calle = calle;
  }

  public String getCalle() {
    return calle;
  }

  public void setAltura(int altura) {
    this.altura = altura;
  }

  public int getAltura() {
    return altura;
  }

  public boolean esIgual(Direccion direccion) {
    return this.localidad == direccion.getLocalidad() && this.altura == direccion.getAltura()
            && this.calle.equals(direccion.getCalle());
  }
}
