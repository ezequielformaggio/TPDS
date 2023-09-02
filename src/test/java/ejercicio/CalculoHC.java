package ejercicio;

import interesadosguiarecomendaciones.GestorDeServicios;
import medicion.*;
import miembro.Miembro;
import miembro.TipoDocumento;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import organizacion.*;
import organizacion.sectorterritorial.SectorTerritorial;
import serviciosgeolocalizacion.Geolocalizador;
import serviciosgeolocalizacion.ServicioGeolocalizacion;
import transporte.*;
import transporte.mediosdetransporte.TransporteEcologico;
import transporte.mediosdetransporte.VehiculoParticular;
import transporte.tipostransportes.TipoTransporteEcologico;
import transporte.tipostransportes.TipoVehiculo;
import trayectosytramos.Tramo;
import trayectosytramos.Trayecto;

import java.time.LocalDate;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CalculoHC {
  private Organizacion organizacionA;
  private Organizacion organizacionB;
  private Sector sector;
  private Miembro pepito;
  private Miembro jorge;
  private Direccion direccionConstitucion;
  private Direccion direccionIndependencia;
  private Direccion direccionDiagonalNorte;
  private Trayecto trayecto;
  private Trayecto trayecto2;
  private Medicion medicion;

  @BeforeEach
  void init() {
    organizacionA = new Organizacion("EmpresaA",
            TipoOrganizacion.ONG, new Direccion(1, "maipu", 100),
            Clasificacion.EMPRESA_DEL_SECTOR_PRIMARIO);

    organizacionB = new Organizacion("EmpresaB",
            TipoOrganizacion.ONG, new Direccion(1, "azcuenga", 100),
            Clasificacion.EMPRESA_DEL_SECTOR_SECUNDARIO);

    sector = new Sector("Finanzas", organizacionA);

    pepito = new Miembro("pepito", "gonzalez",
            TipoDocumento.DNI, "44556677");

    jorge = new Miembro("jorge", "perez",
        TipoDocumento.DNI, "44556676");

    sector.agregarMiembro(pepito);
    pepito.agregarSector(sector);

    trayecto = new Trayecto("Volver a casa");
    trayecto2 = new Trayecto("Volver a casa");

    direccionConstitucion = new Direccion(1, "maipu", 100);
    direccionDiagonalNorte = new Direccion(457, "O'Higgins", 200);
    direccionIndependencia = new Direccion(1, "maipu", 300);

    ServicioGeolocalizacion servicioGeolocalizacion = mock(ServicioGeolocalizacion.class);
    Geolocalizador geolocalizador = new Geolocalizador(servicioGeolocalizacion);

    GestorDeServicios.getInstance().setGeolocalizador(geolocalizador);

    FactorEmision factor = new FactorEmision(UnidadHuellaCarbono.KgCO2eq, Unidad.M3, 0.3);

    TipoDeConsumo gasNatural = new TipoDeConsumo("Gas Natural",
        Unidad.M3,Actividad.COMBUSTION_FIJA, Alcance.EMISIONES_DIRECTAS,
        factor);

    medicion = new Medicion(gasNatural, 30.0, Periodicidad.ANUAL, LocalDate.now());
  }

  @Test
  public void sePermiteElCalculoDeHCACadaOrganizacion() {
    Periodicidad periodicidad =  Periodicidad.ANUAL;
    LocalDate fecha = LocalDate.now();

    Plazo plazo = new Plazo(periodicidad, fecha);

    VehiculoParticular autoAGnc = new VehiculoParticular(TipoVehiculo.AUTO, Combustible.GNC, 1.1);
    Direccion direccion = new Direccion(1, "maipu", 100);
    Tramo tramo1 = new Tramo(autoAGnc,direccion, direccionDiagonalNorte, pepito);
    trayecto.agregarTramos(tramo1);
    pepito.agregarTrayecto(trayecto);

    when(GestorDeServicios.getInstance().getGeolocalizador()
        .distancia(direccion,direccionDiagonalNorte)).thenReturn(25.14);

    organizacionA.agregarMedicion(medicion);

    double huellaDeCarbono = organizacionA.obtenerHuellaCarbono(plazo);

    Assertions.assertEquals(6646, huellaDeCarbono, 0.1);
  }

  @Test
  public void huellaDeCarbonoDeUnMiembroConTransporteEcologicoEsCero(){
    TransporteEcologico bici = new TransporteEcologico(TipoTransporteEcologico.BICI);
    Tramo tramo1 = new Tramo(bici, direccionConstitucion, direccionDiagonalNorte, pepito);
    Tramo tramo2 = new Tramo(bici, direccionDiagonalNorte, direccionIndependencia, pepito);
    trayecto.agregarTramos(tramo1);
    trayecto.agregarTramos(tramo2);

    pepito.agregarTrayecto(trayecto);

    double huellaDeCarbono = pepito.obtenerHuellaCarbono(organizacionA,Periodicidad.MENSUAL);

    Assertions.assertEquals(0.0, huellaDeCarbono, 0.1);
  }

  @Test
  public void huellaDeCarbonoMiembroVehiculoParticular() {
    VehiculoParticular autoAGnc = new VehiculoParticular(TipoVehiculo.AUTO, Combustible.GNC, 1.1);
    Direccion direccion = new Direccion(1, "maipu", 100);
    Tramo tramo1 = new Tramo(autoAGnc,direccion, direccionDiagonalNorte, pepito);
    trayecto.agregarTramos(tramo1);
    pepito.agregarTrayecto(trayecto);

    when(GestorDeServicios.getInstance().getGeolocalizador()
        .distancia(direccion,direccionDiagonalNorte)).thenReturn(25.14);

    double huellaDeCarbono = pepito.obtenerHuellaCarbono(organizacionA,Periodicidad.MENSUAL);

    Assertions.assertEquals(553.08, huellaDeCarbono, 0.1);
  }

  @Test
  public void noSeSumaDosVecesLaHCDeUnTrayectoCompartido() {
    Periodicidad periodicidad =  Periodicidad.MENSUAL;
    LocalDate fecha = LocalDate.now();
    Plazo plazo = new Plazo(periodicidad, fecha);

    VehiculoParticular autoAGnc = new VehiculoParticular(TipoVehiculo.AUTO, Combustible.GNC, 2);
    Direccion direccion = new Direccion(1, "maipu", 100);
    Tramo tramo1 = new Tramo(autoAGnc,direccion, direccionDiagonalNorte, pepito);
    tramo1.agregarMiembro(jorge);
    trayecto.agregarTramos(tramo1);
    trayecto2.agregarTramos(tramo1);
    pepito.agregarTrayecto(trayecto);
    jorge.agregarSector(sector);
    sector.agregarMiembro(jorge);
    jorge.agregarTrayecto(trayecto2);

    when(GestorDeServicios.getInstance().getGeolocalizador()
        .distancia(direccion,direccionDiagonalNorte)).thenReturn(25.0);

    Assertions.assertEquals(1000, organizacionA.obtenerHuellaCarbono(plazo));
  }

  @Test
  public void sePermiteMostrarElImpactoDeUnMiembroSobreUnaOrganizacion() {
    Periodicidad periodicidad =  Periodicidad.ANUAL;
    LocalDate fecha = LocalDate.now();
    Plazo plazo = new Plazo(periodicidad, fecha);

    VehiculoParticular autoAGnc = new VehiculoParticular(TipoVehiculo.AUTO, Combustible.GNC, 1.1);
    Direccion direccion = new Direccion(1, "maipu", 100);
    Tramo tramo1 = new Tramo(autoAGnc,direccion, direccionDiagonalNorte, pepito);
    trayecto.agregarTramos(tramo1);
    pepito.agregarTrayecto(trayecto);

    when(GestorDeServicios.getInstance().getGeolocalizador()
        .distancia(direccion,direccionDiagonalNorte)).thenReturn(25.14);

    organizacionA.agregarMedicion(medicion);

    double numero = organizacionA.impactoMiembro(pepito,plazo);

    Assertions.assertEquals(99.86, numero, 0.1);
  }

  @Test
  public void sePermiteLaVisualizacionDelIndicadorDeHCParaUnSector() {
    VehiculoParticular autoAGnc = new VehiculoParticular(TipoVehiculo.AUTO, Combustible.GNC, 2.0);
    Direccion direccion = new Direccion(1, "maipu", 100);
    Tramo tramo1 = new Tramo(autoAGnc,direccion, direccionDiagonalNorte, pepito);
    Tramo tramo2 = new Tramo(autoAGnc,direccion, direccionConstitucion, jorge);
    trayecto.agregarTramos(tramo1);
    trayecto2.agregarTramos(tramo2);
    pepito.agregarTrayecto(trayecto);
    jorge.agregarTrayecto(trayecto2);
    jorge.agregarSector(sector);
    sector.agregarMiembro(jorge);

    when(GestorDeServicios.getInstance().getGeolocalizador()
        .distancia(direccion,direccionDiagonalNorte)).thenReturn(20.0);
    when(GestorDeServicios.getInstance().getGeolocalizador()
        .distancia(direccion,direccionConstitucion)).thenReturn(30.0);

    double indicador = sector.indicadorHC(Periodicidad.MENSUAL);

    Assertions.assertEquals(1000, indicador, 0.1);
  }

  @Test
  public void sePermiteLaCreacionDeSectoresTerritorialesYElCalculoDeSuHC() {
    Periodicidad periodicidad =  Periodicidad.MENSUAL;
    LocalDate fecha = LocalDate.now();
    Plazo plazo = new Plazo(periodicidad, fecha);

    Sector sector2 = new Sector("Sistemas", organizacionB);

    jorge.agregarSector(sector2);
    sector2.agregarMiembro(jorge);

    VehiculoParticular autoAGnc = new VehiculoParticular(TipoVehiculo.AUTO, Combustible.GNC, 2.0);
    Direccion direccion = new Direccion(1, "maipu", 100);
    Direccion direccion2 = new Direccion(1, "azcuenga", 100);
    Tramo tramo1 = new Tramo(autoAGnc,direccion, direccionDiagonalNorte, pepito);
    Tramo tramo2 = new Tramo(autoAGnc,direccion2, direccionConstitucion, jorge);
    trayecto.agregarTramos(tramo1);
    trayecto2.agregarTramos(tramo2);
    pepito.agregarTrayecto(trayecto);
    jorge.agregarTrayecto(trayecto2);

    when(GestorDeServicios.getInstance().getGeolocalizador()
        .distancia(direccion,direccionDiagonalNorte)).thenReturn(20.0);
    when(GestorDeServicios.getInstance().getGeolocalizador()
        .distancia(direccion2,direccionConstitucion)).thenReturn(30.0);

    SectorTerritorial sectorTerritorial = new SectorTerritorial("Sector Norte");
    sectorTerritorial.agregarOrganizacion(organizacionA);
    sectorTerritorial.agregarOrganizacion(organizacionB);

    Assertions.assertEquals(2000, sectorTerritorial.calcularHC(plazo));
  }
}
