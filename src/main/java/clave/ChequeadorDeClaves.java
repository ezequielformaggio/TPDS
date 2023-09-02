package clave;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ChequeadorDeClaves {

  private static final ChequeadorDeClaves chequeadorDeClaves = new ChequeadorDeClaves();

  public static ChequeadorDeClaves getInstance() {
    return chequeadorDeClaves;
  }

  public void esClaveSegura(String clave) {
    noEsNull(clave);
    cumpleLongitud(clave);
    noEsComun(clave);
  }

  public void cumpleLongitud(String clave) {
    if (clave.length() < 8) {
      throw new ClaveInseguraException("La clave no debe tener menos de 8 caracteres");
    }
  }

  public void noEsNull(String clave) {
    if (clave.equals("")) {
      throw new ClaveInseguraException("La clave no puede estar vacia");
    }
  }

  public void noEsComun(String clave) {
    if (clavesComunes().contains(clave)) {
      throw new ClaveInseguraException("La clave es una clave comun, debe usar otra");
    }
  }

  public List<String> clavesComunes() {
    try {
      return Files.readAllLines(
              Paths.get("src/main/resources/public/files/common-passwords.txt"),
              StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new ArchivoClavesComunesException();
    }
  }
}