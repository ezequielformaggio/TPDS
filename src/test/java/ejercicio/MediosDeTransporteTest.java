package ejercicio;

//import com.sun.org.apache.xml.internal.security.transforms.implementations.TransformXPointer;
import org.junit.jupiter.api.Test;
import organizacion.Direccion;
import transporte.*;
import transporte.mediosdetransporte.*;
import transporte.mediosdetransporte.transportepublico.Parada;
import transporte.mediosdetransporte.transportepublico.TransportePublico;
import transporte.tipostransportes.TipoTransporteEcologico;
import transporte.tipostransportes.TipoTransportePublico;
import transporte.tipostransportes.TipoVehiculo;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MediosDeTransporteTest {
  @Test
  public void crearNuevoVehiculoParticular(){
    VehiculoParticular autoAGnc = new VehiculoParticular(TipoVehiculo.AUTO, Combustible.GNC, 1.1);
    VehiculoParticular motoElectrica = new VehiculoParticular(TipoVehiculo.MOTO, Combustible.ELECTRICO, 1.0);
    assertEquals(autoAGnc.getTipoVehiculo(),TipoVehiculo.AUTO);
    assertEquals(motoElectrica.getTipoCombustible(), Combustible.ELECTRICO);
  }
  @Test
  public void crearNuevoTransporteEcologico() {
    TransporteEcologico bicicleta = new TransporteEcologico(TipoTransporteEcologico.BICI);
    TransporteEcologico monopatin = new TransporteEcologico(TipoTransporteEcologico.MONOPATIN);

    assertEquals(bicicleta.getTipoTransporteEcologico(), TipoTransporteEcologico.BICI);
    assertEquals(monopatin.getTipoTransporteEcologico(), TipoTransporteEcologico.MONOPATIN);
  }
  @Test
  public void crearNuevoTransporteContratado(){
    TransporteContratado taxi_premium = new TransporteContratado("Taxi Premium", 1.1);
    TransporteContratado remis = new TransporteContratado("Remiseria Maria", 1.1);
    assertEquals(taxi_premium.getNombre(),"Taxi Premium");
    assertEquals(remis.getNombre(),"Remiseria Maria");
  }
  @Test
  public void crearNuevaLineaDeTransportePublico() {

    TransportePublico subteLineaCARetiro = crearLineaCARetiro();

    assertEquals(subteLineaCARetiro.getTipoTransportePublico(), TipoTransportePublico.SUBTE);
    assertEquals(subteLineaCARetiro.getLinea(), "Linea C - A Retiro");
    assertEquals(subteLineaCARetiro.getParadas().size(), 4);
    assertEquals(subteLineaCARetiro.getParadas().get(0).getNombre(), "Diagonal Norte");
    assertEquals(subteLineaCARetiro.getParadas().get(3).getNombre(), "Retiro");
  }
  @Test
  void agregarNuevaParadaAdemasAgregaNuevaDistancia() {
    TransportePublico subteLineaCARetiro = crearLineaCARetiro();

    int indiceRetiro = 3;

    assertEquals(subteLineaCARetiro.getParadas().get(indiceRetiro).getDistanciaProxima(), 0);

    Parada nuevaParadaFinal = new Parada("Parada final",
        new Direccion(1, "AV DR JOSE MARIA RAMOS MEJIA", 1900),
        0);

    subteLineaCARetiro.agregarParada(nuevaParadaFinal, 500);

    assertEquals(subteLineaCARetiro.getParadas().get(indiceRetiro).getDistanciaProxima(), 500);
    assertEquals(subteLineaCARetiro.getParadas().get(4).getDistanciaProxima(), 0);
  }

  public TransportePublico crearLineaCARetiro() {
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

    return new TransportePublico(TipoTransportePublico.SUBTE,
        "Linea C - A Retiro", paradasLineaC, 1.5);
  }
}
