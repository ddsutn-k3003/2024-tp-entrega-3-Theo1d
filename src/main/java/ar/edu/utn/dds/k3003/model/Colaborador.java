package ar.edu.utn.dds.k3003.model;

import java.util.*;

import ar.edu.utn.dds.k3003.facades.dtos.FormaDeColaborarEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name="colaborador")
public class Colaborador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Es como el id de las tarjetas
    @Column (name = "nombre")
    private String nombre;
/*
    @ElementCollection(targetClass = FormaDeColaborarEnum.class)
    @Enumerated(EnumType.STRING) // Puedes usar EnumType.ORDINAL si prefieres almacenar índices
    @CollectionTable(name = "colaborador_formas_de_colaborar", joinColumns = @JoinColumn(name = "colaborador_id"))
    @Column(name = "forma_de_colaborar")*/
    @Column(name="formas")
    @Convert(converter = ConversorFormasDeColaborar.class)
    private List<FormaDeColaborarEnum> formas;

    // private Long tarjetasRepartidas; En teoria no se usa esta entrega
    // private Long pesosDonados;
    public Colaborador(){}
    public Colaborador(Long id, String nombre,List<FormaDeColaborarEnum> list) {
        this.id = id;
        this.nombre = nombre;
        this.formas=list;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setFormas(List<FormaDeColaborarEnum> list) {
        this.formas = list;
    }

    public Long getId() {return this.id;}

    public List<FormaDeColaborarEnum> getFormas() {return this.formas;}

    public String getNombre() {
        return this.nombre;
    }

}
