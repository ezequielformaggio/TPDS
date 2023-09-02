package organizacion;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

import medicion.Periodicidad;
import miembro.Miembro;

@Entity
public class Sector {

  @Id
  @GeneratedValue
  private Long id;
  private String descripcion;
  @ManyToMany
  @JoinTable(name = "RelacionSectoresMiembros", joinColumns = @JoinColumn(name = "idSector"),
      inverseJoinColumns = @JoinColumn(name = "idMiembro"))
  private List<Miembro> miembros = new ArrayList<>();
  @ManyToOne
  @JoinColumn(name = "idOrganizacion")
  private Organizacion organizacion;

  protected Sector() {

  }

  public Sector(String descripcion, Organizacion organizacion) {
    this.descripcion = descripcion;
    this.miembros = new ArrayList<>();
    this.organizacion = organizacion;
    this.organizacion.agregarSector(this);
  }

  public void agregarMiembro(Miembro miembro) {
    miembros.add(miembro);
  }

  public void crearListaMiembro() {
    this.miembros = new ArrayList<>();
  }

  public List<Miembro> getMiembros() {
    return miembros;
  }
  
  public String getNombreOrganizacion() {
    return organizacion.getRazonSocial();
  }

  public double calcularHC(Periodicidad periodicidad) {
    return this.miembros.stream().mapToDouble(miembro -> miembro
        .obtenerHuellaCarbono(organizacion, periodicidad)).sum();
  }

  public double indicadorHC(Periodicidad periodicidad) {
    double hcSector = this.calcularHC(periodicidad);

    return hcSector / miembros.size();
  }

  public String getDescripcion() {
    return this.descripcion;
  }

  public Organizacion getOrganizacion(){return this.organizacion;}

  public Long getId(){return this.id;}
}
