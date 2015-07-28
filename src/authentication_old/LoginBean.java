package authentication_old;

public class LoginBean 
{
	private String login_username;
	private String login_password;
	
	public LoginBean() {
		super();
		// TODO Auto-generated constructor stub
	}	
	public LoginBean(String loginUsername, String loginPassword) {
		super();
		login_username = loginUsername;
		login_password = loginPassword;
	}
	public String getLogin_username() {
		return login_username;
	}
	public void setLogin_username(String loginUsername) {
		login_username = loginUsername;
	}
	public String getLogin_password() {
		return login_password;
	}
	public void setLogin_password(String loginPassword) {
		login_password = loginPassword;
	}	
}
