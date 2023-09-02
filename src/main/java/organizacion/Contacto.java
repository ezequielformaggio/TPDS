package organizacion;

import static java.util.Objects.requireNonNull;

import interesadosguiarecomendaciones.InteresadoEnGuia;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Entity
public class Contacto {
  @Id
  @GeneratedValue
  private Long id;
  private String mail;
  private String telefono;
  @ManyToMany(fetch=FetchType.EAGER, cascade= CascadeType.ALL)
  @JoinTable(name = "NotificacionContacto", joinColumns = @JoinColumn(name = "idContacto"),
      inverseJoinColumns = @JoinColumn(name = "idNotificacion"))
  private List<InteresadoEnGuia> interesadosEnGuia;

  protected Contacto() {

  }

  public Contacto(String mail, String telefono) {
    this.mail = requireNonNull(mail,
        "Se debe especificar una direccion de e-mail");
    this.telefono = requireNonNull(telefono,
        "Se debe especificar un numero de telefono");
    this.interesadosEnGuia = new ArrayList<>();
  }

  public String getMail() {
    return this.mail;
  }

  public String getTelefono() {
    return this.telefono;
  }

  public void agregarInteresadoEnGuia(InteresadoEnGuia interesado) {
    this.interesadosEnGuia.add(interesado);
  }

  public void eliminarInteresadoEnGuia(InteresadoEnGuia interesado) {
    this.interesadosEnGuia.remove(interesado);
  }

  public List<InteresadoEnGuia> getInteresadosEnGuia() {
    return this.interesadosEnGuia;
  }

  public void notificar(String url) {
    interesadosEnGuia.forEach(interesado -> interesado.notificarGuia(this, url));
  }

  public Long getIdContacto() {
    return this.id;
  }
}
