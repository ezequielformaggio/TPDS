package organizacion;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import miembro.Miembro;

@Entity
public class SolicitudVinculacion {

  @Id
  @GeneratedValue
  private Long id;
  @ManyToOne
  @JoinColumn(name = "idSector")
  Sector sector;
  @ManyToOne
  @JoinColumn(name = "idMiembro")
  Miembro miembro;
  @Enumerated(EnumType.STRING)
  EstadoSolicitudVinculacion estado;

  protected SolicitudVinculacion() {

  }

  public SolicitudVinculacion(Sector sector, Miembro miembro) {
    this.sector = sector;
    this.miembro = miembro;
    this.estado = EstadoSolicitudVinculacion.PENDIENTE;
  }

  public void aceptar() {
    this.sector.agregarMiembro(this.miembro);
    this.miembro.agregarSector(this.sector);
    this.estado = EstadoSolicitudVinculacion.ACEPTADA;
  }

  public void rechazar() {
    this.estado = EstadoSolicitudVinculacion.RECHAZADA;
  }

  public Sector getSector() {
    return sector;
  }

  public Miembro getMiembro() {
    return miembro;
  }

  public EstadoSolicitudVinculacion getEstado() {
    return estado;
  }

  public Long getId(){return this.id;}
}
