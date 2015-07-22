package rest.sellerapp.bean.user_category;

public class UserCategoryBean 
{
	private int id;
	private String name;
	public UserCategoryBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public UserCategoryBean(int id, String name) {
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
