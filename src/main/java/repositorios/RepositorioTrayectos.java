package repositorios;

import org.uqbarproject.jpa.java8.extras.PerThreadEntityManagers;
import trayectosytramos.Trayecto;

import javax.persistence.EntityManager;

public class RepositorioTrayectos {

  private static final RepositorioTrayectos repositorioTrayectos = new RepositorioTrayectos();

  public static RepositorioTrayectos getInstance() {
    return repositorioTrayectos;
  }

  public void nuevoTrayecto(Trayecto trayecto) {
    EntityManager entityManager = PerThreadEntityManagers.getEntityManager();

    entityManager.getTransaction().begin();
    entityManager.persist(trayecto);
    entityManager.getTransaction().commit();
  }

  public Trayecto encontrarTrayectoPorId(Long idTrayecto) {
    return PerThreadEntityManagers.getEntityManager().find(Trayecto.class, idTrayecto);
  }
}
