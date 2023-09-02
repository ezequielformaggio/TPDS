package ejercicio;

import medicion.*;
import org.junit.jupiter.api.Test;
import organizacion.Clasificacion;
import organizacion.Direccion;
import organizacion.Organizacion;
import organizacion.TipoOrganizacion;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MedicionesTest {

  @Test
  public void seDebePermitirLaCargaDeMedicionesPorParteDeUnaOrganizacion() {
    Organizacion org = new Organizacion("EmpresaA",
            TipoOrganizacion.ONG, new Direccion(1, "maipu", 100),
            Clasificacion.EMPRESA_DEL_SECTOR_PRIMARIO);

    FactorEmision factor = new FactorEmision(UnidadHuellaCarbono.KgCO2eq, Unidad.M3, 0.3);

    TipoDeConsumo gasNatural = new TipoDeConsumo("Gas Natural",
            Unidad.M3,Actividad.COMBUSTION_FIJA, Alcance.EMISIONES_DIRECTAS,
            factor);

    Medicion medicion = new Medicion(gasNatural, 30.0, Periodicidad.ANUAL, LocalDate.now());

    org.agregarMedicion(medicion);

    assertEquals(1, org.getMediciones().size());
  }

  @Test
  public void unFactorDeEmisionNoDebeTenerDistintasUnidadesQueSuTipoDeConsumoAsociado() {
    FactorEmision factor = new FactorEmision(UnidadHuellaCarbono.GrCO2eq, Unidad.KG, 0.4);

    assertEquals("Las unidades del factor de emision asociado no se corresponden"
                    + "con las del tipo de consumo",
            assertThrows(FactorEmisionAsociadoException.class, () ->
                    new TipoDeConsumo("Gas Natural",
                            Unidad.M3,Actividad.COMBUSTION_FIJA, Alcance.EMISIONES_DIRECTAS,
                            factor)).getMessage());
  }
}
