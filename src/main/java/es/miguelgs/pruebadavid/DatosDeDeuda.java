package es.miguelgs.pruebadavid;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DatosDeDeuda {
	private String diaAccion;
	private String diaAccionLlamada;
	private String arquetipo;
	
	public static DatosDeDeuda getDiasAccionDeuda(BigDecimal id){
		int val = id.toBigInteger().intValue();
		return DatosDeDeuda.builder()
						   .diaAccion(String.valueOf(val))
						   .diaAccionLlamada(String.valueOf(val + 1))
						   .arquetipo("Arquetipo: " + val)
						   .build();
	}
}
