package miembro;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.*;

import medicion.Periodicidad;
import organizacion.Organizacion;
import organizacion.Sector;
import trayectosytramos.Trayecto;

@Entity
public class Miembro {

  @Id
  @GeneratedValue
  private Long id;
  private String nombre;
  private String apellido;
  @Enumerated(EnumType.STRING)
  private TipoDocumento tipoDoc;
  private String nroDocumento;
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "MiembrosSectores", joinColumns = @JoinColumn(name = "idMiembro"),
      inverseJoinColumns = @JoinColumn(name = "idSector"))
  private List<Sector> sectores = new ArrayList<>();
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "MiembrosTrayectos", joinColumns = @JoinColumn(name = "idMiembro"),
      inverseJoinColumns = @JoinColumn(name = "idTrayecto"))
  private List<Trayecto> trayectos = new ArrayList<>();

  protected Miembro() {

  }

  public Miembro(String nombre, String apellido, TipoDocumento tipoDoc, String nroDocumento) {
    this.nombre = Objects.requireNonNull(nombre,
        "Se debe especificar un nombre para el miembro");
    this.apellido = Objects.requireNonNull(apellido,
        "Se debe especificar un apellido para el miembro");
    this.tipoDoc = tipoDoc;
    this.nroDocumento = Objects.requireNonNull(nroDocumento,
        "Se debe especificar un numero de documento para el miembro");
    this.sectores = new ArrayList<>();
    this.trayectos = new ArrayList<>();
  }

  public void agregarSector(Sector sector) {
    sectores.add(sector);
  }

  public void crearListaSector() {
    this.sectores = new ArrayList<>();
  }
  
  public List<Trayecto> getTrayectos() {
    return this.trayectos;
  }

  public void agregarTrayecto(Trayecto trayecto) {
    this.trayectos.add(trayecto);
  }

  public void quitarTrayecto(Trayecto trayecto) {
    this.trayectos.remove(trayecto);
  }

  public String getNombre() {
    return this.nombre;
  }

  public String getApellido() {
    return this.apellido;
  }

  public TipoDocumento getTipoDoc() {
    return this.tipoDoc;
  }

  public String getNroDocumento() {
    return this.nroDocumento;
  }

  public List<Sector> getSectores() {
    return this.sectores;
  }
  
  public Boolean perteneceAOrganizacion(String organizacion) {
    return sectores.stream().anyMatch(
        sector -> sector.getNombreOrganizacion().equals(organizacion));
  }

  public double obtenerHuellaCarbono(Organizacion organizacion, Periodicidad periodicidad) {
    List<Trayecto> trayectosDeOrganizacion = this.trayectos.stream()
        .filter(trayecto -> trayecto.esDe(organizacion)).collect(Collectors.toList());

    double HCTrayectos = trayectosDeOrganizacion.stream().mapToDouble(trayecto ->
        trayecto.obtenerHuellaCarbono()).sum();

    return periodicidad.getHCTrayectosSegunPeriodicidad(HCTrayectos, organizacion.getDiasHabiles());

  }

  public Long getId(){return this.id;}
}
