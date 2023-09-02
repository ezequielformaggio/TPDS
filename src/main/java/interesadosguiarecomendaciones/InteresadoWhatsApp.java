package interesadosguiarecomendaciones;

import organizacion.Contacto;
import whatsappsender.WhatsAppSender;

import javax.persistence.*;

@Entity
@DiscriminatorValue("WhatsApp")
public class InteresadoWhatsApp extends InteresadoEnGuia {

  public InteresadoWhatsApp() {

  }

  @Override
  public void notificarGuia(Contacto contacto, String url) {
    GestorDeServicios.getInstance().getWhatsAppSender()
        .enviarWhatsApp(contacto.getTelefono(), url);
  }
}
