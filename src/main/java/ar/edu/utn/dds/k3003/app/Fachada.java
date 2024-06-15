package ar.edu.utn.dds.k3003.app;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import ar.edu.utn.dds.k3003.facades.FachadaColaboradores;
import ar.edu.utn.dds.k3003.facades.FachadaLogistica;
import ar.edu.utn.dds.k3003.facades.FachadaViandas;
import ar.edu.utn.dds.k3003.facades.dtos.ColaboradorDTO;
import ar.edu.utn.dds.k3003.facades.dtos.FormaDeColaborarEnum;
import ar.edu.utn.dds.k3003.model.Colaborador;
import ar.edu.utn.dds.k3003.model.PesosPuntos;
import ar.edu.utn.dds.k3003.model.exceptions.ErrorConParametrosException;
import ar.edu.utn.dds.k3003.repositories.ColaboradorMapper;
import ar.edu.utn.dds.k3003.repositories.ColaboradorRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public class Fachada implements FachadaColaboradores {

    public final ColaboradorRepository colaboradorRepository;
    private final ColaboradorMapper colaboradorMapper;
    private FachadaLogistica logisticaFachada;
    private FachadaViandas viandasFachada;
    final LocalDateTime now = LocalDateTime.now();
    public static EntityManagerFactory entityManagerFactory;


    public  Fachada() {     //pónerle void?
        startEntityManagerFactory();
        EntityManager entityManager= entityManagerFactory.createEntityManager();
        this.colaboradorRepository =new ColaboradorRepository(entityManagerFactory);
        this.colaboradorMapper=new ColaboradorMapper();
    }

    @Override
    public ColaboradorDTO agregar(ColaboradorDTO colaboradorDTO) {

        Colaborador colaborador = new Colaborador(colaboradorDTO.getId(),colaboradorDTO.getNombre(), colaboradorDTO.getFormas()
        );
        colaborador = this.colaboradorRepository.save(colaborador);
        return colaboradorMapper.map(colaborador);
    }

    @Override
    public void actualizarPesosPuntos(Double viandas_Distribuidas, Double viandasDonadas,Double pesosDonados,
                                      Double tarjetasRepartidas, Double heladerasActivas) throws ErrorConParametrosException {
    this.colaboradorRepository.actualizarPesosPuntos(new PesosPuntos(pesosDonados,viandas_Distribuidas,viandasDonadas,tarjetasRepartidas,heladerasActivas));
    }

    @Override
    public Double puntos(Long colaboradorId) throws NoSuchElementException{
        Double puntos = 0.0;
        Colaborador colaborador = colaboradorRepository.findById(colaboradorId);

         //   Double pesoViandasDon=colaboradorRepository.getViandasDonadas();
         //   puntos= pesoViandasDon * this.viandasFachada.viandasDeColaborador(colaboradorId, 1, 2024).size();

            Double pesoViandasDist=colaboradorRepository.getViandas_Distribuidas();
            puntos+= pesoViandasDist * this.logisticaFachada.trasladosDeColaborador(colaboradorId, 1 , 2024).size();
      //  }
        return puntos;
    }

    @Override
    public ColaboradorDTO modificar(Long id, List<FormaDeColaborarEnum> list) throws NoSuchElementException {
        Colaborador colaborador = colaboradorRepository.cambiarFormas(id,list);
        return colaboradorMapper.map(colaborador);
    }

    @Override
    public ColaboradorDTO buscarXId(Long colaboradorId) throws NoSuchElementException {
        Colaborador colaborador = colaboradorRepository.findById(colaboradorId);
        return colaboradorMapper.map(colaborador);
    }
    @Override
    public void setLogisticaProxy(FachadaLogistica logistica) {this.logisticaFachada=logistica;}

    @Override
    public void setViandasProxy(FachadaViandas viandas) {this.viandasFachada=viandas;}

    public void borrarDB() {
        colaboradorRepository.borrarRepository();
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
}

