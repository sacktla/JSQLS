import java.io.IOException;
public class Test{

	public static void main(String[] args) throws JSQLException{
		JSQLTable table;
		try{
			table = new JSQLTable("test.csv","TableA");

		
		JSQLTable tableB = new JSQLTable("TableB");
		JSQLDataBase database = new JSQLDataBase("Database");
		
		String[] headers = {"HeaderA","FLAT"};
		String[] row1 = {"1","5"};
		String[] row2 = {"7","9"};
		String[] row3 = {"11","15"};
		String[] row4 = {"12","20"};

		tableB.setHeaders(headers);
		tableB.insert(row1);
		tableB.insert(row2);
		tableB.insert(row3);
		tableB.insert(row4);

		database.addTable(table);
		database.addTable(tableB);


		JSQLTable mergedTables = database.joinTwoTables("Merged", "TableA", "TableB", "HeaderA");

		String[] lines = mergedTables.selectAll();
		for(String line:lines){
			System.out.println(line);
		}
		}catch(IOException f){
			
		}
	}

}
