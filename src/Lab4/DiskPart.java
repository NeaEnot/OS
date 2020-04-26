package Lab4;

public class DiskPart {
	private DiskSector[] sectors;
	
	public DiskPart(int size, int sectorSize) {
		sectors = new DiskSector[size];
		for (int i = 0; i < size; i++) {
			sectors[i] = new DiskSector(sectorSize);
		}
	}
	
	public void writeDataIntoSector(int index, String data) {
		if (index >= 0 && index < sectors.length) {
			sectors[index].write(data);
		}
	}
	
	public String readDataFromSector(int index) {
		if (index >= 0 && index < sectors.length) {
			return sectors[index].read();
		} else {
			throw new IndexOutOfBoundsException();
		}
	}
	
}
