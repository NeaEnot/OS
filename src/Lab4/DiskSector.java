package Lab4;

public class DiskSector {
	private final int size;
	
	private String data;
	
	public DiskSector(int size) {
		this.size = size;
		data = "";
	}
	
	public void write(String data) {
		this.data = data.substring(0, data.length() <= size ? data.length() : size);
	}
	
	public String read() {
		return new String(data);
	}

}
