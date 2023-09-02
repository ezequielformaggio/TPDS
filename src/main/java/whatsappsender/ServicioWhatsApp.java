package whatsappsender;

import com.mysql.cj.log.Log;
import org.uqbarproject.jpa.java8.extras.PerThreadEntityManagers;

import java.time.LocalDateTime;

public class ServicioWhatsApp implements WhatsAppSender {
  @Override
  public void enviarWhatsApp(String telefono, String contenido) {
    System.out.println("["+ LocalDateTime.now() +"]Se envia un mensaje a " + telefono);
  }
}
