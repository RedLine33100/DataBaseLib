package fr.redline.dblp.dbv2.mysql.data;

import fr.redline.dblp.dbv2.mysql.cmd.Commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LinkedData {

	private final HashMap<String, Object> hashMap = new HashMap<>();
	
	public LinkedData addObject(String colon, Object object) {
		if(canAddObject(colon))

			hashMap.put(colon, object);

		return this;
	}
	
	public Object getObject(String colon) {
		return this.hashMap.get(colon);
	}

	public String getString(String colon){
		return String.valueOf(this.getObject(colon));
	}

	public Integer getInt(String colon){
		return (Integer) this.getObject(colon);
	}

	public Double getDouble(String colon){
		return (Double) this.getObject(colon);
	}

	public Float getFloat(String colon){
		return (Float) this.getObject(colon);
	}
	
	public boolean deleteObject(String colon) {
		if(!this.canAddObject(colon)){
			this.hashMap.remove(colon);
			return true;
		} else return false;
	}
	
	public boolean canAddObject(String colon) {
		return !this.hashMap.containsKey(colon);
	}
	
	public List<String> getColList() {
		return new ArrayList<>(hashMap.keySet());
	}
	
	public List<Object> getObjectList() {
		return new ArrayList<>(hashMap.values());
	}

	public String toCmdString(Commands commands){

		StringBuilder stringBuild = null;
		List<String> colonDataCollection = getColList();

		if(!colonDataCollection.isEmpty())

			switch(commands.name){

				case "UPDATE":{
					for(Object colon : colonDataCollection){

						if(stringBuild == null)
							stringBuild = new StringBuilder();
						else
							stringBuild.append(",\n");

						stringBuild.append(colon).append(" = ").append("?");

					}
					break;
				}

				case "INSERT":{
					for(Object colon : colonDataCollection){

						if(stringBuild == null)
							stringBuild = new StringBuilder();
						else
							stringBuild.append(",\n");

						stringBuild.append(colon);

					}
					break;
				}
			}

		if(stringBuild == null) stringBuild = new StringBuilder();
		return stringBuild.toString();

	}

}
