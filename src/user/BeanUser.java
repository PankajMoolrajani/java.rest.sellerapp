package user;

public class BeanUser {
	private String user_name,first_name,last_name,user_category,phone_number,email_id,add_line_one,add_line_two,city,state,zip;
	private int user_id, user_cat_id ;	

	public BeanUser() {
		super();
		// TODO Auto-generated constructor stub
	}
	

	public BeanUser(int user_id, String user_name, String email_id, String phone_number) {
		super();
		this.user_name = user_name;
		this.phone_number = phone_number;
		this.email_id = email_id;
		this.user_id = user_id;
	}


	public BeanUser(String first_name, String last_name,
			String user_category, String phone_number, String email_id,
			String add_line_one, String add_line_two, String city, String state,
			String zip) {
		super();
		this.first_name = first_name;
		this.last_name = last_name;
		this.user_category = user_category;
		this.phone_number = phone_number;
		this.email_id = email_id;
		this.add_line_one = add_line_one;
		this.add_line_two = add_line_two;
		this.city = city;
		this.state = state;
		this.zip = zip;
	}
		
	public BeanUser(String first_name, String last_name, String user_category,
			String phone_number, String email_id, String add_line_one,
			String add_line_two, String city, String state, String zip,
			int user_id, int user_cat_id) {
		super();
		this.first_name = first_name;
		this.last_name = last_name;
		this.user_category = user_category;
		this.phone_number = phone_number;
		this.email_id = email_id;
		this.add_line_one = add_line_one;
		this.add_line_two = add_line_two;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.user_id = user_id;
		this.user_cat_id = user_cat_id;
	}

	public int getUserId() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public int getUserCatId() {
		return user_cat_id;
	}

	public void setUserCatId(int user_cat_id) {
		this.user_cat_id = user_cat_id;
	}
	
	public String getFirstName() {
		return first_name;
	}	

	public String getUserName() {
		return user_name;
	}

	public void setUserName(String user_name) {
		this.user_name = user_name;
	}

	public void setFirstName(String first_name) {
		this.first_name = first_name;
	}

	public String getLastName() {
		return last_name;
	}

	public void setLastName(String last_name) {
		this.last_name = last_name;
	}

	public String getUserCategory() {
		return user_category;
	}

	public void setUserCategory(String user_category) {
		this.user_category = user_category;
	}

	public String getPhoneNumber() {
		return phone_number;
	}

	public void setPhoneNumber(String phone_number) {
		this.phone_number = phone_number;
	}

	public String getEmailId() {
		return email_id;
	}

	public void setEmailId(String email_id) {
		this.email_id = email_id;
	}

	public String getAddLineOne() {
		return add_line_one;
	}

	public void setAddLineOne(String add_line_one) {
		this.add_line_one = add_line_one;
	}

	public String getAddLineTwo() {
		return add_line_two;
	}

	public void setAddLineTwo(String add_line_two) {
		this.add_line_two = add_line_two;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}
}
