package com.sessionfire.live;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.internal.preferences.Base64;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.sessionfive.core.LayerType;
import com.sessionfive.core.Presentation;
import com.sessionfive.core.Shape;
import com.sessionfive.core.ui.PresentationLoader;
import com.sessionfive.shapes.ImageShape;

public class JSONOutput {

	public void store(Presentation presentation) {
		JSONObject jsonMainObject = new JSONObject();
		PresentationLoader loader = new PresentationLoader();
		Properties settingsMap = loader.createSettingsMap(presentation);
		
		jsonMainObject.putAll(settingsMap);
		
		List<Shape> shapes = presentation.getShapes(LayerType.CAMERA_ANIMATED);
		addShapes(jsonMainObject, shapes);
		
		try {
			FileWriter fileWriter = new FileWriter("/Users/mlippert/Desktop/test.json");			
			jsonMainObject.writeJSONString(fileWriter);
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void addShapes(JSONObject jsonMainObject, List<Shape> shapes) {
		if (shapes != null && shapes.size() > 0) {
			JSONArray shapeArray = new JSONArray();
			for (Shape shape : shapes) {
				JSONObject jsonShape = new JSONObject();
				jsonShape.put("type", shape.getClass().getName());
				if (shape instanceof ImageShape) {
					byte[] imageBytes = ((ImageShape) shape).getImageBytes();
					byte[] encodedImageBytes = Base64.encode(imageBytes);
					jsonShape.put("image", new String(encodedImageBytes));
					jsonShape.put("imagetype", ((ImageShape) shape).getImageType());
				}
				
				addShapes(jsonShape, shape.getShapes());
				shapeArray.add(jsonShape);
			}
			jsonMainObject.put("shapes", shapeArray);
		}
	}

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
			
			JSONObject jsonMainObject = new JSONObject();
			PresentationLoader loader = new PresentationLoader();
			Properties settingsMap = loader.createSettingsMap(presentation);
			
			jsonMainObject.putAll(settingsMap);
			
			List<Shape> shapes = presentation.getShapes(LayerType.CAMERA_ANIMATED);
			addShapes(jsonMainObject, shapes);
			
			PrintWriter printout = new PrintWriter(outputStream);
			jsonMainObject.writeJSONString(printout);
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
