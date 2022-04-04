package stocker.controller.inputoutput;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Pull client for http requests
 * @author Christoph Kaplan
 *
 */
public class PullClient{
	/**
	 * Queries data via http by given request url
	 * @param request the request url
	 * @return the answer from the server
	 * @throws Exception when an error occurres
	 */
	public String doPullRequest(String request) throws Exception {
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(request).openConnection();
			conn.setRequestMethod("GET");
			conn.connect();
			int code = conn.getResponseCode();
			if (code >= 400) {
				throw new Exception("doPullRequest() error:" + "HTTP-Statuscode: " +code);
			}
			try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
				String myData = in.readLine();
				return myData;
			}
		} catch (IOException ex) {
			throw new Exception("doPullRequest() error:" + ex.getMessage());
		}
	}
	
}

