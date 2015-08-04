package user_old;

public class BeanUser {
	private String userName,firstName,lastName,userCategory,phoneNumber,emailId,addLineOne,addLineTwo,city,state,zip;
	private int userId, userCatId ;	

	public BeanUser() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public BeanUser(int userId, String userName,
			String emailId, String phoneNumber) {
		super();
		this.userId = userId;
		this.userName = userName;		
		this.emailId = emailId;
		this.phoneNumber = phoneNumber;
	}	
	
	public BeanUser(String firstName, String lastName,
			String userCategory, String phoneNumber, String emailId,
			String addLineOne, String addLineTwo, String city, String state,
			String zip) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.userCategory = userCategory;
		this.phoneNumber = phoneNumber;
		this.emailId = emailId;
		this.addLineOne = addLineOne;
		this.addLineTwo = addLineTwo;
		this.city = city;
		this.state = state;
		this.zip = zip;
	}
		
	public BeanUser(String firstName, String lastName, String userCategory,
			String phoneNumber, String emailId, String addLineOne,
			String addLineTwo, String city, String state, String zip,
			int userId, int userCatId) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.userCategory = userCategory;
		this.phoneNumber = phoneNumber;
		this.emailId = emailId;
		this.addLineOne = addLineOne;
		this.addLineTwo = addLineTwo;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.userId = userId;
		this.userCatId = userCatId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUser_id(int userId) {
		this.userId = userId;
	}

	public int getUserCatId() {
		return userCatId;
	}

	public void setUserCatId(int userCatId) {
		this.userCatId = userCatId;
	}
	
	public String getFirstName() {
		return firstName;
	}	

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserCategory() {
		return userCategory;
	}

	public void setUserCategory(String userCategory) {
		this.userCategory = userCategory;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getAddLineOne() {
		return addLineOne;
	}

	public void setAddLineOne(String addLineOne) {
		this.addLineOne = addLineOne;
	}

	public String getAddLineTwo() {
		return addLineTwo;
	}

	public void setAddLineTwo(String addLineTwo) {
		this.addLineTwo = addLineTwo;
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
