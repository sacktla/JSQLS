public class JSQLObject{
	private String type;
	private Object value;

	public JSQLObject(String value, String type){
		
		if(validType(type)){
			this.type = type;
		}else{
			this.value = null;
			this.type = null;
			return;
		}
		if(type.equals("NUMERIC") && value.indexOf(".") == -1){
			try{
				Integer number = Integer.parseInt(value);
				this.value = number;
			}catch(NumberFormatException n){
				this.value = null;
				this.type = null;
			}
		}else if(type.equals("DECIMAL") && value.indexOf(".") != -1){
			try{
				Double number = Double.parseDouble(value);
				this.value = number;
			}catch(NumberFormatException n){
				this.value = null;
				this.type = null;
			}
		}else if(type.equals("CHAR")){
			this.value = value;
		}else{
			this.value = null;
			this.type = null;
		}
	}

	public Object getValue(){
		return this.value;
	}

	public String getType(){
		return this.type;
	}

	private boolean validType(String type){
		String[] validTypes = {"NUMERIC","DECIMAL","CHAR"};
		for(String types:validTypes){
			if(type.equals(types)){
				return true;
			}
		}
		return false;
	}
}