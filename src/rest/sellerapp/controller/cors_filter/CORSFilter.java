package rest.sellerapp.controller.cors_filter;

import javax.ws.rs.ext.Provider;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;

/**
 * Filter to handle cross-origin resource sharing.
 */
@Provider
public class CORSFilter implements ContainerResponseFilter
{
  public CORSFilter()
  {
  }
  
  public ContainerResponse filter(ContainerRequest request, ContainerResponse response)
  {	  	 	  
	  response.getHttpHeaders().add( "Access-Control-Allow-Origin", "*" );
	  response.getHttpHeaders().add( "Access-Control-Allow-Credentials", "true" );
	  response.getHttpHeaders().add( "Access-Control-Allow-Methods", "GET, POST, DELETE, PUT" );
	  response.getHttpHeaders().add( "Access-Control-Allow-Headers", "X-Requested-With, Content-Type, X-Codingpedia" );      
    return response;
  }
}