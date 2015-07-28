

package authentication;



public  class VerifyAccessToken  {

	

    public String verifyAccessToken(String accessToken) {
        
		if(verfyToken(accessToken))
		{
			// it will be a map
			return "True";
		}
        else
		{
			// it will be a map
			return "False";
        }
    }	
	public boolean verifyToken(String accessToken)
	{
		return false;
	}

 }