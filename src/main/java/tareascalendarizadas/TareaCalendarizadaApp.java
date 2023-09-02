package tareascalendarizadas;

import java.util.List;
import javax.persistence.EntityManager;

import org.uqbarproject.jpa.java8.extras.PerThreadEntityManagers;
import organizacion.Contacto;

public class TareaCalendarizadaApp {

  /* main como punto de acceso que llamaría con un crontab una vez por mes,
  pasandole solamente la URL */
  public static void main(String laUrl) {
    EntityManager entityManager = PerThreadEntityManagers.getEntityManager();

    List<Contacto> contactosNotificar = entityManager.createQuery(
        "SELECT DISTINCT c FROM Contacto c JOIN c.interesadosEnGuia", Contacto.class).getResultList();

    contactosNotificar.forEach(contacto -> contacto.notificar(laUrl));
  }
}
