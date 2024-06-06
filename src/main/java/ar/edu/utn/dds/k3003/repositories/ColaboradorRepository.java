package ar.edu.utn.dds.k3003.repositories;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import ar.edu.utn.dds.k3003.model.Colaborador;

public class ColaboradorRepository {

    public Collection<Colaborador> colaboradores=new ArrayList<>();
    private static AtomicLong seqId = new AtomicLong(); // empieza en 0
    private Double pesosDonados;
    private Double viandas_Distribuidas;
    private Double viandasDonadas;
    private Double tarjetasRepartidas;
    private Double heladerasActivas;

    public ColaboradorRepository() {

    }

    public Colaborador save(Colaborador colaborador) {
        if (Objects.isNull(colaborador.getId())) {
            colaborador.setId(seqId.getAndIncrement());
        }
        this.colaboradores.add(colaborador);
        return colaborador;
    }

    public Colaborador findById(Long id) throws NoSuchElementException{
        Optional<Colaborador> first = this.colaboradores.stream().filter(x -> x.getId().equals(id)).findFirst();
        return first
                .orElseThrow(() -> new NoSuchElementException(String.format("no hay un colaborador de id: %s", id)));
    }
    public void actualizarPesosPuntos(Double pesosDonados, Double viandas_Distribuidas, Double viandasDonadas,Double tarjetasRepartidas, Double heladerasActivas){
        this.pesosDonados=pesosDonados;
        this.viandas_Distribuidas=viandas_Distribuidas;
        this.viandasDonadas=viandasDonadas;
        this.tarjetasRepartidas=tarjetasRepartidas;
        this.heladerasActivas=heladerasActivas;
    }
    public Double getPesosDonados() {
        return this.pesosDonados;
    }
    public Double getViandas_Distribuidas() {
        return this.viandas_Distribuidas;
    }
    public Double getViandasDonadas() {
        return this.viandasDonadas;
    }
    public Double getTarjetasRepartidas() {
        return this.tarjetasRepartidas;
    }
    public Double getHeladerasActivas() {
        return this.heladerasActivas;
    }
    public Collection<Colaborador> getColaboradores() {return this.colaboradores;}
}
