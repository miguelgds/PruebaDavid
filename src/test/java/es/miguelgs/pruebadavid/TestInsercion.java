package es.miguelgs.pruebadavid;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.junit.Test;

import rx.Observable;
import rx.schedulers.Schedulers;

public class TestInsercion {

	private static final List<BigDecimal> idsDeuda = Arrays.asList(new BigDecimal(1f), new BigDecimal(2f), new BigDecimal(3f));
	private static final List<Object> codsEstrategia = Collections.emptyList();
	
	@Test
	public void test() throws InterruptedException{
		CountDownLatch c = new CountDownLatch(idsDeuda.size());
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
				  .subscribe(insertadoEnFichero -> c.countDown(),
						     Throwable::printStackTrace,
						     () -> Printer.printMessage("Terminado el proceso"));	
		
		c.await();
	}
	
	private Boolean insertarFilaFichero(String codAccion, BigDecimal id, String sheet){
		Printer.printMessage("INSERCION EN FICHERO: codAccion: %s, id: %f, sheet: %s", codAccion, id, sheet);
		return true;
	}
	
	private Collection<AccionDetalle> insertarInBBDD(Collection<AccionDetalle> accionesCalculadas, String filtro, BigDecimal id){
		Printer.printMessage("INSERCION EN BBDD: accionesCalculadas: %s, filtro: %s, id: %f", accionesCalculadas, filtro, id);
		return accionesCalculadas;
	}
}
