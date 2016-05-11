import java.io.IOException;
public class Test{

	public static void main(String[] args) throws IOException{
		JSQLTable table = new JSQLTable("test.csv","TEST_TABLE");
		String[] tableParam = {"HeaderA","HeaderC"};
		String[] testLines = table.selectWhereNotEqual(tableParam,"HeaderA", "6");
		
		for(String line : testLines){
			System.out.println(line);
		}

		String[] addLines = {"16","17", "18", "19", "20"};

		table.insert(addLines);

		String[] testLines2 = table.selectWhereNotEqual(tableParam,"HeaderA","6");
		for(String line:testLines2){
			System.out.println(line);
		}
	}

}
