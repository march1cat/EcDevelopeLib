package ec.event;

public class EcEvent<T> {
	
	private T data;
	
	public void setEventData(T data) {
		this.data = data;
	}
	
	public T getData() {
		return data;
	}

}
