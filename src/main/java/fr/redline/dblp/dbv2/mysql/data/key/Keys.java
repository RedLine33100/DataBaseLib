package fr.redline.dblp.dbv2.mysql.data.key;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class Keys {

	private final HashMap<String, KeyData> hashMap = new HashMap<>();

	public Keys(String name, String colon, Symbol symbol, Object object) {
		addKey(new KeyData(name, colon, symbol, object));
	}
	
	public void addKey(KeyData keyData) {
		
		if(keyData != null) {
		
			if(canAddObject(keyData.getKeyName()))

				hashMap.put(keyData.getKeyName(), keyData);
		
		}
		
	}
	
	public KeyData getKey(String colon) {
		return this.hashMap.get(colon);
	}
	
	public boolean canAddObject(String colon) {
		return !hashMap.containsKey(colon);
	}
	
	public List<String> getColumnList() {
		return new ArrayList<>(hashMap.keySet());
	}

	public List<Object> getObjectList() {
		return new ArrayList<>(hashMap.values());
	}

	public String toWhereString(){

		StringBuilder whereString = new StringBuilder().append(" WHERE ");

		for(Entry<String, KeyData> hashMapData : hashMap.entrySet()) {
			if (hashMapData.getValue().getSeparator() != null)
				whereString.append(hashMapData.getValue().getSeparator());
			whereString.append(hashMapData.getValue().getColon()).append(hashMapData.getValue().getSymbol()).append("?");
		}

		return whereString.toString();

	}
	
}
