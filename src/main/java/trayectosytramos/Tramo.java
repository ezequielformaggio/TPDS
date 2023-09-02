package trayectosytramos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;

import miembro.Miembro;
import organizacion.Direccion;
import serviciosgeolocalizacion.Geolocalizador;
import transporte.mediosdetransporte.MedioDeTransporte;
import transporte.mediosdetransporte.transportepublico.Parada;

@Entity
public class Tramo {

  @Id
  @GeneratedValue
  private Long idTramo;
  private Long idTrayecto;
  @ManyToOne
  @JoinColumn(name = "idMedioTransporte")
  private MedioDeTransporte transporte;
  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "localidad", column = @Column(name = "localidadOrigen")),
      @AttributeOverride(name = "calle", column = @Column(name = "calleOrigen")),
      @AttributeOverride(name = "altura", column = @Column(name = "alturaOrigen"))
  })
  private Direccion origen;
  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "localidad", column = @Column(name = "localidadDestino")),
      @AttributeOverride(name = "calle", column = @Column(name = "calleDestino")),
      @AttributeOverride(name = "altura", column = @Column(name = "alturaDestino"))
  })
  private Direccion destino;
  @ManyToMany
  @JoinTable(name = "TramosMiembros", joinColumns = @JoinColumn(name = "idTramo"),
      inverseJoinColumns = @JoinColumn(name = "idMiembro"))
  private List<Miembro> miembros = new ArrayList<>();

  protected Tramo() {

  }

  public Tramo(MedioDeTransporte transporte, Direccion origen, Direccion destino, Miembro miembro) {
    this.transporte = Objects.requireNonNull(transporte,
        "Se debe especificar un medio de transporte para el tramo");
    this.origen = Objects.requireNonNull(origen,
        "Se debe especificar el punto de partida del tramo");
    this.destino = Objects.requireNonNull(destino,
        "Se debe especificar el punto de llegada del tramo");
    this.miembros.add(miembro);
  }

  public Tramo(MedioDeTransporte transporte, Parada puntoPartida, Parada puntoLlegada,
               Miembro miembro) {
    this.transporte = Objects.requireNonNull(transporte,
        "Se debe especificar un medio de transporte para el tramo");
    Objects.requireNonNull(puntoPartida, "Se debe especificar la parada de inicio del tramo");
    Objects.requireNonNull(puntoLlegada, "Se debe especificar la parada de fin del tramo");
    this.origen = puntoPartida.getDireccion();
    this.destino = puntoLlegada.getDireccion();
    this.miembros.add(miembro);
  }

  public MedioDeTransporte getTransporte() {
    return transporte;
  }

  public Double getDistancia() {
    return transporte.getDistancia(this.origen, this.destino);
  }

  public Direccion getOrigen() {
    return this.origen;
  }

  public Direccion getDestino() {
    return this.destino;
  }

  public List<Miembro> getMiembros() {
    return miembros;
  }

  public void agregarMiembro(Miembro...miembros) {
    Collections.addAll(this.miembros, miembros);
  }

  public double calcularHuella() {
    return this.transporte.calcularHC(this.getDistancia()) / miembros.size();
  }

  public Long getIdTramo() {
    return this.idTramo;
  }
}