package com.sessionfire.live;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sessionfive.core.Presentation;

public class LiveServer {

	public void upload(Presentation presentation) {
		System.out.println("Upload presentation");

		try {
			URL url = new URL("http://localhost:3000/upload");
			HttpURLConnection urlConn = (HttpURLConnection) url
					.openConnection();
			urlConn.setRequestMethod("PUT");
			urlConn.setAllowUserInteraction(false); // no user interaction
			urlConn.setDoOutput(true); // want to send
			urlConn.setRequestProperty("Content-type",
					"application/json; charset=UTF-8");
			urlConn.setRequestProperty("accept", "application/json");
			OutputStream outputStream = urlConn.getOutputStream();

			JSONConverter json = new JSONConverter();

			PrintWriter printout = new PrintWriter(outputStream);
			json.writePresentation(presentation, printout);
			printout.flush();

			int rspCode = urlConn.getResponseCode();
			System.out.println("Return Code: " + rspCode);

			if (rspCode == 200) {
				uploadSuccessful(urlConn.getInputStream(), presentation);
			} else {
				uploadFailed(urlConn.getErrorStream());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void uploadFailed(InputStream errorStream) {
	}

	private void uploadSuccessful(InputStream returnStream,
			Presentation presentation) throws IOException, ParseException {
		JSONObject result = (JSONObject) new JSONParser()
				.parse(new InputStreamReader(returnStream));
		
		String id = (String) result.get("id");
		presentation.setId(id);
		
		System.out.println("presentation got id: " + id);
		System.out.println("presentation got url: " + result.get("url"));
	}

}
