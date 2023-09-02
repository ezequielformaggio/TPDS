package mailsender;

import org.uqbarproject.jpa.java8.extras.PerThreadEntityManagers;

import java.time.LocalDateTime;

public class ServicioMail implements MailSender {
  @Override
  public void enviarMail(String direccion, String contenido) {
    System.out.println("["+ LocalDateTime.now() +"]Se envia un mail a " + direccion);
  }
}
