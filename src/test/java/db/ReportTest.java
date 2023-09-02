package db;

import interesadosguiarecomendaciones.GestorDeServicios;
import medicion.*;
import medicion.reporte.Reportes;
import medicion.reporte.ReportesHC;
import miembro.Miembro;
import miembro.TipoDocumento;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;
import organizacion.*;
import organizacion.sectorterritorial.SectorTerritorial;
import serviciosgeolocalizacion.Geolocalizador;
import serviciosgeolocalizacion.ServicioGeolocalizacion;
import transporte.Combustible;
import transporte.mediosdetransporte.VehiculoParticular;
import transporte.tipostransportes.TipoVehiculo;
import trayectosytramos.Tramo;
import trayectosytramos.Trayecto;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReportTest extends AbstractPersistenceTest implements WithGlobalEntityManager {

  @BeforeEach
  void init() {
    super.setup();

    FactorEmision factor = new FactorEmision(UnidadHuellaCarbono.KgCO2eq, Unidad.M3, 0.3);

    TipoDeConsumo gasNatural = new TipoDeConsumo("Gas Natural",
        Unidad.M3, Actividad.COMBUSTION_FIJA, Alcance.EMISIONES_DIRECTAS,
        factor);
    Medicion medicion = new Medicion(gasNatural, 50000.0, Periodicidad.ANUAL, LocalDate.now());

    Organizacion org = new Organizacion("EmpresaA",
        TipoOrganizacion.EMPRESA, new Direccion(1, "maipu", 100),
        Clasificacion.EMPRESA_DEL_SECTOR_PRIMARIO);

    Organizacion orgB = new Organizacion("EmpresaB",
        TipoOrganizacion.ONG, new Direccion(1, "maipu", 100),
        Clasificacion.EMPRESA_DEL_SECTOR_PRIMARIO);

    org.agregarMedicion(medicion);
    orgB.agregarMedicion(medicion);

    Sector recursosHumanos = new Sector("Recursos Humanos", org);
    Sector recursosHumanos2 = new Sector("Recursos Humanos", orgB);

    SectorTerritorial sectorTerritorial = new SectorTerritorial("la pampa");
    SectorTerritorial sectorTerritorial2 = new SectorTerritorial("bs as");

    sectorTerritorial.agregarOrganizacion(orgB);
    sectorTerritorial.agregarOrganizacion(org);

    sectorTerritorial2.agregarOrganizacion(orgB);

    Miembro laurita = new Miembro("laurita", "fernandez",
        TipoDocumento.DNI, "557766889");

    SolicitudVinculacion solicitud = new SolicitudVinculacion(recursosHumanos, laurita);
    org.agregarSolicitudVinculacion(solicitud);
    org.aceptarSolicitudVinculacion(solicitud);

    SolicitudVinculacion solicitud2 = new SolicitudVinculacion(recursosHumanos2, laurita);
    orgB.agregarSolicitudVinculacion(solicitud2);
    orgB.aceptarSolicitudVinculacion(solicitud2);

    ServicioGeolocalizacion servicioGeolocalizacion = mock(ServicioGeolocalizacion.class);
    Geolocalizador geolocalizador = new Geolocalizador(servicioGeolocalizacion);

    GestorDeServicios.getInstance().setGeolocalizador(geolocalizador);

    Direccion direccionConstitucion = new Direccion(1, "maipu", 100);
    Direccion direccionIndependencia = new Direccion(1, "maipu", 300);

    when(geolocalizador.distancia(direccionConstitucion,direccionIndependencia)).thenReturn(25.0);

    VehiculoParticular autoAGnc = new VehiculoParticular(TipoVehiculo.AUTO, Combustible.GNC, 1.1);

    Tramo tramo = new Tramo(autoAGnc,direccionConstitucion, direccionIndependencia, laurita);

    Trayecto trayecto = new Trayecto("Ir a la oficina");

    trayecto.agregarTramos(tramo);

    laurita.agregarTrayecto(trayecto);

    entityManager().persist(gasNatural);
    entityManager().persist(medicion);
    entityManager().persist(trayecto);
    entityManager().persist(tramo);
    entityManager().persist(sectorTerritorial);
    entityManager().persist(sectorTerritorial2);
    entityManager().persist(org);
    entityManager().persist(orgB);
    entityManager().persist(recursosHumanos);
    entityManager().persist(recursosHumanos2);
    entityManager().persist(autoAGnc);

    entityManager().persist(laurita);
    entityManager().persist(solicitud);
    entityManager().persist(solicitud2);
  }

  @AfterEach
  public void onExit() {
    super.tearDown();
  }

  @Test
  void reporteHCTotalSectorTerritorial(){
    Periodicidad periodicidad =  Periodicidad.MENSUAL;
    LocalDate fecha = LocalDate.now();
    Plazo plazo = new Plazo(periodicidad, fecha);

    ReportesHC reportesHC = new ReportesHC();

    assertEquals(2,reportesHC.totalPorSectorTerritorial(plazo).size());
  }

  @Test
  void reporteHCTotalPorTipoOrganizacion(){
    Periodicidad periodicidad =  Periodicidad.MENSUAL;
    LocalDate fecha = LocalDate.now();
    Plazo plazo = new Plazo(periodicidad, fecha);

    ReportesHC reportesHC = new ReportesHC();

    reportesHC.totalPorTipoDeOrganizacion(plazo);
    assertEquals(2,2);
  }

  @Test
  void reporteComposicionHCSectorTerritorial(){
    Periodicidad periodicidad =  Periodicidad.MENSUAL;
    LocalDate fecha = LocalDate.now();
    Plazo plazo = new Plazo(periodicidad, fecha);

    ReportesHC reportesHC = new ReportesHC();

    SectorTerritorial sectorTerritorial2 = entityManager().createQuery(
        "SELECT s FROM SectorTerritorial s WHERE s.descripcion = 'bs as'",
        SectorTerritorial.class).getSingleResult();

    reportesHC.composicionTotalSectorTerritorial(sectorTerritorial2, plazo);
    assertEquals(2,2);
  }

  @Test
  void reporteComposicionHCOrganizacion(){
    Periodicidad periodicidad =  Periodicidad.MENSUAL;
    LocalDate fecha = LocalDate.now();
    Plazo plazo = new Plazo(periodicidad, fecha);

    ReportesHC reportesHC = new ReportesHC();

    Organizacion org = entityManager().createQuery(
        "SELECT o FROM Organizacion o WHERE o.razonSocial = 'EmpresaA'",
        Organizacion.class).getSingleResult();

    reportesHC.composicionTotalOrganizacion(org, plazo);
    assertEquals(3, reportesHC.composicionTotalOrganizacion(org, plazo).size());
  }

  @Test
  void reporteEvolucionHCSectorTerritorial(){
    Periodicidad periodicidad =  Periodicidad.MENSUAL;
    LocalDate fecha = LocalDate.now().minusMonths(12);
    Plazo plazo = new Plazo(periodicidad, fecha);

    ReportesHC reportesHC = new ReportesHC();

    SectorTerritorial sectorTerritorial2 = entityManager().createQuery(
        "SELECT s FROM SectorTerritorial s WHERE s.descripcion = 'bs as'",
        SectorTerritorial.class).getSingleResult();

    List<Reportes> resultadoReporte = reportesHC.
        evolucionHCSectorTerritorial(sectorTerritorial2, plazo);
    assertEquals(12,resultadoReporte.size());
  }

  @Test
  void reporteEvolucionHCOrganizacion(){
    Periodicidad periodicidad =  Periodicidad.MENSUAL;
    LocalDate fecha = LocalDate.now().minusMonths(12);
    Plazo plazo = new Plazo(periodicidad, fecha);

    ReportesHC reportesHC = new ReportesHC();

    Organizacion orgB = entityManager().createQuery(
        "SELECT o FROM Organizacion o WHERE o.razonSocial = 'EmpresaB'",
        Organizacion.class).getSingleResult();

    reportesHC.evolucionHCOrganizacion(orgB, plazo);
    assertEquals(2,2);
  }
}
