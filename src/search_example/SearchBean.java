package search_example;

import java.util.List;

public class SearchBean 
{
	private String table_name,columns;
	private ConditionClass condition;

	public SearchBean(String tableName, String columns,
			ConditionClass condition) {
		super();
		table_name = tableName;
		this.columns = columns;
		this.condition = condition;
	}
	public SearchBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getTable_name() {
		return table_name;
	}
	public void setTable_name(String tableName) {
		table_name = tableName;
	}
	public String getColumns() {
		return columns;
	}
	public void setColumns(String columns) {
		this.columns = columns;
	}
	public ConditionClass getCondition() {
		return condition;
	}
	public void setCondition(ConditionClass condition) {
		this.condition = condition;
	}	
}
