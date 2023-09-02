package serviciosgeolocalizacion;

import organizacion.Direccion;

public class Geolocalizador {

  private ServicioGeolocalizacion servicioGeolocalizacion;

  public Geolocalizador(ServicioGeolocalizacion servicioGeolocalizacion) {
    this.servicioGeolocalizacion = servicioGeolocalizacion;
  }

  public Double distancia(Direccion origen, Direccion destino) {
    return this.servicioGeolocalizacion.distancia(origen, destino);
  }
}
