/*
AUTHOR: JESUS ZARAGOZA

This class will represent an entire data base formed by different JSQLTables.

*/
import java.util.HashMap;
import java.util.Arrays;
import java.io.IOException;
public class JSQLDataBase{
	//INSTANCE VARIABLES
	private HashMap<String,JSQLTable> tables;
	private String dataBaseName;
	//CONSTRUCTOR
	//CREATES A DATABASE WITH THE NAME PASSED AS A STRING
	public JSQLDataBase(String dataBaseName){
		this.tables = new HashMap<String,JSQLTable>();
		this.dataBaseName = dataBaseName;
	}
	//ADDS A JSQLTable
	public void addTable(JSQLTable table){
		String tableName = table.getTableName();
		this.tables.put(tableName,table);
	}
	//CREATES A JSQLTable and adds it to the database
	public void addTable(String tableName){
		JSQLTable tableToAdd = new JSQLTable(tableName);
		this.tables.put(tableName,tableToAdd);
	}
	//INSERT VALUES INTO TABLE SPECIFIED IN ARGUMENTS
	//TABLE MUST HAVE BEEN CREATED AND HAVE HEADERS
	public void insertIntoTable(String tableName, String[] values)throws JSQLException{
		Object[] keys = this.tables.keySet().toArray();
		if(Arrays.asList(keys).contains(tableName)){
			this.tables.get(tableName).insert(values);
		}else{
			throw new JSQLException("Table $ does not exist in database".replace("$",tableName));
		}
	}
	//ADDS HEADERS TO A TABLE 
	//TABLE HAS BEEN CREATED WITH THE DEFAULT JSQLTable CONSTRUCTOR
	public void addHeaders(String tableName, String[] headers) throws JSQLException{
		Object[] keys = this.tables.keySet().toArray();
		if(Arrays.asList(keys).contains(tableName)){
			this.tables.get(tableName).setHeaders(headers);
		}else{
			throw new JSQLException("Table $ does not exist in database".replace("$",tableName));
		}
	}
	//SELECTS ALL THE TABLES AND RETURNS THEM AS A TWO-D ARRAY
	public String[][] selectAllTables(){
		Object[] keys = this.tables.keySet().toArray();
		Arrays.sort(keys);
		int numberOfTables = this.getNumberOfTables();
		String[][] tablesMerged = new String[numberOfTables][];
		for(int i = 0; i < keys.length; i++){
			tablesMerged[i] = this.tables.get(keys[i]).selectAll();
		}

		return tablesMerged;
	}
	//JOINS TWO TABLES BY A HEADER. THE TWO TABLES MUST BE OF THE SAME LENGTH
	public JSQLTable joinTwoTables(String tableName, String tableAName, String tableBName, String header) throws JSQLException{
		JSQLTable returnTable = new JSQLTable(tableName);
		Object[] keys = this.tables.keySet().toArray();
		String[] tablesNames = {tableAName,tableBName};
		for(int i = 0; i < 2; i++){
			if(!Arrays.asList(keys).contains(tablesNames[i])){
				throw new JSQLException("Table $ does not exist in database".replace("$",tablesNames[i]));
			}
		}

		JSQLTable tableA, tableB;
		tableA = this.getTableReference(tableAName);
		tableB = this.getTableReference(tableBName);


		Object[] rowsTableA = tableA.getTableReference().keySet().toArray();
		Arrays.sort(rowsTableA);
		Object[] rowsTableB = tableB.getTableReference().keySet().toArray();
		Arrays.sort(rowsTableB);

		//Set headers of new table
		String[] tableAHeaders = tableA.getHeaders();
		String[] tableBHeaders = tableB.getHeaders();
		String[] headers = new String[tableAHeaders.length + tableBHeaders.length -1];
		
		
		for(int i = 0; i < headers.length; i++){
			if(i<tableAHeaders.length){
				headers[i] = tableAHeaders[i];	
			}else{
				if(!tableBHeaders[i-tableAHeaders.length].equals(header)){
					headers[i] = tableBHeaders[i-tableAHeaders.length];
				}else{
					for(int j = i; i < headers.length; j++){
						headers[i] = tableBHeaders[j+1-tableAHeaders.length];
						i++;
					}
					break;
				}
			}
		}

		returnTable.setHeaders(headers);
		
		for(int i = 0; i < rowsTableA.length; i++){
			for(int j = 0;j<rowsTableB.length; j++){
				if(tableA.getTableReference().get(rowsTableA[i]).get(header).equals(tableB.getTableReference().get(rowsTableB[j]).get(header))){
					String[] row = new String[headers.length];
					for(int a = 0; a<headers.length;a++){
						if(tableA.getTableReference().get(rowsTableA[i]).get(headers[a]) != null){
							row[a] = tableA.getTableReference().get(rowsTableA[i]).get(headers[a]);
						}else{
							row[a] = tableB.getTableReference().get(rowsTableB[i]).get(headers[a]);
						}
					}
					
					returnTable.insert(row);
				}
			}
		}
		return returnTable;

	}
	//SELECTS ALL THE TABLES INDICATED IN THE ARRAY OF STRING.
	public String[][] selectAllFromTables(String[] tableNames){
		Object[] keys = this.tables.keySet().toArray();
		Arrays.sort(keys);
		String[][] tablesMerged = new String[tableNames.length][];
		for(int i = 0; i < tableNames.length; i++){
			if(Arrays.asList(keys).contains(tableNames[i])){
				tablesMerged[i] = this.tables.get(tableNames[i]).selectAll();
			}else{
				tablesMerged[i] = null;
			}
		}
		return tablesMerged;
	}
	//DELETES ROWS THAT MEET CONDITIONA(HEADER) EQUALS TO CONDITIONB(VALUE)
	public void deleteFromTable(String tableName, String conditionA, String conditionB) throws JSQLException{
		Object[] keys = this.tables.keySet().toArray();
		if(Arrays.asList(keys).contains(tableName)){
			this.tables.get(tableName).delete(conditionA, conditionB);
		}else{
			throw new JSQLException("Table $ does not exist in database".replace("$",tableName));
		}
	}
	//RETURNS ARRAY OF TABLE VALUES AND HEADERS. SELECT TABLE BY PASSING NAME AS ARGUMENT
	public String[] selectAllTable(String tableName) throws JSQLException{
		Object[] keys = this.tables.keySet().toArray();
		if(Arrays.asList(keys).contains(tableName)){
			return this.tables.get(tableName).selectAll();
		}else{
			throw new JSQLException("Table $ does not exist in database".replace("$",tableName));
		}

	}
	//RETURN ARRAY OF TABLE VALUES AND HEADERS INDICATED IN ARGUMENTS
	public String[] selectFromTable(String tableName, String[] headers) throws JSQLException{
		Object[] keys = this.tables.keySet().toArray();
		if(Arrays.asList(keys).contains(tableName)){
			return this.tables.get(tableName).select(headers);
		}else{
			throw new JSQLException("Table $ does not exist in database".replace("$",tableName));
		}
	}
	//RETURN ARRAY OF TABLE VALUES AND HEADERS THAT MEET CONDITIONA(HEADER) = CONDITIONB(VALUE)
	public String[] selectAllWhereEquals(String tableName, String conditionA, String conditionB) throws JSQLException{
		Object[] keys = this.tables.keySet().toArray();
		if(Arrays.asList(keys).contains(tableName)){
			return this.tables.get(tableName).selectAllWhereEqual(conditionA, conditionB);
		}else{
			throw new JSQLException("Table $ does not exist in database".replace("$",tableName));
		}
	}
	//RETURN ARRAY OF TABLE VALUES AND HEADERS INDICATED IN ARGUMENTS THAT MEET CONDITIONA(HEADER) = CONDITIONB(VALUE)
	public String[] selectWhereEquals(String tableName, String[] headers, String conditionA, String conditionB) throws JSQLException{
		Object[] keys = this.tables.keySet().toArray();
		if(Arrays.asList(keys).contains(tableName)){
			return this.tables.get(tableName).selectWhereEqual(headers, conditionA, conditionB);
		}else{
			throw new JSQLException("Table $ does not exist in database".replace("$",tableName));
		}
	}
	//RETURN ARRAY OF TABLE VALUES AND HEADERS THAT MEET CONDITIONA(HEADER) != CONDITIONB(VALUE)
	public String[] selectAllWhereNotEquals(String tableName, String conditionA, String conditionB) throws JSQLException{
		Object[] keys = this.tables.keySet().toArray();
		if(Arrays.asList(keys).contains(tableName)){
			return this.tables.get(tableName).selectAllWhereNotEqual(conditionA, conditionB);
		}else{
			throw new JSQLException("Table $ does not exist in database".replace("$",tableName));
		}
	}
	//RETURN ARRAY OF TABLE VALUES AND HEADERS INDICATED IN ARGUMENTS THAT MEET CONDITIONA(HEADER) != CONDITIONB(VALUE)
	public String[] selectWhereNotEquals(String tableName, String[] headers, String conditionA, String conditionB) throws JSQLException{
		Object[] keys = this.tables.keySet().toArray();
		if(Arrays.asList(keys).contains(tableName)){
			return this.tables.get(tableName).selectWhereNotEqual(headers, conditionA, conditionB);
		}else{
			throw new JSQLException("Table $ does not exist in database".replace("$",tableName));
		}
	}
	//RETURNS NUMBER OF TABLES IN DATABASE
	private int getNumberOfTables(){
		return this.tables.keySet().toArray().length;
	}
	//RETURN TABLE NAMES INSIDE DATABASE
	public Object[] showTablesNames(){
		return this.tables.keySet().toArray();
	}

	//RETURNS NAME OF DATABASE
	public String getDataBaseName(){
		return this.dataBaseName;
	}
	//RETURNS THE REFERENCE OF TABLE SELECTED BY NAME IN ARGUMENT
	public JSQLTable getTableReference(String nameTable){
		return this.tables.get(nameTable);
	}


	
}
