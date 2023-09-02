package db;

import csvreader.*;
import medicion.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CsvReaderTest extends AbstractPersistenceTest implements WithGlobalEntityManager {

  @BeforeEach
  public void onInit() {
    super.setup();

    FactorEmision factorGasNatural = new FactorEmision(UnidadHuellaCarbono.KgCO2eq, Unidad.M3, 0.3);

    TipoDeConsumo gasNatural = new TipoDeConsumo("Gas Natural",
        Unidad.M3, Actividad.COMBUSTION_FIJA, Alcance.EMISIONES_DIRECTAS,
        factorGasNatural);

    FactorEmision factorNafta = new FactorEmision(UnidadHuellaCarbono.KgCO2eq, Unidad.LT, 0.5);

    TipoDeConsumo nafta = new TipoDeConsumo("Nafta",
        Unidad.LT,Actividad.COMBUSTION_FIJA, Alcance.EMISIONES_DIRECTAS,
        factorNafta);

    FactorEmision factorCarbon = new FactorEmision(UnidadHuellaCarbono.KgCO2eq, Unidad.KG, 0.3);

    TipoDeConsumo carbon = new TipoDeConsumo("Carbon",
        Unidad.KG,Actividad.COMBUSTION_FIJA, Alcance.EMISIONES_DIRECTAS,
        factorCarbon);

    entityManager().persist(gasNatural);
    entityManager().persist(nafta);
    entityManager().persist(carbon);
  }

  @AfterEach
  public void onExit() {
    super.tearDown();
  }

  @Test
  public void sePermiteCargarMedicionesPorCSV() {
    CsvReader reader = new CsvReader("src/test/resources/CSVmuestra.csv");
    List<Medicion> nuevasMediciones = reader.extraerMediciones();

    // org ahora tiene 4 mediciones.html.hbs
    assertEquals(4, nuevasMediciones.size());
    // de esas 4 mediciones.html.hbs la segunda es de tipo de consumo = nafta, el valor es 100.0 y
    // la periodicidad es mensual
    assertEquals("Nafta", nuevasMediciones.get(1).getTipoConsumo().getDescripcion());
    assertEquals(100.0, nuevasMediciones.get(1).getValor());
    assertEquals(Periodicidad.MENSUAL, nuevasMediciones.get(1).getPeriodicidad());
  }

  @Test
  public void siElCSVQueQuieroCargarNoExisteTiraExcepcion() {
    CsvReader reader = new CsvReader("src/test/resources/CSVmuestra_ups.csv");
    assertThrows(ArchivoCsvException.class, () -> reader.extraerMediciones());
  }

  @Test
  public void siElHeaderDelCSVTieneFormatoIncorrectoTiraExcepcion() {
    CsvReader reader = new CsvReader("src/test/resources/CSV_formatoHeaderIncorrecto.csv");
    assertThrows(HeaderCsvException.class, () -> reader.extraerMediciones());
  }

  @Test
  public void siExisteUnRegistroConFormatoIncorrectoTiraExcepcion() {
    CsvReader reader = new CsvReader("src/test/resources/CSV_formatoRegistroIncorrecto.csv");
    assertThrows(RegistroCsvException.class, () -> reader.extraerMediciones());
  }

  @Test
  public void siNoSeEncuentraUnTipoDeConsumoValidoTiraExcepcion() {
    CsvReader reader = new CsvReader("src/test/resources/CSV_tipoConsumoInexistente.csv");
    assertThrows(CampoCsvException.class, () -> reader.extraerMediciones());
  }

  @Test
  public void siSeIntentaCargarUnaFechaIncorrectaTiraExcepcion() {
    CsvReader reader = new CsvReader("src/test/resources/CSV_fechaFueraDeRango.csv");
    assertThrows(CampoCsvException.class, () -> reader.extraerMediciones());
  }
}
