package repositorios;

import miembro.Miembro;
import org.uqbarproject.jpa.java8.extras.PerThreadEntityManagers;

import javax.persistence.EntityManager;

public class RepositorioMiembros {

  private static final RepositorioMiembros repositorioMiembros = new RepositorioMiembros();

  public static RepositorioMiembros getInstance() {
    return repositorioMiembros;
  }

  public void nuevoMiembro(Miembro miembro) {
    EntityManager entityManager = PerThreadEntityManagers.getEntityManager();

    entityManager.getTransaction().begin();
    entityManager.persist(miembro);
    entityManager.getTransaction().commit();
  }
}
