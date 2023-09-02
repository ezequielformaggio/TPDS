package organizacion;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import medicion.Medicion;
import miembro.Miembro;
import repositorios.RepositorioMediciones;

@Entity
public class Organizacion {

  @Id
  @GeneratedValue
  private Long id;
  private String razonSocial;
  @Enumerated(EnumType.STRING)
  private TipoOrganizacion tipoOrg;
  @Embedded
  private Direccion direccion;

  @OneToMany
  @JoinColumn(name = "idOrganizacion")
  private List<Sector> sectores;
  @Enumerated(EnumType.STRING)
  private Clasificacion clasificacion;
  @OneToMany
  @JoinColumn(name = "idOrganizacion")
  private List<SolicitudVinculacion> solicitudes;
  @OneToMany
  @JoinColumn(name = "idOrganizacion")
  private List<Medicion> mediciones;
  @OneToMany
  @JoinColumn(name = "idOrganizacion")
  private List<Contacto> contactos;

  private int diasHabiles;

  protected Organizacion() {

  }

  public Organizacion(String razonSocial, TipoOrganizacion tipoOrg,
                      Direccion direccion, Clasificacion clasificacion) {
    this.razonSocial = requireNonNull(razonSocial,
        "Se debe especificar una razon social para la organizacion");
    this.tipoOrg = tipoOrg;
    this.direccion = requireNonNull(direccion,
        "Se debe especificar una direccion para la organizacion");
    this.sectores = new ArrayList<>();
    this.solicitudes = new ArrayList<>();
    this.mediciones = new ArrayList<>();
    this.clasificacion = clasificacion;
    this.contactos = new ArrayList<>();
    this.diasHabiles = 240;
  }

  public Organizacion agregarSector(Sector sector) {
    this.sectores.add(sector);
    return this;
  }

  public List<Sector> getSectores() {
    return sectores;
  }
  
  public String getRazonSocial() {
    return this.razonSocial;
  }

  public void agregarSolicitudVinculacion(SolicitudVinculacion solicitud) {
    solicitudes.add(solicitud);
  }

  public void aceptarSolicitudVinculacion(SolicitudVinculacion solicitud) {
    solicitud.aceptar();
    solicitudes.remove(solicitud);
  }

  public void agregarMedicion(Medicion medicion) {
    this.mediciones.add(medicion);
  }

  public void agregarMedicionCsv(Medicion medicion) {
    this.mediciones.add(medicion);
    RepositorioMediciones.getInstance().nuevaMedicion(medicion);
  }

  public double obtenerHuellaCarbono(Plazo plazo) {

    double hcDeMiembros = this.obtenerHuellaCarbonoMiembros(plazo);

    double hcDeOrganizacion = obtenerHuellaCarbonoMedicionesOrg(plazo);

    return Math.round(hcDeOrganizacion + hcDeMiembros);
  }

  public double obtenerHuellaCarbonoMiembros(Plazo plazo) {

    double hcDeMiembros = this.sectores.stream().mapToDouble(sector -> sector
            .calcularHC(plazo.getPeriodicidad())).sum();

    return Math.round(hcDeMiembros);
  }

  public double obtenerHuellaCarbonoMedicionesOrg(Plazo plazo) {

    double hcMediciones = plazo.getPeriodicidad()
            .getHCOrganizacionSegunPeriodicidad(mediciones, plazo.getFecha());

    return Math.round(hcMediciones);
  }

  public double impactoMiembro(Miembro miembro, Plazo plazo) {
    double hcMiembro = miembro.obtenerHuellaCarbono(this, plazo.getPeriodicidad());

    double hcOrganizacion = this.obtenerHuellaCarbono(plazo);

    return (hcMiembro / hcOrganizacion) * 100;
  }

  public Clasificacion getClasificacion() {
    return this.clasificacion;
  }

  public TipoOrganizacion getTipoOrg() {
    return this.tipoOrg;
  }

  public Direccion getDireccion() {
    return this.direccion;
  }

  public List<SolicitudVinculacion> getSolicitudes() {
    return this.solicitudes;
  }

  public List<Medicion> getMediciones() {
    return this.mediciones;
  }

  public void agregarContacto(Contacto contacto) {
    this.contactos.add(contacto);
  }

  public void eliminarContacto(Contacto contacto) {
    this.contactos.remove(contacto);
  }

  public List<Contacto> getContactos() {
    return contactos;
  }

  public void setDiasHabiles(int diasHabiles) {
    this.diasHabiles = diasHabiles;
  }

  public int getDiasHabiles() {
    return this.diasHabiles;
  }

  public Long getId(){return this.id;}
}