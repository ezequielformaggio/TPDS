package ejercicio;

import administrador.Administrador;
import clave.ClaveInseguraException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ChequeoDeClavesTest {

  @Test
  public void seCreaAdministradorConContraseniaValida() {
    Administrador admin = new Administrador("miUsuario", "m1cl4v3nu3v4TRN");

    assertEquals(admin.getUsuario(), "miUsuario");
    assertEquals(admin.getContrasenia(), "m1cl4v3nu3v4TRN");
  }

  @Test
  public void noSePuedeCrearUnAdministradorConContraseniaInseguraComun() {
    assertEquals("La clave es una clave comun, debe usar otra",
        assertThrows(ClaveInseguraException.class, () -> new Administrador("asd", "airplane"))
        .getMessage());
  }

  @Test
  public void noSePuedeCrearUnAdministradorConContraseniaInseguraCorta() {
    assertEquals("La clave no debe tener menos de 8 caracteres",
        assertThrows(ClaveInseguraException.class, () -> new Administrador("asd", "aaa"))
            .getMessage());
  }

  @Test
  public void noSePuedeCrearUnAdministradorConContraseniaInseguraVacia() {
    assertEquals("La clave no puede estar vacia",
        assertThrows(ClaveInseguraException.class, () -> new Administrador("asd", ""))
            .getMessage());
  }
}
