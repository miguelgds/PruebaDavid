package es.miguelgs.pruebadavid;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Estrategia {

	public static Collection<AccionDetalle> getAccionesDetalle(BigDecimal id,
			List<Object> codsEstrategia, String diaAccion,
			String diaAccionLlamada, String arquetipo) {
		
		return IntStream.rangeClosed(0, new Double(Math.random() * 10).intValue())
				 		.mapToObj(i -> AccionDetalle.builder()
						 							.accion(Accion.builder()
						 										  .codAccion("" + id.intValue() + " - " + i)
						 										  .build())
						 							.build())
						.collect(Collectors.toList());
	}
}
