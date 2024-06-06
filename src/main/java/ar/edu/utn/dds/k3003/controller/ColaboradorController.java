package ar.edu.utn.dds.k3003.controller;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.dtos.ColaboradorDTO;
import ar.edu.utn.dds.k3003.facades.dtos.FormaDeColaborarEnum;
import ar.edu.utn.dds.k3003.model.MapFormas;
import ar.edu.utn.dds.k3003.model.exceptions.ErrorConParametrosException;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class ColaboradorController {

  private final Fachada fachada;

  public ColaboradorController(Fachada fachada) {
    this.fachada = fachada;
  }

  public void agregar(Context context) {
  try {
    ColaboradorDTO colaboradorDTORta = this.fachada.agregar(context.bodyAsClass(ColaboradorDTO.class));
    context.json(colaboradorDTORta);
    context.status(HttpStatus.CREATED);
   // context.result("Colaborador agregado");
  } catch (NoSuchElementException e) {
    context.result(e.getLocalizedMessage());
    context.status(HttpStatus.BAD_REQUEST);
  }

  }
  public void obtenerColaborador(Context context) {
    var id = context.pathParamAsClass("id",Long.class).get();
    try {
      var colaboradorDTORta = this.fachada.buscarXId(id);
      context.json(colaboradorDTORta);
    } catch (NoSuchElementException ex){
      context.result(ex.getLocalizedMessage());
      context.status(HttpStatus.NOT_FOUND);
    }
  }

  public void modificarColaboracion(Context context) {
    Long id = context.pathParamAsClass("id",Long.class).get();
    FormaDeColaborarEnum[] list= context.bodyAsClass(FormaDeColaborarEnum[].class);//esta bien esto?
    try {
      ColaboradorDTO colaboradorDTORta = this.fachada.modificar(id, List.of(list));//CUIDADO SI LE AGREGAMOS FUNCONABILIDAD A FORMADECOLABORARENUM
      context.json(colaboradorDTORta);
      context.status(HttpStatus.CREATED);
    } catch (NoSuchElementException ex) {
      context.result(ex.getLocalizedMessage());
      context.status(HttpStatus.NOT_FOUND);
    }
  }

  public void obtenerPuntos(Context context) {

    Long id = context.pathParamAsClass("id",Long.class).get();
    try {
      Double puntos = this.fachada.puntos(id);
      context.json(puntos);
    } catch (NoSuchElementException ex){
      context.result(ex.getLocalizedMessage());
      context.status(HttpStatus.NOT_FOUND);
    }
  }
  public void actualizarPesosPuntos(Context context) {

    var mapa =context.bodyAsClass(Map.class);   //Funcionara?
    try {
      this.fachada.actualizarPesosPuntos((Double) mapa.get("pesosDonados"), (Double) mapa.get("viandasDistribuidas"), (Double) mapa.get("viandasDonadas"), (Double) mapa.get("tarjetasRepartidas"), (Double) mapa.get("heladerasActivas"));
      context.status(HttpStatus.CREATED);
      context.result("Puntos actualizados");
    }

    catch (ErrorConParametrosException ex)
    {
      context.result(ex.getLocalizedMessage());
      context.status(HttpStatus.CONFLICT);    //ESTA BIEN ESTO?
    }
  }
}