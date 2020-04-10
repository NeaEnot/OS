package Lab3;

public class MemoryManager {
	private Memory HDD;
	private Memory RAM;
	
	private PagesTable table;
	private Program process;
	
	private int pageSize;
	
	public MemoryManager(Memory HDD, Memory RAM, int pageSize, int programSize) {
		this.HDD = HDD;
		this.RAM = RAM;
		this.pageSize = pageSize;

		process = new Program(programSize);
		table = new PagesTable(HDD.getSize() / pageSize + 1, RAM.getSize() / pageSize + 1);
		
		table.nextCicle();
	}
	
	public void invoke(int instructionsCount) {
		for (int i = 0; i < instructionsCount; i++) {
			Instruction instruction = process.nextInstruction();
			
			int virtualAddres = instruction.getAddres();
			int virtualPageNumber = virtualAddres / pageSize;
			int offset = virtualAddres % pageSize;
			
			log("������� ��������� � ������������ ������ " + virtualAddres + " ����������� �������� " + virtualPageNumber);
			
			int realPageNumber = table.getMap(virtualPageNumber);
			
			if (realPageNumber == -1) {
				log("��������������� �������� ��� � ����������� ������");
				
				if (table.isFullUsed()) {
					int virtualLessDemandedPageNumber = table.getLessDemandedPageNumber();
					int realLessDemandedPageNumber = table.getMap(virtualLessDemandedPageNumber);
					
					log("�������� ������������ � ������ �������� " + realLessDemandedPageNumber);
					
					Page loadedPage = HDD.getPage(virtualPageNumber);
					RAM.setPage(loadedPage, realLessDemandedPageNumber);
					
					log("�� ����� ��������� �������� " + virtualPageNumber);
					
					table.reset(virtualLessDemandedPageNumber);
					table.set(virtualPageNumber, realLessDemandedPageNumber);
					
					log("����������� ������������ ����������� �������� " + virtualPageNumber + 
						" � �������� " + realLessDemandedPageNumber + " � ����������� ������");
					
					realPageNumber = realLessDemandedPageNumber;
				} else {
					log("� ����������� ������ ���� ��������� �����");
					
					int freePlaceNumber = table.getFreePlaceMapped();
					
					Page loadedPage = HDD.getPage(virtualPageNumber);
					RAM.setPage(loadedPage, freePlaceNumber);
					
					log("�� ����� ��������� �������� " + virtualPageNumber);
					
					table.set(virtualPageNumber, freePlaceNumber);
					
					log("����������� ������������ ����������� �������� " + virtualPageNumber + 
						" � �������� " + freePlaceNumber + " � ����������� ������");
					
					realPageNumber = freePlaceNumber;
				}
			}
			
			int realAddres = pageSize * realPageNumber + offset;
			
			log("����������� ����� ������������� ����������� ����� " + realAddres + " ���������� �������� " + realPageNumber);

			table.usingPage(virtualPageNumber);
			
			log("������� ��������� ����������� ������:");
			log(RAM.getPagesList());
			
			if (i % 4 == 3) {
				table.nextCicle();
				log("������ ���� ��������");
				log("");
			}
		}
	}
	
	private void log(String message) {
		System.out.println(message);
	}
}
