package es.miguelgs.pruebadavid;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
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
		
		Printer.printMessage("INICIO TEST");
		
		CountDownLatch c = new CountDownLatch(1);
		this.procesarDeudas(idsDeuda) 
			.subscribe(codAccionInsertada -> {},
					   t -> {t.printStackTrace(); c.countDown();},
				       () -> c.countDown());	
		
		c.await();
		
		Printer.printMessage("FIN TEST");
	}
	
	private Observable<String> procesarDeudas(Collection<BigDecimal> idsDeuda){
		return Observable.from(idsDeuda)	
						 .flatMap(id -> this.procesarDeuda(id)
								 			.subscribeOn(Schedulers.io()) // CON ESTE UTILIZA TANTOS THREADS COMO SEA NECESARIO Y CUANDO TERMINAN LOS MANTIENE PARA REUTILIZAR
								 			//.subscribeOn(Schedulers.computation()) // CON ESTE UTILIZA TANTOS THREADS COMO CORES TENGA LA MAQUINA
		  );
	}
	
	private Observable<String> procesarDeuda(BigDecimal idDeuda){
		return Observable.just(DatosDeDeuda.getDiasAccionDeuda(idDeuda))						  				
						 .map(datosDeuda -> Estrategia.getAccionesDetalle(idDeuda, 
				   											codsEstrategia, 
				   											datosDeuda.getDiaAccion(), 
				   											datosDeuda.getDiaAccionLlamada(), 
				   											datosDeuda.getArquetipo()))
						 .map(listaAccionesDetalle -> insertarInBBDD(listaAccionesDetalle, "FILTRO", idDeuda))
						 .flatMapIterable(x -> x)
						 .map(accionDetalle -> insertarFilaFichero(accionDetalle.getAccion().getCodAccion(), idDeuda, "SHEET"));
	}
	
	private String insertarFilaFichero(String codAccion, BigDecimal id, String sheet){
		Printer.printMessage("INSERCION EN FICHERO: codAccion: %s, id: %f, sheet: %s", codAccion, id, sheet);
		try {
			TimeUnit.MILLISECONDS.sleep(10);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		return codAccion;
	}
	
	private Collection<AccionDetalle> insertarInBBDD(Collection<AccionDetalle> accionesCalculadas, String filtro, BigDecimal id){
		Printer.printMessage("INSERCION EN BBDD: accionesCalculadas: %s, filtro: %s, id: %f", accionesCalculadas, filtro, id);
		try {
			TimeUnit.MILLISECONDS.sleep(20 * accionesCalculadas.size());
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		return accionesCalculadas;
	}
}
