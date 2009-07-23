package com.sessionfive.animation;

import com.sessionfive.core.Shape;

public class ShapeFocus {
	
	public static CameraSetting getFocussedShape(Shape shape) {
		float shapeX = shape.getX();
		float shapeY = shape.getY();
		
		float shapeWidth = shape.getWidth();
		float shapeHeight = shape.getHeight();
		
		float rotationAngle = -shape.getRotation();
		double rotationRadian = Math.toRadians(rotationAngle);
		
		double upX = Math.sin(rotationRadian);
		double upY = Math.cos(rotationRadian);
		double upZ = 0f;

		float cameraX = shapeX + (shapeWidth / 2);
		float cameraY = shapeY + (shapeHeight / 2);
		float cameraZ = shapeWidth > shapeHeight ? shapeWidth : shapeHeight;
		
		CameraSetting cameraSetting = new CameraSetting(cameraX, cameraY, cameraZ,
				cameraX, cameraY, 0, (float)upX, (float)upY, (float)upZ);
		
		return cameraSetting;
	}

}
