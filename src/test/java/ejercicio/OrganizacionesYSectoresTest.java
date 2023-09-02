package ejercicio;

import miembro.Miembro;
import miembro.TipoDocumento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import organizacion.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrganizacionesYSectoresTest {
  private Organizacion org;
  private Sector sector;
  private Miembro pepito;

  @BeforeEach
  void init() {
    org = new Organizacion("EmpresaA",
        TipoOrganizacion.ONG, new Direccion(1, "maipu", 100),
        Clasificacion.EMPRESA_DEL_SECTOR_PRIMARIO);
    sector = new Sector("Finanzas", org);

    pepito = new Miembro("pepito", "gonzalez",
        TipoDocumento.DNI, "44556677");
  }

  @Test
  public void darDeAltaOrganizacion() {
    assertEquals("EmpresaA", org.getRazonSocial());
  }

  @Test
  public void darDeAltaUnSectorYAgregarloAUnaOrganizacion() {
    Sector recursosHumanos = new Sector("Recursos Humanos", org);

    assertTrue(org.getSectores().contains(recursosHumanos));
    assertTrue(org.getSectores().contains(sector));
  }

  @Test
  public void vincularMiembroConSector() {

    SolicitudVinculacion solicitudPepito = new SolicitudVinculacion(sector, pepito);
    org.agregarSolicitudVinculacion(solicitudPepito);

    assertFalse(sector.getMiembros().contains(pepito));
    assertTrue(org.getSolicitudes().contains(solicitudPepito));

    org.aceptarSolicitudVinculacion(solicitudPepito);

    assertTrue(sector.getMiembros().contains(pepito));
    assertFalse(org.getSolicitudes().contains(solicitudPepito));
  }
}
