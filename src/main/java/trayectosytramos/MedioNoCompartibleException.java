package trayectosytramos;

public class MedioNoCompartibleException extends RuntimeException {
  public MedioNoCompartibleException() {
    super("El medio de transporte debe ser particular o contratado");
  }
}