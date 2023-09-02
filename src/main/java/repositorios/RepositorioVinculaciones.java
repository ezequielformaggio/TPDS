package repositorios;

import org.uqbarproject.jpa.java8.extras.PerThreadEntityManagers;
import organizacion.SolicitudVinculacion;
import usuario.Usuario;

import javax.persistence.EntityManager;
import java.util.List;

public class RepositorioVinculaciones {

  private static final RepositorioVinculaciones repositorioVinculaciones = new RepositorioVinculaciones();

  public static RepositorioVinculaciones getInstance() {
    return repositorioVinculaciones;
  }

  public void nuevaSolicitudVinculacion(SolicitudVinculacion solicitud) {
    EntityManager entityManager = PerThreadEntityManagers.getEntityManager();

    entityManager.getTransaction().begin();
    entityManager.persist(solicitud);
    entityManager.getTransaction().commit();
  }

  public void solicitudVinculacionModificada(SolicitudVinculacion solicitud) {
    EntityManager entityManager = PerThreadEntityManagers.getEntityManager();

    entityManager.getTransaction().begin();
    entityManager.persist(solicitud);
    entityManager.getTransaction().commit();
  }
}
