package search_example;

import java.util.List;

public class ConditionClass 
{
	private List<String> and;
	private List<String> or;
	public ConditionClass() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ConditionClass(List<String> and, List<String> or) {
		super();
		this.and = and;
		this.or = or;
	}
	public List<String> getAnd() {
		return and;
	}
	public void setAnd(List<String> and) {
		this.and = and;
	}
	public List<String> getOr() {
		return or;
	}
	public void setOr(List<String> or) {
		this.or = or;
	}
		
}
