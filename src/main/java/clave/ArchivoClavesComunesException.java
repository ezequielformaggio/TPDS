package clave;

public class ArchivoClavesComunesException extends RuntimeException {
  public ArchivoClavesComunesException() {
    super("No se puede acceder al archivo de claves comunes");
  }
}
