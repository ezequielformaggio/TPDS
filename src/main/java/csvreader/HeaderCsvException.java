package csvreader;

public class HeaderCsvException extends RuntimeException {
  public HeaderCsvException() {
    super("Error de formato en el header del CSV. Por favor verifique el formato nuevamente.");
  }
}
