package db;

import interesadosguiarecomendaciones.*;
import mailsender.MailSender;
import mailsender.ServicioMail;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.test.AbstractPersistenceTest;
import organizacion.*;
import tareascalendarizadas.TareaCalendarizadaApp;
import whatsappsender.ServicioWhatsApp;
import whatsappsender.WhatsAppSender;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

public class NotificacionesGuiaTest extends AbstractPersistenceTest implements WithGlobalEntityManager {

  @BeforeEach
  public void onInit() {
    super.setup();
    GestorDeServicios.getInstance().setMailSender(mock(MailSender.class));
    GestorDeServicios.getInstance().setWhatsAppSender(mock(WhatsAppSender.class));

    InteresadoMail interesadoMail = new InteresadoMail();
    InteresadoWhatsApp interesadoWhatsApp = new InteresadoWhatsApp();

    Organizacion org = new Organizacion("EmpresaA",
        TipoOrganizacion.ONG, new Direccion(1, "maipu", 100),
        Clasificacion.EMPRESA_DEL_SECTOR_PRIMARIO);

    Contacto contactoPepito = new Contacto("pepito@empresaa.com.ar","+5491123456789");
    contactoPepito.agregarInteresadoEnGuia(interesadoMail);
    contactoPepito.agregarInteresadoEnGuia(interesadoWhatsApp);

    org.agregarContacto(contactoPepito);

    entityManager().persist(interesadoMail);
    entityManager().persist(interesadoWhatsApp);
    entityManager().persist(contactoPepito);
    entityManager().persist(org);
  }

  @AfterEach
  public void onExit() {
    super.tearDown();
  }

  @Test
  public void siPrueboSiLlegoAClaseServicioSeCreaUnLog() {
    GestorDeServicios.getInstance().setMailSender(new ServicioMail());
    GestorDeServicios.getInstance().setWhatsAppSender(new ServicioWhatsApp());
    TareaCalendarizadaApp.main("https://www.laguia.com.ar");
    Assertions.assertEquals(1, 1);
  }

  @Test
  public void siDisparoEnvioDeGuiaSeNotificaPorMailAContactos() {
    Contacto contactoPepito = entityManager().createQuery(
        "SELECT c FROM Contacto c WHERE c.mail = 'pepito@empresaa.com.ar'" +
            "AND c.telefono = '+5491123456789'", Contacto.class).getSingleResult();

    TareaCalendarizadaApp.main("https://www.laguia.com.ar");
    Mockito.verify(GestorDeServicios.getInstance().getMailSender(), times(1)).
        enviarMail(contactoPepito.getMail(), "https://www.laguia.com.ar");
  }

  @Test
  public void siDisparoEnvioDeGuiaSeNotificaPorWhatsAppAContactos() {
    Contacto contactoPepito = entityManager().createQuery(
        "SELECT c FROM Contacto c WHERE c.mail = 'pepito@empresaa.com.ar'" +
            "AND c.telefono = '+5491123456789'", Contacto.class).getSingleResult();
    TareaCalendarizadaApp.main("https://www.laguia.com.ar");
    Mockito.verify(GestorDeServicios.getInstance().getWhatsAppSender(), times(1)).
        enviarWhatsApp(contactoPepito.getTelefono(), "https://www.laguia.com.ar");
  }

  @Test
  public void siPepitoSoloDecideRecibirMailsNoRecibeWhatsApp() {
    Contacto contactoPepito = entityManager().createQuery(
        "SELECT c FROM Contacto c WHERE c.mail = 'pepito@empresaa.com.ar'" +
            "AND c.telefono = '+5491123456789'", Contacto.class).getSingleResult();
    contactoPepito.eliminarInteresadoEnGuia(contactoPepito.getInteresadosEnGuia().get(1));
    entityManager().persist(contactoPepito);

    TareaCalendarizadaApp.main("https://www.laguia.com.ar");

    Mockito.verify(GestorDeServicios.getInstance().getMailSender(), times(1)).
        enviarMail(contactoPepito.getMail(), "https://www.laguia.com.ar");
    Mockito.verify(GestorDeServicios.getInstance().getWhatsAppSender(), times(0)).
        enviarWhatsApp(contactoPepito.getTelefono(), "https://www.laguia.com.ar");
  }

  @Test
  public void siPepitoSoloDecideRecibirWhatsAppNoRecibeMails() {
    Contacto contactoPepito = entityManager().createQuery(
        "SELECT c FROM Contacto c WHERE c.mail = 'pepito@empresaa.com.ar'" +
            "AND c.telefono = '+5491123456789'", Contacto.class).getSingleResult();
    contactoPepito.eliminarInteresadoEnGuia(contactoPepito.getInteresadosEnGuia().get(0));
    entityManager().persist(contactoPepito);

    TareaCalendarizadaApp.main("https://www.laguia.com.ar");

    Mockito.verify(GestorDeServicios.getInstance().getMailSender(), times(0)).
        enviarMail(contactoPepito.getMail(), "https://www.laguia.com.ar");
    Mockito.verify(GestorDeServicios.getInstance().getWhatsAppSender(), times(1)).
        enviarWhatsApp(contactoPepito.getTelefono(), "https://www.laguia.com.ar");
  }

  @Test
  public void cadaContactoPuedeSetearSuPreferenciaDeNotificacion() {
    Contacto contactoPepito = entityManager().createQuery(
        "SELECT c FROM Contacto c WHERE c.mail = 'pepito@empresaa.com.ar'" +
            "AND c.telefono = '+5491123456789'", Contacto.class).getSingleResult();

    InteresadoMail otroInteresadoMail = new InteresadoMail();
    InteresadoWhatsApp otroInteresadoWhatsApp = new InteresadoWhatsApp();

    Contacto contactoJaimito = new Contacto("jaimito@empresab.com.ar", "+5491154659878");
    contactoJaimito.agregarInteresadoEnGuia(otroInteresadoMail);
    Contacto contactoAndreita = new Contacto("andreita@empresab.com.ar", "+5491121356898");
    contactoAndreita.agregarInteresadoEnGuia(otroInteresadoWhatsApp);

    Organizacion orgB = new Organizacion("EmpresaB",
        TipoOrganizacion.EMPRESA, new Direccion(5, "lavalle", 653),
        Clasificacion.EMPRESA_DEL_SECTOR_SECUNDARIO);

    orgB.agregarContacto(contactoJaimito);
    orgB.agregarContacto(contactoAndreita);

    entityManager().persist(otroInteresadoMail);
    entityManager().persist(otroInteresadoWhatsApp);
    entityManager().persist(contactoJaimito);
    entityManager().persist(contactoAndreita);
    entityManager().persist(orgB);

    TareaCalendarizadaApp.main("https://www.laguia.com.ar");

    // un mail para pepito + un mail para jaimito
    Mockito.verify(GestorDeServicios.getInstance().getMailSender(), times(2)).
        enviarMail(any(), any());
    // un whatsapp para pepito + un whatsapp para andreita
    Mockito.verify(GestorDeServicios.getInstance().getWhatsAppSender(), times(2)).
        enviarWhatsApp(any(), any());

    Mockito.verify(GestorDeServicios.getInstance().getMailSender(), times(1)).
        enviarMail(contactoPepito.getMail(), "https://www.laguia.com.ar");
    Mockito.verify(GestorDeServicios.getInstance().getWhatsAppSender(), times(1)).
        enviarWhatsApp(contactoPepito.getTelefono(), "https://www.laguia.com.ar");

    Mockito.verify(GestorDeServicios.getInstance().getMailSender(), times(1)).
        enviarMail(contactoJaimito.getMail(), "https://www.laguia.com.ar");
    Mockito.verify(GestorDeServicios.getInstance().getWhatsAppSender(), times(0)).
        enviarWhatsApp(contactoJaimito.getTelefono(), "https://www.laguia.com.ar");

    Mockito.verify(GestorDeServicios.getInstance().getMailSender(), times(0)).
        enviarMail(contactoAndreita.getMail(), "https://www.laguia.com.ar");
    Mockito.verify(GestorDeServicios.getInstance().getWhatsAppSender(), times(1)).
        enviarWhatsApp(contactoAndreita.getTelefono(), "https://www.laguia.com.ar");
  }
}
