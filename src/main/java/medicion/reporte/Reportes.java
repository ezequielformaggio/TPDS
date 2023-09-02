package medicion.reporte;

import organizacion.Organizacion;
import organizacion.TipoOrganizacion;
import organizacion.sectorterritorial.SectorTerritorial;

import java.time.LocalDate;

public class Reportes {
  String sectorTerritorial;
  Double valor;
  Organizacion organizacion;
  TipoOrganizacion tipoOrganizacion;
  String fecha;

  public Reportes(String sectorTerritorial, Double valor, Organizacion organizacion, TipoOrganizacion tipoOrganizacion, String fecha) {
    this.sectorTerritorial = sectorTerritorial;
    this.valor = valor;
    this.organizacion = organizacion;
    this.tipoOrganizacion = tipoOrganizacion;
    this.fecha = fecha;
  }

  public String getSectorTerritorial() {
    return sectorTerritorial;
  }

  public void setSectorTerritorial(String sectorTerritorial) {
    this.sectorTerritorial = sectorTerritorial;
  }

  public Double getValor() {
    return valor;
  }

  public void setValor(Double valor) {
    this.valor = valor;
  }

  public Organizacion getOrganizacion() {
    return organizacion;
  }

  public void setOrganizacion(Organizacion organizacion) {
    this.organizacion = organizacion;
  }

  public TipoOrganizacion getTipoOrganizacion() {
    return tipoOrganizacion;
  }

  public void setTipoOrganizacion(TipoOrganizacion tipoOrganizacion) {
    this.tipoOrganizacion = tipoOrganizacion;
  }

  public String getFecha() {
    return fecha;
  }

  public void setFecha(String fecha) {
    this.fecha = fecha;
  }
}
