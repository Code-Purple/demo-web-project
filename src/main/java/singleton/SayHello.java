package singleton;

public class SayHello {
	private static SayHello instance = null;
	
	private SayHello() {} // local constructor
	
	public static SayHello getInstance() {
		if(instance == null) {
			instance = new SayHello();
		}
		return instance;
	}
	public String getHello() {
		return "Hello";
	}
}
