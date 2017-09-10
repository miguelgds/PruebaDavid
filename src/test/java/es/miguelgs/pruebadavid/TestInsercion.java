package es.miguelgs.pruebadavid;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;

import rx.Observable;
import rx.schedulers.Schedulers;

public class TestInsercion {

	private static final List<BigDecimal> idsDeuda = IntStream.range(1, 1000)
															  .mapToObj(BigDecimal::new)
															  .collect(Collectors.toList());
	private static final List<Object> codsEstrategia = Collections.emptyList();
	
	@Test
	public void test() throws InterruptedException{
		
		CountDownLatch c = new CountDownLatch(1);
		Observable.from(idsDeuda)	
				  .observeOn(Schedulers.io())
				  .flatMap(id -> Observable.just(DatosDeDeuda.getDiasAccionDeuda(id))						  				
						  				.map(datosDeuda -> Estrategia.getAccionesDetalle(id, 
						  					   											codsEstrategia, 
						  					   											datosDeuda.getDiaAccion(), 
						  					   											datosDeuda.getDiaAccionLlamada(), 
						  					   											datosDeuda.getArquetipo()))
									   .observeOn(Schedulers.io())
						  			   .map(listaAccionesDetalle -> insertarInBBDD(listaAccionesDetalle, "FILTRO", id))
						  			   .flatMapIterable(x -> x)
						  			   .map(accionDetalle -> insertarFilaFichero(accionDetalle.getAccion().getCodAccion(), id, "SHEET"))) // 
				  .subscribe(codAccionInsertada -> {},
						     t -> {t.printStackTrace(); c.countDown();},
						     () -> {Printer.printMessage("Terminado el proceso"); c.countDown();});	
		
		c.await();
	}
	
	private String insertarFilaFichero(String codAccion, BigDecimal id, String sheet){
		Printer.printMessage("INSERCION EN FICHERO: codAccion: %s, id: %f, sheet: %s", codAccion, id, sheet);
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			System.out.println(e);
		}
		return codAccion;
	}
	
	private Collection<AccionDetalle> insertarInBBDD(Collection<AccionDetalle> accionesCalculadas, String filtro, BigDecimal id){
		Printer.printMessage("INSERCION EN BBDD: accionesCalculadas: %s, filtro: %s, id: %f", accionesCalculadas, filtro, id);
		try {
			Thread.sleep(20 * accionesCalculadas.size());
		} catch (InterruptedException e) {
			System.out.println(e);
		}
		return accionesCalculadas;
	}
}
