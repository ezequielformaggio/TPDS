package interesadosguiarecomendaciones;

import mailsender.MailSender;
import serviciosgeolocalizacion.Geolocalizador;
import whatsappsender.WhatsAppSender;

public class GestorDeServicios {

  private static final GestorDeServicios gestorDeServicios = new GestorDeServicios();

  private MailSender mailSender;
  private WhatsAppSender whatsAppSender;
  private Geolocalizador geolocalizador;

  public static GestorDeServicios getInstance() {
    return gestorDeServicios;
  }

  public void setMailSender(MailSender mailSender) {
    this.mailSender = mailSender;
  }

  public void setWhatsAppSender(WhatsAppSender whatsAppSender) {
    this.whatsAppSender = whatsAppSender;
  }

  public void setGeolocalizador(Geolocalizador geolocalizador) {
    this.geolocalizador = geolocalizador;
  }

  public MailSender getMailSender() {
    return this.mailSender;
  }

  public WhatsAppSender getWhatsAppSender() {
    return this.whatsAppSender;
  }

  public Geolocalizador getGeolocalizador() {
    return this.geolocalizador;
  }
}
