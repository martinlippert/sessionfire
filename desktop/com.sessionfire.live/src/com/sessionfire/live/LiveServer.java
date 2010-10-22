package com.sessionfire.live;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;

import javax.swing.SwingUtilities;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sessionfive.core.Presentation;

public class LiveServer {

	public void upload(final Presentation presentation, final UploadResult uploadCallback) {
		
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
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
					PrintWriter printout = new PrintWriter(outputStream);

					JSONConverter json = new JSONConverter();
					json.writePresentation(presentation, printout);
					printout.flush();

					int rspCode = urlConn.getResponseCode();

					if (rspCode == 200) {
						uploadSuccessful(urlConn.getInputStream(), presentation, uploadCallback);
					} else {
						uploadFailed(urlConn.getErrorStream(), uploadCallback);
					}
				} catch (Exception e) {
					e.printStackTrace();
					uploadFailed(null, uploadCallback);
				}
			}
		};
		
		Executors.newSingleThreadExecutor().execute(runnable);
	}

	private void uploadFailed(final InputStream errorStream, final UploadResult uploadCallback) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				uploadCallback.uploadFailed();
			}
		});
	}

	private void uploadSuccessful(final InputStream returnStream,
			final Presentation presentation, final UploadResult uploadCallback) throws IOException, ParseException {
		final JSONObject result = (JSONObject) new JSONParser()
				.parse(new InputStreamReader(returnStream));
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				String id = (String) result.get("id");
				presentation.setId(id);
				
				uploadCallback.uploadSuccessful((String)result.get("url"));
			}
		});
	}

}
