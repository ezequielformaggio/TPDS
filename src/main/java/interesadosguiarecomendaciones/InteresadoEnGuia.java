package interesadosguiarecomendaciones;

import organizacion.Contacto;

import javax.persistence.*;

@Entity(name = "Notificacion")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "MetodoNotificacion")
public abstract class InteresadoEnGuia {

  @Id
  @GeneratedValue
  private Long id;

  public void notificarGuia(Contacto contacto, String url) {

  }
}
