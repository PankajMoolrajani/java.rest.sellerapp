package inventory;

public class BeanInventoryCategory {
	String name, name_table;
	int id, id_parent_category, id_tax;
	
	public BeanInventoryCategory() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BeanInventoryCategory(String name, String name_table, int id,
			int id_parent_category) {
		super();
		this.name = name;
		this.name_table = name_table;
		this.id = id;
		this.id_parent_category = id_parent_category;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameTable() {
		return name_table;
	}

	public void setNameTable(String name_table) {
		this.name_table = name_table;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdParentCategory() {
		return id_parent_category;
	}

	public void setIdParentCategory(int id_parent_category) {
		this.id_parent_category = id_parent_category;
	}
	
	public int getIdTax(){
		return id_tax;
	}
	
	public void setIdTax(int id_tax){
		this.id_tax = id_tax;
	}
	

}
