package ec.net.cmd;

public class Command {

	private String uri = null;
	private String name = null;
	
	public Command(String uri,String name){
		this.uri = uri;
		this.name = name;
	}

	
	

	public String getUri() {
		return uri;
	}




	public String getName() {
		return name;
	}

}
