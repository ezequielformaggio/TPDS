package repositorios;

import medicion.Medicion;
import org.uqbarproject.jpa.java8.extras.PerThreadEntityManagers;
import organizacion.Organizacion;
import usuario.Usuario;

import javax.persistence.EntityManager;
import java.util.List;

public class RepositorioMediciones {

  private static final RepositorioMediciones repositorioMediciones = new RepositorioMediciones();

  public static RepositorioMediciones getInstance() {
    return repositorioMediciones;
  }

  public void nuevaMedicion(Medicion medicion) {
    EntityManager entityManager = PerThreadEntityManagers.getEntityManager();

    entityManager.getTransaction().begin();
    entityManager.persist(medicion);
    entityManager.getTransaction().commit();
  }

}
