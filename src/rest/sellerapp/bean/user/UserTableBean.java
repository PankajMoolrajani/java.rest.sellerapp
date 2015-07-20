package rest.sellerapp.bean.user;

public class UserTableBean 
{
	private int id;
	private String userName,firstName,emailId,phoneNo;
	public UserTableBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public UserTableBean(int id, String userName,
			String emailId, String phoneNo) {
		super();
		this.id = id;
		this.userName = userName;		
		this.emailId = emailId;
		this.phoneNo = phoneNo;
	}	
	public UserTableBean(int id, String userName, String phoneNo) {
		super();
		this.id = id;
		this.userName = userName;
		this.phoneNo = phoneNo;
	}
	
	public UserTableBean(String userName, String phoneNo) {
		super();
		this.userName = userName;
		this.phoneNo = phoneNo;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}	
}
