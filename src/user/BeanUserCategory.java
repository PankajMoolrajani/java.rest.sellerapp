package user;

public class BeanUserCategory 
{
	private int id;
	private String name;
	public BeanUserCategory() {
		super();
		// TODO Auto-generated constructor stub
	}
	public BeanUserCategory(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
		
}
