package search;

import java.util.HashMap;
import java.util.Map;

public class BeanSearch {

	String table_name, columns, conditions;
	Map <String, Object>map_condition;
	
	public BeanSearch() {
		super();
		// TODO Auto-generated constructor stub
	}
	public BeanSearch(String table_name, String columns, Map<String, Object> map_condition) {
		super();
		this.table_name = table_name;
		this.columns = columns;
		this.map_condition = map_condition;
	}
	public String getTableName() {
		return table_name;
	}
	public void setTableName(String table_name) {
		this.table_name = table_name;
	}
	public String getColumns() {
		return columns;
	}
	public void setColumns(String columns) {
		System.out.println("set columns");
		this.columns = columns;
	}
	public String getConditions() {
		return conditions;
	}
	public void setConditions(String conditions) {
		System.out.println("set condition");
		this.conditions = conditions;
	}
	public Map<String, Object> getMapCondition() {
		return map_condition;
	}
	public void setMapCondition(Map<String, Object> map_condition) {
		System.out.println("set map condition");
		this.map_condition = map_condition;
	}	
}
