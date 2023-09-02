package administrador;

import clave.ChequeadorDeClaves;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Entity
public class Administrador {

  @Id
  @GeneratedValue
  private Long id;
  private String usuario;
  private String contrasenia;

  protected Administrador() {

  }

  public Administrador(String usuario, String contrasenia) {
    this.usuario = Objects.requireNonNull(usuario,
        "Se debe especificar un usuario para el administrador");
    this.contrasenia = validarContrasenia(contrasenia);
  }

  private String validarContrasenia(String contrasenia) {
    ChequeadorDeClaves.getInstance().esClaveSegura(contrasenia);
    return contrasenia;
  }

  public String getUsuario() {
    return this.usuario;
  }

  public String getContrasenia() {
    return this.contrasenia;
  }

}

