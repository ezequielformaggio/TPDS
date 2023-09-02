package ejercicio;

import interesadosguiarecomendaciones.GestorDeServicios;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import organizacion.Direccion;
import serviciosgeolocalizacion.Geolocalizador;
import serviciosgeolocalizacion.serviciogeoddstpa.ServicioGeoDsTpa;

public class ApiDistanciaResponseTest {

  Direccion direccionConstitucion;
  Direccion direccionDiagonalNorte;

  @BeforeEach
  public void onInit() {
    ServicioGeoDsTpa servicioGeoDsTpa = new ServicioGeoDsTpa();
    Geolocalizador geolocalizador = new Geolocalizador(servicioGeoDsTpa);

    GestorDeServicios.getInstance().setGeolocalizador(geolocalizador);

    direccionConstitucion = new Direccion(1, "maipu", 100);
    direccionDiagonalNorte = new Direccion(457, "O'Higgins", 200);
  }

  @Test
  public void testResponse() {
    double valor = -0.1;

    valor = GestorDeServicios.getInstance().getGeolocalizador()
        .distancia(direccionConstitucion, direccionDiagonalNorte);

    Assertions.assertNotEquals(-0.1, valor);
  }

}