package medicion;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public enum UnidadHuellaCarbono {
  GrCO2eq {
    @Override
    public double normalizarAKilogramos(double coeficiente) {
      return coeficiente / 1000;
    }
  },
  KgCO2eq {
    @Override
    public double normalizarAKilogramos(double coeficiente) {
      return coeficiente;
    }
  },
  TnCO2eq {
    @Override
    public double normalizarAKilogramos(double coeficiente) {
      return coeficiente * 1000;
    }
  };

  abstract double normalizarAKilogramos(double coefiente);
}
