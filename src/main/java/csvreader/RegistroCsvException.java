package csvreader;

public class RegistroCsvException extends RuntimeException {
  public RegistroCsvException(int nroRegistro) {
    super("Existe un problema al intentar cargar el archivo CSV. Por favor "
        + "verifique el formato de la linea " + nroRegistro);
  }
}
