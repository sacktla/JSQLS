/*
AUTHOR JESUS ZARAGOZA
PARSER ALLOWS FOR AN INTERFACE JTextArea TO INTERACT WITH JSQL PACKAGE
*/
import java.util.HashMap;
import javax.swing.JTextArea;
public class Parser{
	private String command;
	private HashMap<String,Integer> mapper;
	private JSQLDataBase database;


	public Parser(){
		this.mapper = new HashMap<String,Integer>();
		this.mapper.put("LOAD",0);
		this.mapper.put("SELECT",1);
		this.mapper.put("ALL",2);
		this.mapper.put("FROM",3);
		this.mapper.put("JOIN",4);
		this.mapper.put("ADD",5);
		this.mapper.put("TABLE",6);
		this.mapper.put("DATABASE",7);
		this.mapper.put("INSERT",8);
		this.mapper.put("DELETE",9);
		this.mapper.put("HEADERS",10);
		this.mapper.put("WHERE",11);
		this.mapper.put("NOT",12);
		this.mapper.put("SHOW",13);
		this.mapper.put("TABLES",14);
		this.mapper.put("INTO", 15);
		this.mapper.put("CREATE",16);
		this.mapper.put("EQUALS", 17);
		this.mapper.put("!", 18);//for selection of specific headers.
	}

	public void performAction(String command, JTextArea area) throws JSQLException{
		this.command = command;
		this.cleanCommand();
		String[] tokens = this.command.split(" ");
		this.selectCommand(tokens, area);
	}

	private void cleanCommand(){
		this.command.trim();
		this.command = this.command.replace("\n","").replace("\t","");
	}

	private void selectCommand(String[] tokens, JTextArea area ) throws JSQLException{
		JSQLTable table, newTable;
		String[] content, headers, values;
		String conditionA, conditional, conditionB,tableName;
		String tableA,tableB,by,header,name,from,where,equals;
		
		try{
			Integer x= mapper.get(tokens[0]);
			if(x == null){
				x = 100;
			}
			switch(x){
				case 0://LOAD
					table = new JSQLTable(tokens[1],tokens[2]);//tokens 2 is name of table
					this.database.addTable(table);
					area.append("Table $ has been loaded\n".replace("$",tokens[1]));
					break;
				case 1://SELECT
					switch(mapper.get(tokens[1])){
						case 2://ALL
							switch(mapper.get(tokens[2])){
								case 3://from
									content = this.database.selectAllTable(tokens[3]);
									for(String line:content){
										area.append(line + "\n");
									}
									break;
								case 11://where
									conditionA = tokens[3];
									conditional = tokens[4];
									conditionB = tokens[5];
									tableName = tokens[6];
									
									if(conditional.equals("EQUALS")){
										content = this.database.selectAllWhereEquals(tableName,conditionA,conditionB);
										for(String line:content){
											area.append(line + "\n");
										}
									}else if(conditional.equals("NOT")){
										content = this.database.selectAllWhereNotEquals(tableName,conditionA,conditionB);
										for(String line:content){
											area.append(line + "\n");
										}
									}else{
										throw new JSQLException("Invalid arguments");
									}
									break;
								default:
									throw new JSQLException("Invalid arguments");
							}
							break;
						case 18://!
							headers = tokens[2].split(",");
							switch(mapper.get(tokens[3])){
								case 3://from
									content =this.database.selectFromTable(tokens[4],headers);
									for(String line: content){
										area.append(line + "\n");
									}
									break;
								case 11://where
									conditionA = tokens[4];
									conditional = tokens[5];
									conditionB = tokens[6];
									tableName = tokens[7];
									
									if(conditional.equals("EQUALS")){
										content = this.database.selectWhereEquals(tableName,headers,conditionA,conditionB);
										for(String line: content){
											area.append(line + "\n");
										}
									}else if(conditional.equals("NOT")){
										content = this.database.selectWhereNotEquals(tableName,headers,conditionA,conditionB);
										for(String line: content){
											area.append(line + "\n");	
										}
										
									}else{
										throw new JSQLException("Invalid arguments");
									}
									break;
								default:
									throw new JSQLException("Invalid arguments");
							}
							break;
						default:
							throw new JSQLException("Invalid arguments");

					}
					break;
				case 4://JOIN
					tableA = tokens[1];
					tableB = tokens[2];
					by = tokens[3];
					header = tokens[4];
					name = tokens[5];
					tableName = tokens[6];
					if(!by.equals("BY") || !name.equals("NAME")){
						throw new JSQLException("Invalid arguments");
					}
					newTable = this.database.joinTwoTables(tableName,tableA,tableB,header);
					this.database.addTable(newTable);
					area.append("Table & has been created.\n".replace("&",tableName));
					break;
				case 5://ADD
					switch(mapper.get(tokens[1])){
						case 6://TABLE
							tableName = tokens[2];
							newTable = new JSQLTable(tableName);
							this.database.addTable(newTable);
							area.append("Table & has been added.\n".replace("&",tableName));
							break;
						case 10://HEADERS
							tableName= tokens[2];
							headers = tokens[3].split(",");
							this.database.addHeaders(tableName, headers);
							area.append("Headers have been added to table &\n".replace("&", tableName));
							break;
						default:
							throw new JSQLException("Invalid arguments");
					}

					break;
				case 8://INSERT
					values = tokens[1].split(",");
					tableName = tokens[2];
					this.database.insertIntoTable(tableName,values);
					area.append("Rows have been added to table &\n".replace("&", tableName));
					break;
				case 9://DELETE
					from = tokens[1];
					tableName = tokens[2];
					where = tokens[3];
					conditionA = tokens[4];
					equals = tokens[5];
					conditionB = tokens[6];
					if(!from.equals("FROM")|!where.equals("WHERE")|!equals.equals("EQUALS")){
						throw new JSQLException("Invalid arguments");
					}
					this.database.deleteFromTable(tableName,conditionA,conditionB);
					area.append("Rows have been deleted for table &\n".replace("&",tableName));
					break;
				case 13://SHOW
					switch(mapper.get(tokens[1])){
						case 14:
							String[][] tables = this.database.selectAllTables();
							for(int i = 0; i < tables.length; i++){
								for(int j = 0; j < tables[i].length; j++){
									area.append(tables[i][j] + "\n");
								}
							}
							break;
						case 7://Database
							Object[] tableNames = this.database.showTablesNames();
							area.append("Tables in current Database:\n");
							for(Object object:tableNames){
								area.append(String.valueOf(object) + "\n");
							}
							break;
						default:
							throw new JSQLException("Invalid arguments");
					}
					break;
				case 16://CREATE
					switch(mapper.get(tokens[1])){
						case 7://DATABASE
							this.database = new JSQLDataBase(tokens[2]);
							area.append("Database & has been created\n".replace("&", tokens[2]));
							break;
						case 6:
							table = new JSQLTable(tokens[2]);
							this.database.addTable(table);
							area.append("Table & has been created\n".replace("&",tokens[2]));
							break;
						default:
							throw new JSQLException("Invalid arguments");
					}
					break;
				case 100:
					throw new JSQLException("Incorrect command");
					
			}
		}catch(ArrayIndexOutOfBoundsException f){
			throw new JSQLException("Incorrect number of arguments");
		}
	}
}