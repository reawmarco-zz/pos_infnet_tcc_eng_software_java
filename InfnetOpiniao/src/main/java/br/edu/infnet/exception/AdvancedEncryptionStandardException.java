package br.edu.infnet.exception;

public class AdvancedEncryptionStandardException extends Exception{

	private static final long serialVersionUID = 4931365464072614756L;
	
	public AdvancedEncryptionStandardException(Throwable exp) {
		super(exp);
	}
	
	public AdvancedEncryptionStandardException(String message) {
		super(message);
	}

}
