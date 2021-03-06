/*
AUTHOR JESUS ZARAGOZA
This class will represent the Table object in SQL.
This class will have a static Dictionary that all the tables in the same
DB(a different object) will be able to share.
I need to think about the storing and accessing from memory instead of using RAM.
Read a little bit more about binary objects and transversing through objects.
*/
import java.util.HashMap;
import java.util.Arrays;
import java.io.*;
import java.util.Scanner;
public class JSQLTable{
	
	//Instance variables
	private String tableName;
	private HashMap<String,HashMap<String,String>> table;
	private String[] headersName;
	private int lineNumber;
		
	/*Constructor: Creates a table using a csv file*/
	public JSQLTable(String csvFileName, String tableName) throws JSQLException{
		//Constructor variables
		Scanner fileReader;
		String lineReader;
		String[] lineSplit;

		//initialize variables
		lineReader = "";
		lineNumber = 0;

		//instantiate table name
		this.tableName = tableName;
		this.table = new HashMap<String,HashMap<String,String>>();

		try{
			FileInputStream file = new FileInputStream(csvFileName);
			fileReader = new Scanner(file);			 
		}catch(IOException e){
			throw new JSQLException("Problem reading file %".replace("%",csvFileName));
		}

		
		if(fileReader.hasNext()){
			lineNumber ++;
			lineReader = fileReader.nextLine();
			lineReader = lineReader.trim();
			this.headersName = lineReader.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
				
			while(fileReader.hasNext()){
				lineNumber++;
				HashMap<String,String> row = new HashMap<String,String>();
				String rowName = "Row_" + lineNumber;					
				lineReader = fileReader.nextLine();
				lineReader = lineReader.trim();
				lineSplit = lineReader.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
				if(lineSplit.length != this.headersName.length){
					System.out.println("Line & doesn't contain same number of arguments as headers".replace("&",String.valueOf(lineNumber)));
					continue;
				}
				for(int i = 0; i < lineSplit.length; i++){
					row.put(this.headersName[i],lineSplit[i]);
				}
				this.table.put(rowName,row);				
			}	
		}else{
			throw new JSQLException("Empty file");
		}	
		fileReader.close();
	
	}
	
	/*Constructor: Initializes a table for fill up later using methods below*/
	public JSQLTable(String tableName){
		this.tableName = tableName;
		this.table = new HashMap<String,HashMap<String,String>>();
		this.lineNumber = 0;
		
	}
	
	//We are going to need editing and selection capacities here as well so that the class JSQLDataBase can call them
	//This method will select data from the table.
	public String[] selectAll(){
		//Get all keys of rows
		Object[] keys = this.table.keySet().toArray();
		Arrays.sort(keys);
		String[] fields;
		
		fields = new String[keys.length + 1];
		for(int i = 0; i < fields.length; i++){
			fields[i] = "";
		}
        for(int i = 0; i < this.headersName.length; i++){
          	if(i<headersName.length-1){
				fields[0] += this.headersName[i] + ",";
           	}else{
				fields[0] += this.headersName[i];
			}
		}

		int counter = 1;
		for(Object key : keys){
			for(int i = 0; i < headersName.length; i++){
				if(i<headersName.length-1){
					fields[counter] += this.table.get(key).get(headersName[i]) + ",";
				}else{
					fields[counter] += this.table.get(key).get(headersName[i]);
				}
			}
			counter++;
		}
		return fields;		
	}

	//Same as above except for selected headers
	public String[] select(String[] headers) throws JSQLException{
		String[] fields;
		Object[] keys = this.table.keySet().toArray();
		Arrays.sort(keys);

		for(String header: headers){
			if(!Arrays.asList(this.headersName).contains(header)){
                            throw new JSQLException("Header $ passed not found in table!".replace("$",header));
                        }
                }

                fields = new String[keys.length+1];
		
		for(int i = 0; i<fields.length; i++){
			fields[i] = "";
		}

		for(int i=0; i<headers.length; i++){
			if(i < headers.length - 1){
				fields[0] += headers[i] + ",";
			}else{
				fields[0] += headers[i];
			}
		}
        int counter = 1;
        for(Object key: keys){
            for(int i = 0; i < headersName.length; i++){
                if(Arrays.asList(headers).contains(headersName[i])){
                   	fields[counter] += this.table.get(key).get(headersName[i]) + ",";
                }
            }
			counter++;
        }

		//Remove that last comma
		for(int i = 1; i < fields.length; i++){
			fields[i] = fields[i].substring(0,fields[i].length()-1);
		}
        return fields;

	}
	//Method selects all data where conditionA = conditionB
	public String[] selectAllWhereEqual(String conditionA, String conditionB){
		String[] fields;
		String[] cleanFields;
		Object[] keys = this.table.keySet().toArray();
		Arrays.sort(keys);

		fields = new String[keys.length + 1];

		for(int i = 0; i<fields.length; i++){
			fields[i] = "";
		}
		
		for(int i=0; i < this.headersName.length; i++){
			if(i < this.headersName.length - 1){
                fields[0] += headersName[i] + ",";
            }else{
            	fields[0] += headersName[i];
            }   
		}

		int counter = 1;
		for(Object key : keys){
			if(this.table.get(key).get(conditionA).equals(conditionB)){
				for(int i = 0; i < headersName.length; i++){
					if(i<headersName.length-1){
						fields[counter] += this.table.get(key).get(headersName[i]) + ",";
					}else{
						fields[counter] += this.table.get(key).get(headersName[i]);
					}
				}
				counter++;
			}
		}
		cleanFields = new String[counter];
		for(int i = 0; i < fields.length; i++){
			if(!fields[i].equals("")){
				cleanFields[i] = fields[i];
			}
		}
		return cleanFields;
		
	} 	
	//Method selects data specified by headers where conditionA = conditionB
	public String[] selectWhereEqual(String[] headers, String conditionA, String conditionB) throws JSQLException{
		String[] fields;
		String[] cleanFields;
		Object[] keys = this.table.keySet().toArray();
		Arrays.sort(keys);

		fields = new String[keys.length + 1];

		for(String header: headers){
			if(!Arrays.asList(this.headersName).contains(header)){
                            throw new JSQLException("Header $ passed not found in table!".replace("$",header));
                        }
                }

                fields = new String[keys.length+1];
		
		for(int i = 0; i<fields.length; i++){
			fields[i] = "";
		}

		for(int i=0; i<headers.length; i++){
			if(i < headers.length - 1){
				fields[0] += headers[i] + ",";
			}else{
				fields[0] += headers[i];
			}
		}

		int counter = 1;
		for(Object key: keys){
			if(this.table.get(key).get(conditionA).equals(conditionB)){
	            for(int i = 0; i < headersName.length; i++){
	                if(Arrays.asList(headers).contains(headersName[i])){
	                   	fields[counter] += this.table.get(key).get(headersName[i]) + ",";
	                }
	            }
	            counter++;
	        }
			
        }

		//Remove that last comma
		for(int i = 1; i < counter; i++){
			fields[i] = fields[i].substring(0,fields[i].length()-1);
		}

		cleanFields = new String[counter];
		for(int i = 0; i < fields.length; i++){
			if(!fields[i].equals("")){
				cleanFields[i] = fields[i];
			}
		}
		return cleanFields;
	}
	//Method selects all data where conditionA != conditionB
	public String[] selectAllWhereNotEqual(String conditionA, String conditionB){
		String[] fields;
		String[] cleanFields;
		Object[] keys = this.table.keySet().toArray();
		Arrays.sort(keys);

		fields = new String[keys.length + 1];

		for(int i = 0; i<fields.length; i++){
			fields[i] = "";
		}
		
		for(int i=0; i < this.headersName.length; i++){
			if(i < this.headersName.length - 1){
                fields[0] += headersName[i] + ",";
            }else{
            	fields[0] += headersName[i];
            }   
		}

		int counter = 1;
		for(Object key : keys){
			if(!this.table.get(key).get(conditionA).equals(conditionB)){
				for(int i = 0; i < headersName.length; i++){
					if(i<headersName.length-1){
						fields[counter] += this.table.get(key).get(headersName[i]) + ",";
					}else{
						fields[counter] += this.table.get(key).get(headersName[i]);
					}
				}
				counter++;
			}
		}
		cleanFields = new String[counter];
		for(int i = 0; i < fields.length; i++){
			if(!fields[i].equals("")){
				cleanFields[i] = fields[i];
			}
		}
		return cleanFields;
	}
	//Method selects data specified by headers where conditionA != conditionB
	public String[] selectWhereNotEqual(String[] headers, String conditionA, String conditionB) throws JSQLException{
		String[] fields;
		String[] cleanFields;
		Object[] keys = this.table.keySet().toArray();
		Arrays.sort(keys);

		fields = new String[keys.length + 1];

		for(String header: headers){
			if(!Arrays.asList(this.headersName).contains(header)){
                            throw new JSQLException("Header $ passed not found in table!".replace("$",header));
                        }
                }

                fields = new String[keys.length+1];
		
		for(int i = 0; i<fields.length; i++){
			fields[i] = "";
		}

		for(int i=0; i<headers.length; i++){
			if(i < headers.length - 1){
				fields[0] += headers[i] + ",";
			}else{
				fields[0] += headers[i];
			}
		}

		int counter = 1;
		for(Object key: keys){
			if(!this.table.get(key).get(conditionA).equals(conditionB)){
	            for(int i = 0; i < headersName.length; i++){
	                if(Arrays.asList(headers).contains(headersName[i])){
	                   	fields[counter] += this.table.get(key).get(headersName[i]) + ",";
	                }
	            }
	            counter++;
	        }
			
        }

		//Remove that last comma
		for(int i = 1; i < counter; i++){
			fields[i] = fields[i].substring(0,fields[i].length()-1);
		}

		cleanFields = new String[counter];
		for(int i = 0; i < fields.length; i++){
			if(!fields[i].equals("")){
				cleanFields[i] = fields[i];
			}
		}
		return cleanFields;

	}
	//INSERTS ROW OF DATA TO TABLE 
	public void insert(String[] values) throws JSQLException{	
		if(this.headersName == null){
			throw new JSQLException("Headers have not been initialized");
		}
		if(values.length != this.headersName.length){
			throw new JSQLException("Data trying to be added does not contain the same amount of parameters");
		}else{
			HashMap<String,String> row = new HashMap<String,String>();
			this.lineNumber++;
			String rowName = "Row_" + lineNumber;
			for(int i = 0; i < values.length; i++){
				row.put(this.headersName[i],values[i]);
			}
			this.table.put(rowName,row);
		}
	}
	//SETS HEADERS OF TABLE
	public void setHeaders(String [] headers){
		this.headersName = new String[headers.length];
		for(int i = 0; i < headers.length; i++){
			this.headersName[i] = headers[i];
		}
	}
	//DELETES ROW OF DATA OF TABLE WHERE CONDITIONA(HEADER) == CONDITIONB(VALUE)
	public void delete(String conditionA, String conditionB){
		Object[] keys = this.table.keySet().toArray();
		Arrays.sort(keys);

		for(Object key : keys){
			if(this.table.get(key).get(conditionA).equals(conditionB)){
				this.table.remove(key);
			}
		}	
	}
	//RETURNS TABLE NAME
	public String getTableName(){
		return this.tableName;
	}
	//returns headers of table
	public String[] getHeaders(){
		return this.headersName;
	}
	
	//RETURNS THE TABLE REFERENCE
	public HashMap<String,HashMap<String,String>> getTableReference(){
		return this.table;
	}


}
