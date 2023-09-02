package interesadosguiarecomendaciones;

import mailsender.MailSender;
import organizacion.Contacto;

import javax.persistence.*;

@Entity
@DiscriminatorValue("Mail")
public class InteresadoMail extends InteresadoEnGuia {

  public InteresadoMail() {

  }

  @Override
  public void notificarGuia(Contacto contacto, String url) {
    GestorDeServicios.getInstance().getMailSender()
        .enviarMail(contacto.getMail(), url);
  }
}
