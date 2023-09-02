package ejercicio;

import interesadosguiarecomendaciones.GestorDeServicios;
import miembro.Miembro;
import miembro.TipoDocumento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import organizacion.Clasificacion;
import organizacion.Direccion;
import organizacion.Organizacion;
import organizacion.Sector;
import organizacion.TipoOrganizacion;
import serviciosgeolocalizacion.Geolocalizador;
import serviciosgeolocalizacion.ServicioGeolocalizacion;
import transporte.*;
import transporte.mediosdetransporte.transportepublico.Parada;
import transporte.mediosdetransporte.TransporteEcologico;
import transporte.mediosdetransporte.transportepublico.TransportePublico;
import transporte.mediosdetransporte.VehiculoParticular;
import transporte.tipostransportes.TipoTransporteEcologico;
import transporte.tipostransportes.TipoTransportePublico;
import transporte.tipostransportes.TipoVehiculo;
import trayectosytramos.Tramo;
import trayectosytramos.Trayecto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TrayectosYTramosTest {
  private Miembro pepito;
  private Miembro jorge;
  private Direccion direccionConstitucion;
  private Direccion direccionIndependencia;
  private Direccion direccionDiagonalNorte;
  private Trayecto trayecto;
  private ServicioGeolocalizacion servicioGeolocalizacion;

  @BeforeEach
  void init() {
    pepito = new Miembro("pepito", "gonzalez", TipoDocumento.DNI, "445566778");
    jorge = new Miembro("jorge", "gonzalez", TipoDocumento.DNI, "445536778");
    trayecto = new Trayecto("Ir a la oficina");
    direccionConstitucion = new Direccion(1, "maipu", 100);
    direccionDiagonalNorte = new Direccion(457, "O'Higgins", 200);
    direccionIndependencia = new Direccion(1, "maipu", 300);

    servicioGeolocalizacion = mock(ServicioGeolocalizacion.class);
    Geolocalizador geolocalizador = new Geolocalizador(servicioGeolocalizacion);

    GestorDeServicios.getInstance().setGeolocalizador(geolocalizador);
  }

  @Test
  public void sePermiteLaCargaDeTramosCompartidos() {
    List<Miembro> miembros = new ArrayList<>();
    
    Organizacion organizacion1 = new Organizacion("EmpresaA",
        TipoOrganizacion.ONG, new Direccion(1, "maipu", 100),
        Clasificacion.EMPRESA_DEL_SECTOR_PRIMARIO); 
    
    Sector sector1 = new Sector("Finanzas", organizacion1);
    
    pepito.agregarSector(sector1);
    jorge.agregarSector(sector1);
    
    miembros.add(jorge);
    miembros.add(pepito);

    VehiculoParticular autoAGnc = new VehiculoParticular(TipoVehiculo.AUTO, Combustible.GNC, 1.1);
    Tramo tramo = new Tramo(autoAGnc,direccionConstitucion, direccionIndependencia, jorge);
    tramo.agregarMiembro(pepito);
    
    assertEquals(tramo.getMiembros(),miembros);
  }

  @Test
  public void verificarQuePepitoTengaElTrayectoCreadoEnSusTrayecto() {
    pepito.agregarTrayecto(trayecto);

    assertTrue(pepito.getTrayectos().contains(trayecto));
  }

  @Test
  public void sePuedeDarDeAltaUnTrayectoQueTengaVariosTramos() {

    Parada paradaConstitucion = new Parada("constitucion", direccionConstitucion, 0.1);
    Parada paradaDiagonalNorte = new Parada("diagonal norte", direccionDiagonalNorte, 0.1);
    Parada paradaIndependencia = new Parada("independencia", direccionIndependencia, 0.1);

    List<Parada> paradas = new ArrayList<>();
    paradas.add(paradaConstitucion);
    paradas.add(paradaDiagonalNorte);
    paradas.add(paradaIndependencia);

    TransportePublico subteLineaC = new TransportePublico(TipoTransportePublico.SUBTE,
        "Subte Linea C", paradas, 1.2);

    TransporteEcologico aPie = new TransporteEcologico(TipoTransporteEcologico.PIE);

    Tramo tramo1 = new Tramo(subteLineaC, paradaConstitucion, paradaDiagonalNorte, pepito);
    Tramo tramo2 = new Tramo(aPie, direccionDiagonalNorte, direccionIndependencia, pepito);

    trayecto.agregarTramos(tramo1);
    trayecto.agregarTramos(tramo2);

    assertTrue(trayecto.getTramos().size() > 0);
    assertTrue(trayecto.getTramos().contains(tramo1));
    assertTrue(trayecto.getTramos().contains(tramo2));
  }

  @Test
  public void sePermiteDarAConocerLaDistanciaDeUnTrayectoConTransportePublico() {

    Parada paradaConstitucion = new Parada("constitucion", direccionConstitucion, 0.1);
    Parada paradaDiagonalNorte = new Parada("diagonal norte", direccionDiagonalNorte, 0.1);
    Parada paradaIndependencia = new Parada("independencia", direccionIndependencia, 0.1);

    List<Parada> paradas = new ArrayList<>();
    paradas.add(paradaConstitucion);
    paradas.add(paradaDiagonalNorte);
    paradas.add(paradaIndependencia);

    TransportePublico subteLineaC = new TransportePublico(TipoTransportePublico.SUBTE,
        "Subte Linea C", paradas, 1.2);

    Tramo tramo1 = new Tramo(subteLineaC, paradaConstitucion, paradaDiagonalNorte, pepito);
    Tramo tramo2 = new Tramo(subteLineaC, paradaDiagonalNorte, paradaIndependencia, pepito);
    trayecto.agregarTramos(tramo1);
    trayecto.agregarTramos(tramo2);

    pepito.agregarTrayecto(trayecto);

    HashMap<Tramo, Double> hash = pepito.getTrayectos().get(0).
        getDistanciaPuntosIntermedios();

    assertEquals(tramo1.getDistancia(), hash.get(tramo1));
    assertEquals(tramo2.getDistancia(), hash.get(tramo2));
    assertEquals(0.2, pepito.getTrayectos().get(0).getDistanciaTotal());
    // verifico tambien que no consultó el servicio porque no lo necesita al ser transporte publico
    Mockito.verify(servicioGeolocalizacion, times(0)).distancia(any(), any());
  }

  @Test
  void sePermiteDarAConocerLaDistanciaDeUnTrayectoConTransporteEcologico() {

    double distanciaA = 20.0;
    double distanciaB = 15.0;

    TransporteEcologico bici = new TransporteEcologico(TipoTransporteEcologico.BICI);
    Tramo tramo1 = new Tramo(bici, direccionConstitucion, direccionDiagonalNorte, pepito);
    Tramo tramo2 = new Tramo(bici, direccionDiagonalNorte, direccionIndependencia, pepito);

    trayecto.agregarTramos(tramo1);
    trayecto.agregarTramos(tramo2);

    pepito.agregarTrayecto(trayecto);

    when(GestorDeServicios.getInstance().getGeolocalizador()
        .distancia(direccionConstitucion, direccionDiagonalNorte)).thenReturn(distanciaA);
    when(GestorDeServicios.getInstance().getGeolocalizador()
        .distancia(direccionDiagonalNorte, direccionIndependencia)).thenReturn(distanciaB);

    double distanciaTotal = pepito.getTrayectos().get(0).getDistanciaTotal();

    assertEquals(distanciaA + distanciaB, distanciaTotal);
  }
}