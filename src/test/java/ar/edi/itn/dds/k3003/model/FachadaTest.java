package ar.edi.itn.dds.k3003.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ar.edu.utn.dds.k3003.facades.FachadaColaboradores;
import ar.edu.utn.dds.k3003.facades.FachadaLogistica;
import ar.edu.utn.dds.k3003.facades.FachadaViandas;
import ar.edu.utn.dds.k3003.facades.dtos.*;
import ar.edu.utn.dds.k3003.tests.TestTP;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@ExtendWith({MockitoExtension.class})
public class FachadaTest implements TestTP<FachadaColaboradores> {
    FachadaColaboradores instancia;
    ColaboradorDTO colaborador0;
    @Mock
    FachadaLogistica fachadaLogistica;
    @Mock
    FachadaViandas fachadaViandas;

    public FachadaTest() {
    }

    @BeforeEach
    void setUp() throws Exception {
            this.instancia = (FachadaColaboradores)this.instance();
            this.colaborador0 = new ColaboradorDTO("theo", List.of(FormaDeColaborarEnum.DONADOR));
            this.instancia.setLogisticaProxy(this.fachadaLogistica);
            this.instancia.setViandasProxy(this.fachadaViandas);
    }

    @Test
    @DisplayName("Agregar y buscar colaborador")
    void testAgregarColaborador() {
        ColaboradorDTO colaboradorRta = this.instancia.agregar(this.colaborador0);
        Assertions.assertNotNull(colaboradorRta.getId(), "FachadaColaboradores#agregar debe retornar un ColaboradorDTO con un id inicializado.");
        this.instancia.agregar(new ColaboradorDTO("juan", List.of(FormaDeColaborarEnum.DONADOR)));
        ColaboradorDTO colaborador3 = this.instancia.buscarXId(colaboradorRta.getId());
        Objects.requireNonNull(this);
        Assertions.assertEquals("theo", colaborador3.getNombre(), "No se esta recuperando el nombre del colaborador correctamente.");
    }

    @Test
    @DisplayName("cambiando FormaDeColaborarEnum")
    void testModificarFormaDeColaborar() {
        ColaboradorDTO colaboradorRta = this.instancia.agregar(this.colaborador0);
        ColaboradorDTO colaboradorRta2 = this.instancia.modificar(colaboradorRta.getId(), List.of(FormaDeColaborarEnum.TRANSPORTADOR));
        Assertions.assertEquals(FormaDeColaborarEnum.TRANSPORTADOR, colaboradorRta2.getFormas().get(0), "No se actualizó la forma de colaborar.");
        ColaboradorDTO colaborador3 = this.instancia.buscarXId(colaboradorRta.getId());
        Assertions.assertEquals(FormaDeColaborarEnum.TRANSPORTADOR, colaborador3.getFormas().get(0), "No se esta guardando la forma de colaborar.");
    }

    @Test
    @DisplayName("Calculo de puntos")
    void testPuntos() {
        ColaboradorDTO colaboradorRta = this.instancia.agregar(this.colaborador0);
        this.instancia.actualizarPesosPuntos(0.5, 1.0, 1.5, 2.0, 5.0);
        Mockito.when(this.fachadaLogistica.trasladosDeColaborador(colaboradorRta.getId(), 1, 2024)).thenReturn(List.of());
        Mockito.when(this.fachadaViandas.viandasDeColaborador(colaboradorRta.getId(), 1, 2024)).thenReturn(List.of());
        Double puntos = this.instancia.puntos(colaboradorRta.getId());
        Assertions.assertEquals(0.0, puntos, "Si logistica y viandas no responden nada el puntaje deberia ser cero.");

        TrasladoDTO trasladoDTO = new TrasladoDTO("x", 18, 19);
        Mockito.when(this.fachadaLogistica.trasladosDeColaborador(colaboradorRta.getId(), 1, 2024)).thenReturn(List.of(trasladoDTO));
        ViandaDTO viandaDTO = new ViandaDTO("y", LocalDateTime.now(), EstadoViandaEnum.EN_TRASLADO, colaboradorRta.getId(), 20);
        Mockito.when(this.fachadaViandas.viandasDeColaborador(colaboradorRta.getId(), 1, 2024)).thenReturn(List.of(viandaDTO));
        puntos = this.instancia.puntos(colaboradorRta.getId());

        Assertions.assertEquals(1.5, puntos, "Si hay un traslado y una vianda el puntaje deberia ser 1.5.");
        Mockito.when(this.fachadaLogistica.trasladosDeColaborador(colaboradorRta.getId(), 1, 2024)).thenReturn(List.of());
        Mockito.when(this.fachadaViandas.viandasDeColaborador(colaboradorRta.getId(), 1, 2024)).thenReturn(List.of(viandaDTO));
        puntos = this.instancia.puntos(colaboradorRta.getId());
        Assertions.assertEquals(1.0, puntos, "Si hay una vianda y nada mas el puntaje deberia ser 1.");
    }

    public String paquete() {
        return "ar.edu.utn.dds.k3003.tests.colaboradores";
    }

    public Class<FachadaColaboradores> clase() {
        return FachadaColaboradores.class;
    }
}
