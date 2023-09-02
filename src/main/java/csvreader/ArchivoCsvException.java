package csvreader;

public class ArchivoCsvException extends RuntimeException {
  public ArchivoCsvException(String path) {
    super("No se encuentra el archivo CSV " + path);
  }
}
