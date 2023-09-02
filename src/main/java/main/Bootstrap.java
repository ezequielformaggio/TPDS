package main;

import interesadosguiarecomendaciones.GestorDeServicios;
import interesadosguiarecomendaciones.InteresadoMail;
import interesadosguiarecomendaciones.InteresadoWhatsApp;
import mailsender.ServicioMail;
import medicion.*;
import miembro.Miembro;
import miembro.TipoDocumento;
import org.uqbarproject.jpa.java8.extras.EntityManagerOps;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;

import organizacion.*;

import organizacion.Clasificacion;
import organizacion.Direccion;
import organizacion.Organizacion;
import organizacion.TipoOrganizacion;
import organizacion.sectorterritorial.SectorTerritorial;
import serviciosgeolocalizacion.Geolocalizador;
import serviciosgeolocalizacion.serviciogeoddstpa.ServicioGeoDsTpa;
import transporte.Combustible;
import transporte.mediosdetransporte.TransporteContratado;
import transporte.mediosdetransporte.TransporteEcologico;
import transporte.mediosdetransporte.VehiculoParticular;
import transporte.mediosdetransporte.transportepublico.Parada;
import transporte.mediosdetransporte.transportepublico.TransportePublico;
import transporte.tipostransportes.TipoTransporteEcologico;
import transporte.tipostransportes.TipoTransportePublico;
import transporte.tipostransportes.TipoVehiculo;
import trayectosytramos.Tramo;
import trayectosytramos.Trayecto;

import usuario.RolUsuario;
import usuario.Usuario;
import whatsappsender.ServicioWhatsApp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Ejecutar antes de levantar el servidor por primera vez
 *
 * @author flbulgarelli
 */
public class Bootstrap implements WithGlobalEntityManager, EntityManagerOps, TransactionalOps {

  public static void main() {
    new Bootstrap().run();
  }

  public void run() {
    withTransaction(() -> {

      Organizacion organizacion = new Organizacion("EmpresaA",
          TipoOrganizacion.ONG, new Direccion(1, "maipu", 100),
          Clasificacion.EMPRESA_DEL_SECTOR_PRIMARIO);

      persist(organizacion);


      persist(new Organizacion("EmpresaB",
          TipoOrganizacion.GUBERNAMENTAL, new Direccion(1, "maipu", 100),
          Clasificacion.MINISTERIO));
      persist(new Organizacion("EmpresaC",
          TipoOrganizacion.EMPRESA, new Direccion(1, "maipu", 100),
          Clasificacion.EMPRESA_DEL_SECTOR_SECUNDARIO));
      Miembro pepito = new Miembro("pepito", "gonzalez", TipoDocumento.DNI, "37483728");
      Usuario pepitoUser = new Usuario("pepitoUser", "p3p1t0PASS", RolUsuario.MIEMBRO);
      Usuario organizacionUser = new Usuario("organizacion", "0rg4N1z4C10n", RolUsuario.ORGANIZACION);

      // cargo varios tipos de consumo para probar que los levante bien de la base
      FactorEmision factorGasNatural = new FactorEmision(UnidadHuellaCarbono.KgCO2eq, Unidad.M3, 0.3);

      TipoDeConsumo gasNatural = new TipoDeConsumo("Gas Natural",
          Unidad.M3, Actividad.COMBUSTION_FIJA, Alcance.EMISIONES_DIRECTAS,
          factorGasNatural);

      FactorEmision factorNafta = new FactorEmision(UnidadHuellaCarbono.KgCO2eq, Unidad.LT, 0.5);

      TipoDeConsumo nafta = new TipoDeConsumo("Nafta",
          Unidad.LT,Actividad.COMBUSTION_FIJA, Alcance.EMISIONES_DIRECTAS,
          factorNafta);

      FactorEmision factorCarbon = new FactorEmision(UnidadHuellaCarbono.KgCO2eq, Unidad.KG, 0.3);

      TipoDeConsumo carbon = new TipoDeConsumo("Carbon",
          Unidad.KG,Actividad.COMBUSTION_FIJA, Alcance.EMISIONES_DIRECTAS,
          factorCarbon);

      // medicion
      FactorEmision factor = new FactorEmision(UnidadHuellaCarbono.KgCO2eq, Unidad.M3, 0.3);
      Medicion medicion = new Medicion(gasNatural, 30.0, Periodicidad.ANUAL, LocalDate.now());

      organizacion.agregarMedicion(medicion);

      pepitoUser.asignarMiembro(pepito);
      organizacionUser.asignarOrganizacion(organizacion);

      // contactos
      GestorDeServicios.getInstance().setMailSender(new ServicioMail());
      GestorDeServicios.getInstance().setWhatsAppSender(new ServicioWhatsApp());

      Contacto contactoPepito = new Contacto("pepito@empresaa.com.ar","+5491123456789");
      InteresadoMail interesadoMail = new InteresadoMail();
      InteresadoWhatsApp interesadoWhatsApp = new InteresadoWhatsApp();
      contactoPepito.agregarInteresadoEnGuia(interesadoMail);
      contactoPepito.agregarInteresadoEnGuia(interesadoWhatsApp);

      organizacion.agregarContacto(contactoPepito);

      persist(interesadoMail);
      persist(interesadoWhatsApp);
      persist(pepitoUser);
      persist(organizacion);

      Direccion direccionConstitucion = new Direccion(1, "maipu", 100);
      Direccion direccionIndependencia = new Direccion(1, "maipu", 300);

      VehiculoParticular autoAGnc = new VehiculoParticular(TipoVehiculo.AUTO, Combustible.GNC, 1.1);
      Tramo tramo = new Tramo(autoAGnc,direccionConstitucion, direccionIndependencia, pepito);
      Trayecto trayecto = new Trayecto("Ir a la oficina");
      pepito.agregarTrayecto(trayecto);
      trayecto.agregarTramos(tramo);

      Sector sector = new Sector("Finanzas",organizacion);
      SolicitudVinculacion solicitudVinculacion = new SolicitudVinculacion(sector,pepito);

      organizacion.agregarSolicitudVinculacion(solicitudVinculacion);

      TransporteEcologico bicicleta = new TransporteEcologico(TipoTransporteEcologico.BICI);
      TransporteEcologico monopatin = new TransporteEcologico(TipoTransporteEcologico.MONOPATIN);

      TransporteContratado taxi_premium = new TransporteContratado("Taxi Premium", 1.1);
      TransporteContratado remis = new TransporteContratado("Remiseria Maria", 1.1);

      VehiculoParticular motoElectrica = new VehiculoParticular(TipoVehiculo.MOTO, Combustible.ELECTRICO, 1.0);

      Parada diagonalNorte = new Parada("Diagonal Norte",
          new Direccion(1, "AV ROQUE SAENZ PEÑA", 900),
          300);
      Parada lavalle = new Parada("Lavalle",
          new Direccion(1, "ESMERALDA", 988),
          400);
      Parada sanMartin = new Parada("San Martin",
          new Direccion(1, "AV SANTA FE", 750),
          500);
      Parada retiro = new Parada("Retiro",
          new Direccion(1, "AV DR JOSE MARIA RAMOS MEJIA", 1350),
          0);
      List<Parada> paradasLineaC = new ArrayList<>();
      paradasLineaC.add(diagonalNorte);
      paradasLineaC.add(lavalle);
      paradasLineaC.add(sanMartin);
      paradasLineaC.add(retiro);

      TransportePublico lineaC = new TransportePublico(TipoTransportePublico.SUBTE,
          "Linea C - A Retiro", paradasLineaC, 1.5);

      persist(diagonalNorte);
      persist(lavalle);
      persist(sanMartin);
      persist(retiro);
      persist(lineaC);

      Parada diagonalNorteVuelta = new Parada("Diagonal Norte",
          new Direccion(1, "AV ROQUE SAENZ PEÑA", 900),
          0);
      Parada lavalleVuelta = new Parada("Lavalle",
          new Direccion(1, "ESMERALDA", 988),
          300);
      Parada sanMartinVuelta = new Parada("San Martin",
          new Direccion(1, "AV SANTA FE", 750),
          400);
      Parada retiroVuelta = new Parada("Retiro",
          new Direccion(1, "AV DR JOSE MARIA RAMOS MEJIA", 1350),
          500);
      List<Parada> paradasLineaCVuelta = new ArrayList<>();
      paradasLineaCVuelta.add(retiroVuelta);
      paradasLineaCVuelta.add(sanMartinVuelta);
      paradasLineaCVuelta.add(lavalleVuelta);
      paradasLineaCVuelta.add(diagonalNorteVuelta);

      TransportePublico lineaCVuelta = new TransportePublico(TipoTransportePublico.SUBTE,
          "Linea C - A Diagonal Norte", paradasLineaCVuelta, 1.5);

      SectorTerritorial territorioBuenosAires = new SectorTerritorial("Buenos aires");
      SectorTerritorial territorioChaco = new SectorTerritorial("Chaco");
      territorioBuenosAires.agregarOrganizacion(organizacion);

      persist(retiroVuelta);
      persist(sanMartinVuelta);
      persist(lavalleVuelta);
      persist(diagonalNorteVuelta);
      persist(lineaCVuelta);

      persist(gasNatural);
      persist(nafta);
      persist(carbon);
      persist(medicion);
      persist(organizacionUser);

      persist(bicicleta);
      persist(monopatin);
      persist(taxi_premium);
      persist(remis);
      persist(motoElectrica);
      persist(autoAGnc);
      persist(tramo);
      persist(trayecto);
      persist(pepito);
      persist(contactoPepito);
      persist(sector);
      persist(solicitudVinculacion);
      persist(territorioBuenosAires);
      persist(territorioChaco);

      ServicioGeoDsTpa servicioGeoDsTpa = new ServicioGeoDsTpa();
      Geolocalizador geolocalizador = new Geolocalizador(servicioGeoDsTpa);

      GestorDeServicios.getInstance().setGeolocalizador(geolocalizador);
    });
  }
}
