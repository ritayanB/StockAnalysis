package org.csf.exception;

public class NoDataFoundException extends Throwable {

	/**
	 * 
	 */

	private static final long serialVersionUID = 9003198866792270821L;

	Long key;
	String message;

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public NoDataFoundException() {
		// TODO Auto-generated constructor stub
	}

	public NoDataFoundException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public NoDataFoundException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public NoDataFoundException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public NoDataFoundException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
	}

}
