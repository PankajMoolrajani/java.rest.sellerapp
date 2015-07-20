package rest.sellerapp.bean.user;

public class UserSearchParamBean 
{
	private String searchString,screenSize;

	public UserSearchParamBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserSearchParamBean(String searchString, String screenSize) {
		super();
		this.searchString = searchString;
		this.screenSize = screenSize;
	}

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	public String getScreenSize() {
		return screenSize;
	}

	public void setScreenSize(String screenSize) {
		this.screenSize = screenSize;
	}
	
}
