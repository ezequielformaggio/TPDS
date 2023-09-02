package trayectosytramos;

public class MiembrosDeDiferenteOrganizacionException extends RuntimeException {
  public MiembrosDeDiferenteOrganizacionException() {
    super("Los empleados que comparten trayecto deben ser de la misma organizacion");
  }
}