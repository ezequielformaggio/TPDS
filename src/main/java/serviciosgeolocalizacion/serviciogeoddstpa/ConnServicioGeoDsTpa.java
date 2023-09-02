package serviciosgeolocalizacion.serviciogeoddstpa;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import serviciosgeolocalizacion.serviciogeoddstpa.entidades.Distancia;

public interface ConnServicioGeoDsTpa {
  @GET("distancia")
  Call<Distancia> distancia(@Header("authorization") String authHeader,
                            @Query("localidadOrigenId") int idOrigen,
                            @Query("calleOrigen") String calleOrigen,
                            @Query("alturaOrigen") int alturaOrigen,
                            @Query("localidadDestinoId") int idDestino,
                            @Query("calleDestino") String calleDestino,
                            @Query("alturaDestino") int alturaDestino);
}
