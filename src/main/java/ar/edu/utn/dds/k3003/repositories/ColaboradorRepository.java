package ar.edu.utn.dds.k3003.repositories;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import ar.edu.utn.dds.k3003.facades.dtos.FormaDeColaborarEnum;
import ar.edu.utn.dds.k3003.model.Colaborador;
import ar.edu.utn.dds.k3003.model.PesosPuntos;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static ar.edu.utn.dds.k3003.facades.dtos.FormaDeColaborarEnum.TRANSPORTADOR;

public class ColaboradorRepository {

    private PesosPuntos pesosPuntos;
    private EntityManagerFactory entityManagerFactory ;
    public ColaboradorRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public Colaborador save(Colaborador colaborador) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(colaborador);
        entityManager.getTransaction().commit();
        entityManager.close();
        return colaborador;
    }

    public Colaborador findById(Long id) throws NoSuchElementException{
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Colaborador colaborador= entityManager.find(Colaborador.class, id);
        entityManager.close();
        return colaborador;
    }

    public Colaborador cambiarFormas(Long id, List<FormaDeColaborarEnum> list) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Colaborador colaborador= entityManager.find(Colaborador.class, id);
        colaborador.setFormas(list);
        entityManager.getTransaction().commit();
        entityManager.close();
        return colaborador;
    }
    public Collection<Colaborador> getColaboradores() {return null;}

    public void borrarRepository() {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        try{
            em.createQuery("DELETE FROM Colaborador").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE colaborador_id_seq RESTART WITH 1").executeUpdate();
            em.getTransaction().commit();
        }catch(RuntimeException e){
            if(em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally{
            em.close();
        }
    }

    public void actualizarPesosPuntos(PesosPuntos pesosPuntos){
        this.pesosPuntos=pesosPuntos;

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
