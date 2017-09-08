package es.miguelgs.pruebadavid;

public class Printer {

	private static final long startTime = System.currentTimeMillis();
	
	public static String getMessage(String template, Object... args){
		return getSecondsElapsed() + " | " + Thread.currentThread().getName() + " | " +  String.format(template, args);
	}
	
	public static void printMessage(String template, Object... args){
		System.out.println(getMessage(template, args));
	}
	
	private static float getSecondsElapsed(){
		return (System.currentTimeMillis() - startTime) / (float) 1000;
	}
}
