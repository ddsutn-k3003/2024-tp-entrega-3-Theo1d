package ar.edu.utn.dds.k3003.clients;

import ar.edu.utn.dds.k3003.facades.FachadaHeladeras;
import ar.edu.utn.dds.k3003.facades.FachadaLogistica;
import ar.edu.utn.dds.k3003.facades.FachadaViandas;
import ar.edu.utn.dds.k3003.facades.dtos.RutaDTO;
import ar.edu.utn.dds.k3003.facades.dtos.TrasladoDTO;
import ar.edu.utn.dds.k3003.facades.dtos.ViandaDTO;
import ar.edu.utn.dds.k3003.facades.exceptions.TrasladoNoAsignableException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.HttpStatus;
import lombok.SneakyThrows;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class LogisticaProxy implements FachadaLogistica {

    private final String endpoint;
    private final LogisticaRetrofitClient service;

    public LogisticaProxy(ObjectMapper objectMapper) {
        var env = System.getenv();
        this.endpoint = env.getOrDefault("URL_LOGISTICA", "http://localhost:8082/");

        var retrofit =
                new Retrofit.Builder()
                        .baseUrl(this.endpoint)
                        .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                        .build();

        this.service = retrofit.create(LogisticaRetrofitClient.class);

    }
    @Override
    public RutaDTO agregar(RutaDTO rutaDTO) {
        return null;
    }

    @Override
    public TrasladoDTO buscarXId(Long aLong) throws NoSuchElementException {
        return null;
    }

    @Override
    public TrasladoDTO asignarTraslado(TrasladoDTO trasladoDTO) throws TrasladoNoAsignableException {
        return null;
    }

    @SneakyThrows
    @Override
    public List<TrasladoDTO> trasladosDeColaborador(Long id, Integer mes, Integer anio) throws NoSuchElementException{
        Response<List<TrasladoDTO>> execute = service.getTraslados(id, mes, anio).execute();
        if (execute.isSuccessful()) {
            var trasladoPrueba=new TrasladoDTO(null,null,null,null,null);
            List<TrasladoDTO> respuesta =new ArrayList<>();
            respuesta.add(trasladoPrueba);
            return respuesta;
        }
        if (execute.code() == HttpStatus.NOT_FOUND.getCode()) {
            throw new NoSuchElementException("No se encontraron traslados");
        }
        throw new RuntimeException("Error conectandose con el componente logistica");
    }

    @Override
    public void setHeladerasProxy(FachadaHeladeras fachadaHeladeras) {

    }

    @Override
    public void setViandasProxy(FachadaViandas fachadaViandas) {

    }

    @Override
    public void trasladoRetirado(Long aLong) {

    }

    @Override
    public void trasladoDepositado(Long aLong) {

    }
}
