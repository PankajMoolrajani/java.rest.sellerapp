package orders;

public class CreateOrderBean {
	private String user_id, user_name, product_id;

	public CreateOrderBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CreateOrderBean(String user_id, String user_name, String product_id) {
		super();
		this.user_id = user_id;
		this.user_name = user_name;
		this.product_id = product_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}
	

}
