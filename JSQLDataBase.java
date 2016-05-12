/*
This class will represent an entire data base formed by different JSQLTables.
A good idea is to have a way to link the two of them asides from having them 
be a part of the class
*/
import java.util.HashMap;
import java.util.Arrays;
import java.io.IOException;
public class JSQLDataBase{
	private HashMap<String,JSQLTable> tables;
	private String dataBaseName;
	//this one needs to have methods for combining the data frames and printing them out

	public JSQLDataBase(String dataBaseName){
		this.tables = new HashMap<String,JSQLTable>();
		this.dataBaseName = dataBaseName;
	}

	public void addTable(JSQLTable table){
		String tableName = table.getTableName();
		this.tables.put(tableName,table);
	}

	public void addTable(String tableName){
		JSQLTable tableToAdd = new JSQLTable(tableName);
		this.tables.put(tableName,tableToAdd);
	}

	public void insertIntoTable(String tableName, String[] values)throws IOException{
		Object[] keys = this.tables.keySet().toArray();
		if(Arrays.asList(keys).contains(tableName)){
			this.tables.get(tableName).insert(values);
		}else{
			System.out.println("Table $ does not exist in database".replace("$",tableName));
			throw new IOException();
		}
	}

	public void addHeaders(String tableName, String[] headers) throws IOException{
		Object[] keys = this.tables.keySet().toArray();
		if(Arrays.asList(keys).contains(tableName)){
			this.tables.get(tableName).setHeaders(headers);
		}else{
			System.out.println("Table $ does not exist in database".replace("$",tableName));
			throw new IOException();
		}
	}

	public String[][] selectAllTables() throws IOException{
		Object[] keys = this.tables.keySet().toArray();
		Arrays.sort(keys);
		int numberOfTables = this.getNumberOfTables();
		String[][] tablesMerged = new String[numberOfTables][];
		for(int i = 0; i < keys.length; i++){
			tablesMerged[i] = this.tables.get(keys[i]).selectAll();
		}

		return tablesMerged;
	}

	public String[][] selectAllFromTables(String[] tableNames) throws IOException{
		Object[] keys = this.tables.keySet().toArray();
		Arrays.sort(keys);
		String[][] tablesMerged = new String[tableNames.length][];
		for(int i = 0; i < tableNames.length; i++){
			if(Arrays.asList(keys).contains(tableNames[i])){
				tablesMerged[i] = this.tables.get(tableNames[i]).selectAll();
			}else{
				tablesMerged[i] = null;
				System.out.println("Table $ does not exist in Database".replace("$",tableNames[i]));
			}
		}
		return tablesMerged;
	}

	public void deleteFromTable(String tableName, String conditionA, String conditionB) throws IOException{
		Object[] keys = this.tables.keySet().toArray();
		if(Arrays.asList(keys).contains(tableName)){
			this.tables.get(tableName).delete(conditionA, conditionB);
		}else{
			System.out.println("Table $ does not exist in database".replace("$",tableName));
			throw new IOException();
		}
	}

	public String[] selectAllTable(String tableName) throws IOException{
		Object[] keys = this.tables.keySet().toArray();
		if(Arrays.asList(keys).contains(tableName)){
			return this.tables.get(tableName).selectAll();
		}else{
			System.out.println("Table $ does not exist in database".replace("$",tableName));
			throw new IOException();
		}

	}

	public String[] selectFromTable(String tableName, String[] headers) throws IOException{
		Object[] keys = this.tables.keySet().toArray();
		if(Arrays.asList(keys).contains(tableName)){
			return this.tables.get(tableName).select(headers);
		}else{
			System.out.println("Table $ does not exist in database".replace("$",tableName));
			throw new IOException();
		}
	}

	public String[] selectAllWhereEquals(String tableName, String conditionA, String conditionB) throws IOException{
		Object[] keys = this.tables.keySet().toArray();
		if(Arrays.asList(keys).contains(tableName)){
			return this.tables.get(tableName).selectAllWhereEqual(conditionA, conditionB);
		}else{
			System.out.println("Table $ does not exist in database".replace("$",tableName));
			throw new IOException();
		}
	}

	public String[] selectWhereEquals(String tableName, String[] headers, String conditionA, String conditionB) throws IOException{
		Object[] keys = this.tables.keySet().toArray();
		if(Arrays.asList(keys).contains(tableName)){
			return this.tables.get(tableName).selectWhereEqual(headers, conditionA, conditionB);
		}else{
			System.out.println("Table $ does not exist in database".replace("$",tableName));
			throw new IOException();
		}
	}

	public String[] selectAllWhereNotEquals(String tableName, String conditionA, String conditionB) throws IOException{
		Object[] keys = this.tables.keySet().toArray();
		if(Arrays.asList(keys).contains(tableName)){
			return this.tables.get(tableName).selectAllWhereNotEqual(conditionA, conditionB);
		}else{
			System.out.println("Table $ does not exist in database".replace("$",tableName));
			throw new IOException();
		}
	}

	public String[] selectWhereNotEqualse(String tableName, String[] headers, String conditionA, String conditionB) throws IOException{
		Object[] keys = this.tables.keySet().toArray();
		if(Arrays.asList(keys).contains(tableName)){
			return this.tables.get(tableName).selectWhereNotEqual(headers, conditionA, conditionB);
		}else{
			System.out.println("Table $ does not exist in database".replace("$",tableName));
			throw new IOException();
		}
	}

	private int getNumberOfTables(){
		return this.tables.keySet().toArray().length;
	}

	public Object[] showTablesNames(){
		return this.tables.keySet().toArray();
	}


	public String getDataBaseName(){
		return this.dataBaseName;
	}


	
}
