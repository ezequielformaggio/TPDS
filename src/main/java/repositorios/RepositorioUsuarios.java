package repositorios;

import miembro.Miembro;
import org.uqbarproject.jpa.java8.extras.PerThreadEntityManagers;
import organizacion.Contacto;
import usuario.Usuario;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class RepositorioUsuarios {

  private static final RepositorioUsuarios repositorioUsuarios = new RepositorioUsuarios();

  public static RepositorioUsuarios getInstance() {
    return repositorioUsuarios;
  }

  public void nuevoUsuario(Usuario usuario) {
    EntityManager entityManager = PerThreadEntityManagers.getEntityManager();

    entityManager.getTransaction().begin();
    entityManager.persist(usuario);
    entityManager.getTransaction().commit();
  }

  public Usuario buscarPorUsuarioYContrasenia(String nombreUsuario, String contrasenia) {
    EntityManager entityManager = PerThreadEntityManagers.getEntityManager();

    TypedQuery<Usuario> query = entityManager.createQuery(
        "SELECT u FROM Usuario u WHERE u.nombreUsuario = '" + nombreUsuario + "'" +
            "AND u.contrasenia = '" + contrasenia + "'", Usuario.class);

    return query.getSingleResult();
  }

  public Usuario buscarUsuarioPorIdUsuario(Long idUsuario) {
    return PerThreadEntityManagers.getEntityManager().find(Usuario.class, idUsuario);
  }
}
