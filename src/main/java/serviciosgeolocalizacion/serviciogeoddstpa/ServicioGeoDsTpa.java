package serviciosgeolocalizacion.serviciogeoddstpa;

import organizacion.Direccion;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import serviciosgeolocalizacion.ObtenerDistanciasException;
import serviciosgeolocalizacion.ServicioGeolocalizacion;
import serviciosgeolocalizacion.serviciogeoddstpa.entidades.Distancia;

public class ServicioGeoDsTpa implements ServicioGeolocalizacion {

  private final Retrofit retrofit;
  private static final String urlAPI = "https://ddstpa.com.ar/api/";

  public ServicioGeoDsTpa() {
    this.retrofit = new Retrofit.Builder()
        .baseUrl(urlAPI)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
  }

  @Override
  public Double distancia(Direccion origen, Direccion destino) {
    int idOrigen = origen.getLocalidad();
    String calleOrigen = origen.getCalle();
    int alturaOrigen = origen.getAltura();
    int idDestino = destino.getLocalidad();
    String calleDestino = destino.getCalle();
    int alturaDestino = destino.getAltura();

    ConnServicioGeoDsTpa connServicioGeoDsTpa = this.retrofit
        .create(ConnServicioGeoDsTpa.class);
    Call<Distancia> requestDistancia = connServicioGeoDsTpa
        .distancia("Bearer " + System.getenv("APIGEO_TOKEN"),
            idOrigen, calleOrigen, alturaOrigen,
            idDestino, calleDestino, alturaDestino);

    try {
      Response<Distancia> responseDistancia = requestDistancia.execute();
      assert responseDistancia.body() != null;
      return responseDistancia.body().getValor();
    } catch (Exception e) {
      throw new ObtenerDistanciasException("No se puede ubicar al servicio de Geolocalizacion");
    }
  }
}
