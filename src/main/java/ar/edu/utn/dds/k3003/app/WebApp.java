package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.clients.LogisticaProxy;
import ar.edu.utn.dds.k3003.clients.ViandasProxy;
import ar.edu.utn.dds.k3003.facades.dtos.Constants;
import ar.edu.utn.dds.k3003.facades.dtos.FormaDeColaborarEnum;
import ar.edu.utn.dds.k3003.model.Colaborador;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import ar.edu.utn.dds.k3003.controller.ColaboradorController;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.text.SimpleDateFormat;
import java.util.*;

import static ar.edu.utn.dds.k3003.facades.dtos.FormaDeColaborarEnum.TRANSPORTADOR;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class WebApp {
    public static EntityManagerFactory entityManagerFactory;
    public static void main(String[] args) {
        startEntityManagerFactory();
        EntityManager entityManager= entityManagerFactory.createEntityManager();
        var env = System.getenv();

        // Variables de entorno
        var URL_VIANDAS = env.get("URL_VIANDAS");
        var URL_LOGISTICA = env.get("URL_LOGISTICA");
        var URL_HELADERAS = env.get("URL_HELADERAS");
        var URL_COLABORADORES = env.get("URL_COLABORADORES");

        ObjectMapper objectMapper = createObjectMapper();
        var fachada = new Fachada(entityManager);


        var port = Integer.parseInt(env.getOrDefault("PORT", "8080"));

        fachada.setLogisticaProxy(new LogisticaProxy(objectMapper));
        fachada.setViandasProxy(new ViandasProxy(objectMapper));
        var colaboradorController = new ColaboradorController(fachada,entityManagerFactory);

        var app = Javalin.create(config -> {
            config.jsonMapper(new JavalinJackson().updateMapper(WebApp::configureObjectMapper));
        }).start(port);



        app.post("/colaboradores", colaboradorController::agregar);
        app.get("/colaboradores/{id}", colaboradorController::obtenerColaborador);
        app.patch("/colaboradores/{id}", colaboradorController::modificarColaboracion);
        app.get("/colaboradores/{id}/puntos", colaboradorController::obtenerPuntos);
        app.put("/formula", colaboradorController::actualizarPesosPuntos);
        app.delete("/clear",colaboradorController::clean);
    }

    public static void startEntityManagerFactory() {
// https://stackoverflow.com/questions/8836834/read-environment-variables-in-persistence-xml-file
        Map<String, String> env = System.getenv();
        Map<String, Object> configOverrides = new HashMap<String, Object>();
        String[] keys = new String[] { "javax.persistence.jdbc.url", "javax.persistence.jdbc.user",
                "javax.persistence.jdbc.password", "javax.persistence.jdbc.driver", "hibernate.hbm2ddl.auto",
                "hibernate.connection.pool_size", "hibernate.show_sql" };
        for (String key : keys) {
            if (env.containsKey(key)) {
                String value = env.get(key);
                configOverrides.put(key, value);
            }
        }
        entityManagerFactory = Persistence.createEntityManagerFactory("db", configOverrides);
    }

    public static ObjectMapper createObjectMapper() {
        var objectMapper = new ObjectMapper();
        configureObjectMapper(objectMapper);
        return objectMapper;
    }

    public static void configureObjectMapper(ObjectMapper objectMapper) {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        var sdf = new SimpleDateFormat(Constants.DEFAULT_SERIALIZATION_FORMAT, Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        objectMapper.setDateFormat(sdf);
    }
}
