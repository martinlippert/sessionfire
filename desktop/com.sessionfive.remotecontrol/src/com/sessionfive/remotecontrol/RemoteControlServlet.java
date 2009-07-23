package com.sessionfive.remotecontrol;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sessionfive.app.SessionFiveApplication;

public class RemoteControlServlet extends HttpServlet {

	private static final long serialVersionUID = -4739172642665336515L;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		super.init(config);
		
		System.out.println("servlet initialized");
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		
		if ("/forward".equals(pathInfo)) {
			SessionFiveApplication.animationControllerInstance.forward();
		}
		else if ("/backward".equals(pathInfo)) {
			SessionFiveApplication.animationControllerInstance.backward();
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		super.doGet(request, response);
	}

}
