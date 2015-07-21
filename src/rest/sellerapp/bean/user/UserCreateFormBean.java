package rest.sellerapp.bean.user;

public class UserCreateFormBean
{
	private String firstName,lastName,userCategory,phoneNumber,emailId,addLineOne,addLineTwo,city,state,zip;

	public UserCreateFormBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserCreateFormBean(String firstName, String lastName,
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

	public String getFirstName() {
		return firstName;
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
