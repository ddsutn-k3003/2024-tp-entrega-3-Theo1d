package ar.edu.utn.dds.k3003.repositories;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import ar.edu.utn.dds.k3003.facades.dtos.FormaDeColaborarEnum;
import ar.edu.utn.dds.k3003.model.Colaborador;
import ar.edu.utn.dds.k3003.model.PesosPuntos;
import javax.persistence.EntityManager;

import static ar.edu.utn.dds.k3003.facades.dtos.FormaDeColaborarEnum.TRANSPORTADOR;

public class ColaboradorRepository {

    private PesosPuntos pesosPuntos=null;
    private EntityManager entityManager ;

    public ColaboradorRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Colaborador save(Colaborador colaborador) {
        entityManager.getTransaction().begin();
        entityManager.persist(colaborador);
        entityManager.getTransaction().commit();

        return colaborador;
    }

    public Colaborador findById(Long id) throws NoSuchElementException{
        return this.entityManager.find(Colaborador.class, id);
    }

    public Colaborador cambiarFormas(Long id, List<FormaDeColaborarEnum> list) {
        entityManager.getTransaction().begin();
        Colaborador colaborador= entityManager.find(Colaborador.class, id);
        colaborador.setFormas(list);
        entityManager.getTransaction().commit();
        return colaborador;
    }
    public Collection<Colaborador> getColaboradores() {return null;}

    public void borrarRepository() {

        try{
            entityManager.getTransaction().begin();
            entityManager.createQuery("DELETE FROM Colaborador ");
            entityManager.getTransaction().commit();
        }
        catch (RuntimeException e){
            if(entityManager.getTransaction().isActive()) entityManager.getTransaction().rollback();
            throw e;
        }

      //  entityManager.clear();
    }

    public void actualizarPesosPuntos(PesosPuntos pesosPuntos){


        /*try
        {
        PesosPuntos pesosPuntos0=entityManager.find(PesosPuntos.class,1L);
        pesosPuntos0=pesosPuntos;
        }
        catch (Exception e) {
            entityManager.persist(pesosPuntos);
        }
            entityManager.getTransaction().commit();*/
    }

    public Double getPesosDonados() {
       // PesosPuntos pesosPuntos = entityManager.find(PesosPuntos.class,1);
        return pesosPuntos.getPesosDonados();
    }
    public Double getViandas_Distribuidas() {

      //  PesosPuntos pesosPuntos = entityManager.find(PesosPuntos.class,1);
        return pesosPuntos.getViandas_Distribuidas();
    }
    public Double getViandasDonadas() {

   //     PesosPuntos pesosPuntos = entityManager.find(PesosPuntos.class,1);
        return pesosPuntos.getViandasDonadas();
    }
    public Double getTarjetasRepartidas() {

     //   PesosPuntos pesosPuntos = entityManager.find(PesosPuntos.class,1);
        return pesosPuntos.getTarjetasRepartidas();
    }

    public Double getHeladerasActivas() {
    //    PesosPuntos pesosPuntos = entityManager.find(PesosPuntos.class,1);
        return pesosPuntos.getHeladerasActivas();
    }
}
