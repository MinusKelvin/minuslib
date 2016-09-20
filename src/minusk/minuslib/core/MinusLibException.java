package minusk.minuslib.core;

/**
 * Created by MinusKelvin on 3/17/16.
 */
public class MinusLibException extends RuntimeException {
	public MinusLibException() {
		super();
	}
	public MinusLibException(String message) {
		super(message);
	}
	public MinusLibException(Throwable cause) {
		super(cause);
	}
	public MinusLibException(String message, Throwable cause) {
		super(message, cause);
	}
	public MinusLibException(String message, Throwable cause, boolean enableSuppression, boolean writeableStackTrace) {
		super(message, cause, enableSuppression, writeableStackTrace);
	}
}
