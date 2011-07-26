package fi.loo.VLCRemote;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.widget.Toast;


public class Control {

	public static InputStream OpenHttpConnection(String urlString) throws IOException {
	    InputStream in = null;
	    int response = -1;
	           
	    URL url = new URL (urlString); 
	    URLConnection conn = url.openConnection();
	             
	    if (!(conn instanceof HttpURLConnection))                     
	        throw new IOException("Not an HTTP connection");
	    
	    try{
	        HttpURLConnection httpConn = (HttpURLConnection) conn;
	        httpConn.setAllowUserInteraction(false);
	        httpConn.setInstanceFollowRedirects(true);
	        httpConn.setRequestMethod("GET");
	        httpConn.connect(); 
	
	        response = httpConn.getResponseCode();                 
	        if (response == HttpURLConnection.HTTP_OK) {
	            in = httpConn.getInputStream();                                 
	        }                     
	    }
	    catch (Exception ex)
	    {
	        throw new IOException("Error connecting");            
	    }
	    return in;     
	}
	
	
	
	
	
	
	
	public static boolean sendCommand(String command) throws IOException, URISyntaxException {

	   boolean returnA = false;
	   
	   HttpClient client = new DefaultHttpClient();
       HttpGet request = new HttpGet();
       request.setURI(new URI("http://www.cs.helsinki.fi/u/tomikosk/"));
       HttpResponse response = client.execute(request);
		
       if (response != null)
    	   returnA = true;
		
       return returnA;
	    	
		
	}
	
	
	
	
	
	public static void getValue(String command) {
		
	}

}
