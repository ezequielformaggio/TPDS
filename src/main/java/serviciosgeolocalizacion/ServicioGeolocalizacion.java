package serviciosgeolocalizacion;

import java.io.IOException;
import organizacion.Direccion;

public interface ServicioGeolocalizacion {
  public Double distancia(Direccion origen, Direccion destino);
}
