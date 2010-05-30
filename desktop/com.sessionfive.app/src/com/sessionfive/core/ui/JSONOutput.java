package com.sessionfive.core.ui;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.internal.preferences.Base64;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.sessionfive.core.LayerType;
import com.sessionfive.core.Presentation;
import com.sessionfive.core.Shape;
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
				}
				
				addShapes(jsonShape, shape.getShapes());
				shapeArray.add(jsonShape);
			}
			jsonMainObject.put("shapes", shapeArray);
		}
	}

}
