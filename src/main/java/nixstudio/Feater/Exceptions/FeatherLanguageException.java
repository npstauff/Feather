package nixstudio.Feater.Exceptions;

public class FeatherLanguageException extends Exception{
	String message = "";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public FeatherLanguageException(String message) {
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}
}
