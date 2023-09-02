package usuario;

import clave.ChequeadorDeClaves;
import miembro.Miembro;
import organizacion.Organizacion;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Usuario {

  @Id
  @GeneratedValue
  private Long id;
  private String nombreUsuario;
  private String contrasenia;

  @Enumerated(EnumType.STRING)
  private RolUsuario rolUsuario;

  @OneToOne
  @JoinColumn(name = "idMiembro")
  private Miembro miembro = null;
  @OneToOne
  @JoinColumn(name = "idOrganizacion")
  private Organizacion organizacion = null;

  protected Usuario() {

  }

  public Usuario(String nombreUsuario, String contrasenia, RolUsuario rolUsuario) {
    this.nombreUsuario = Objects.requireNonNull(nombreUsuario,
        "Se debe especificar un nombre de usuario");
    this.contrasenia = validarContrasenia(contrasenia);
    this.rolUsuario = rolUsuario;
  }

  private String validarContrasenia(String contrasenia) {
    ChequeadorDeClaves.getInstance().esClaveSegura(contrasenia);
    return contrasenia;
  }

  public void asignarMiembro(Miembro miembro) {
    if (this.rolUsuario == RolUsuario.MIEMBRO) {
      this.miembro = miembro;
    }
  }

  public void asignarOrganizacion(Organizacion organizacion) {
    if (this.rolUsuario == RolUsuario.ORGANIZACION) {
      this.organizacion = organizacion;
    }
  }

  public String getNombreUsuario() {
    return this.nombreUsuario;
  }

  public String getContrasenia() {
    return this.contrasenia;
  }

  public Miembro getMiembro() {
    return this.miembro;
  }

  public Organizacion getOrganizacion() {
    return this.organizacion;
  }

  public Long getId() {
    return this.id;
  }

  public RolUsuario getRolUsuario(){return this.rolUsuario;}
}
