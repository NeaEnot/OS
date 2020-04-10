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
			
			log("Процесс обратился к виртуальному адресу " + virtualAddres + " виртуальной страницы " + virtualPageNumber);
			
			int realPageNumber = table.getMap(virtualPageNumber);
			
			if (realPageNumber == -1) {
				log("Соответствующей страницы нет в оперативной памяти");
				
				if (table.isFullUsed()) {
					int virtualLessDemandedPageNumber = table.getLessDemandedPageNumber();
					int realLessDemandedPageNumber = table.getMap(virtualLessDemandedPageNumber);
					
					log("Наименее востребована в памяти страница " + realLessDemandedPageNumber);
					
					Page loadedPage = HDD.getPage(virtualPageNumber);
					RAM.setPage(loadedPage, realLessDemandedPageNumber);
					
					log("Из диска загружена страница " + virtualPageNumber);
					
					table.reset(virtualLessDemandedPageNumber);
					table.set(virtualPageNumber, realLessDemandedPageNumber);
					
					log("Установлено соответствие виртуальной страницы " + virtualPageNumber + 
						" и страницы " + realLessDemandedPageNumber + " в оперативной памяти");
					
					realPageNumber = realLessDemandedPageNumber;
				} else {
					log("В оперативной памяти есть свободное место");
					
					int freePlaceNumber = table.getFreePlaceMapped();
					
					Page loadedPage = HDD.getPage(virtualPageNumber);
					RAM.setPage(loadedPage, freePlaceNumber);
					
					log("Из диска загружена страница " + virtualPageNumber);
					
					table.set(virtualPageNumber, freePlaceNumber);
					
					log("Установлено соответствие виртуальной страницы " + virtualPageNumber + 
						" и страницы " + freePlaceNumber + " в оперативной памяти");
					
					realPageNumber = freePlaceNumber;
				}
			}
			
			int realAddres = pageSize * realPageNumber + offset;
			
			log("Виртуальный адрес соответствует физическому адесу " + realAddres + " физической страницы " + realPageNumber);

			table.usingPage(virtualPageNumber);
			
			log("Текущее состояние оперативной памяти:");
			log(RAM.getPagesList());
			
			if (i % 4 == 3) {
				table.nextCicle();
				log("Прошёл цикл старения");
				log("");
			}
		}
	}
	
	private void log(String message) {
		System.out.println(message);
	}
}
