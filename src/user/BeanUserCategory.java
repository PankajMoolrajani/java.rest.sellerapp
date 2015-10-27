
package user;

public  class BeanUserCategory  {

	int id;
	String name, description;
	
	public BeanUserCategory() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public BeanUserCategory(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public BeanUserCategory(int id, String name, String description) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
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
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
 }