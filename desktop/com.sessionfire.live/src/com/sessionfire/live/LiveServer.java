package com.sessionfire.live;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import com.sessionfive.core.Presentation;

public class LiveServer {

	public void upload(Presentation presentation) {
		System.out.println("Upload presentation");
		
		try {
			URL url = new URL("http://localhost:3000/upload");
			HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
			urlConn.setRequestMethod("PUT");
			urlConn.setAllowUserInteraction(false); // no user interaction
			urlConn.setDoOutput(true); // want to send
			urlConn.setRequestProperty("Content-type", "application/json; charset=UTF-8");
			urlConn.setRequestProperty("accept", "application/json");
			OutputStream outputStream = urlConn.getOutputStream();
			
			JSONConverter json = new JSONConverter();
			
			PrintWriter printout = new PrintWriter(outputStream);
			json.writePresentation(presentation, printout);
			printout.flush();

			int rspCode = urlConn.getResponseCode();
			System.out.println("Return Code: " + rspCode);
			
			InputStream ist = null;
			if (rspCode == 200) {
				ist = urlConn.getInputStream();
			}
			else {
				ist = urlConn.getErrorStream();
			}
			InputStreamReader isr = new InputStreamReader(ist);
			BufferedReader br = new BufferedReader(isr);

			String nextLine = br.readLine();
			while (nextLine != null) {
				System.out.println(nextLine);
				nextLine = br.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
