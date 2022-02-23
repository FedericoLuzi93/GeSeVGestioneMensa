package it.gesev.mensa.exc;

import org.springframework.http.HttpStatus;

public class GesevException extends RuntimeException
{

	private static final long serialVersionUID = 7854865386639091308L;
	private HttpStatus status;
	
	public GesevException(String message)
	{
		super(message);
	}
	
	public GesevException(String message, HttpStatus status)
	{
		super(message);
		this.status = status;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}
}
