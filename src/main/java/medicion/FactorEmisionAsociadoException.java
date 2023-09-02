package medicion;

public class FactorEmisionAsociadoException extends RuntimeException {
  public FactorEmisionAsociadoException() {
    super("Las unidades del factor de emision asociado no se corresponden"
        + "con las del tipo de consumo");
  }
}