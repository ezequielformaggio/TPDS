package organizacion.sectorterritorial;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import organizacion.Organizacion;
import organizacion.Plazo;

@Entity
public class SectorTerritorial {

  @Id
  @GeneratedValue
  private Long id;
  private String descripcion;
  @OneToMany
  @JoinColumn(name = "idSectorTerritorial")
  private List<Organizacion> organizaciones;

  protected SectorTerritorial() {

  }

  public SectorTerritorial(String descripcion) {
    this.descripcion = descripcion;
    this.organizaciones = new ArrayList<>();
  }

  public void agregarOrganizacion(Organizacion organizacion) {
    this.organizaciones.add(organizacion);
  }

  public void eliminarOrganizacion(Organizacion organizacion) {
    this.organizaciones.remove(organizacion);
  }

  public double calcularHC(Plazo plazo) {


    return this.organizaciones.stream().mapToDouble(organizacion -> organizacion
        .obtenerHuellaCarbono(plazo)).sum();

    // la guarda y no lo devuelve

  }

  public double calcularHCMiembros(Plazo plazo) {
    return this.organizaciones.stream().mapToDouble(organizacion -> organizacion
            .obtenerHuellaCarbonoMiembros(plazo)).sum();
  }

  public double calcularHCMediciones(Plazo plazo) {
    return this.organizaciones.stream().mapToDouble(organizacion -> organizacion
            .obtenerHuellaCarbonoMedicionesOrg(plazo)).sum();
  }

  public String getDescripcion() {
    return this.descripcion;
  }

  public Long getidSectorTerritorial() {
    return this.id;
  }

  public List<Organizacion> getOrganizaciones (){
    return organizaciones;
  }
}
