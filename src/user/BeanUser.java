package user;

public  class BeanUser  {
	int id, id_pass, id_address, id_user_category, zip;
	String password, question, ans, name_user, name_first, name_last, emailid, phone;
	String address_line_one, address_line_two, city, state, country, landmark;
	
	public BeanUser() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public BeanUser(int id, int id_pass, int id_address, int id_user_category,
			int zip, String password, String question, String ans,
			String name_user, String name_first, String name_last,
			String emailid, String phone, String address_line_one,
			String address_line_two, String city, String state, String country,
			String landmark) {
		super();
		this.id = id;
		this.id_pass = id_pass;
		this.id_address = id_address;
		this.id_user_category = id_user_category;
		this.zip = zip;
		this.password = password;
		this.question = question;
		this.ans = ans;
		this.name_user = name_user;
		this.name_first = name_first;
		this.name_last = name_last;
		this.emailid = emailid;
		this.phone = phone;
		this.address_line_one = address_line_one;
		this.address_line_two = address_line_two;
		this.city = city;
		this.state = state;
		this.country = country;
		this.landmark = landmark;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdPass() {
		return id_pass;
	}

	public void setIdPass(int id_pass) {
		this.id_pass = id_pass;
	}

	public int getIdAddress() {
		return id_address;
	}

	public void setIdAddress(int id_address) {
		this.id_address = id_address;
	}

	public int getIdUserCategory() {
		return id_user_category;
	}

	public void setIdUserCategory(int id_user_category) {
		this.id_user_category = id_user_category;
	}

	public int getZip() {
		return zip;
	}

	public void setZip(int zip) {
		this.zip = zip;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAns() {
		return ans;
	}

	public void setAns(String ans) {
		this.ans = ans;
	}

	public String getNameUser() {
		return name_user;
	}

	public void setNameUser(String name_user) {
		this.name_user = name_user;
	}

	public String getNameFirst() {
		return name_first;
	}

	public void setNameFirst(String name_first) {
		this.name_first = name_first;
	}

	public String getNameLast() {
		return name_last;
	}

	public void setNameLast(String name_last) {
		this.name_last = name_last;
	}

	public String getEmailid() {
		return emailid;
	}

	public void setEmailid(String emailid) {
		this.emailid = emailid;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddressLineOne() {
		return address_line_one;
	}

	public void setAddressLineOne(String address_line_one) {
		this.address_line_one = address_line_one;
	}

	public String getAddressLineTwo() {
		return address_line_two;
	}

	public void setAddressLineTwo(String address_line_two) {
		this.address_line_two = address_line_two;
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

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getLandmark() {
		return landmark;
	}

	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}
	
	
 }