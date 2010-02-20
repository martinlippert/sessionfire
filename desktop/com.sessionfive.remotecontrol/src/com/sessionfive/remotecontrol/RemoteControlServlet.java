package com.sessionfive.remotecontrol;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sessionfive.app.SessionFiveApplication;

public class RemoteControlServlet extends HttpServlet {

	private static final long serialVersionUID = -4739172642665336515L;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		// determine debug info
		String port = System.getProperty("org.eclipse.equinox.http.jetty.http.port");
		String host = "";
		try {
			InetAddress addr = InetAddress.getLocalHost();
			host = addr.getHostAddress();
		} catch (UnknownHostException e) {
		}

		SessionFiveApplication.setStatus(host + ":" + port);
	}

	@Override
	/*
	 * http://localhost:8088/sessionfive/remotecontrol/go?to=4
	 * http://localhost:8088/sessionfive/remotecontrol/numberofkeyframes
	 * http://localhost:8088/sessionfive/remotecontrol/keyframe?at=0 (liefert
	 * ein 512x512 PNG mit Alpha-Channel)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pathInfo = request.getPathInfo();

		if ("/forward".equals(pathInfo)) {
			SessionFiveApplication.getInstance().getAnimationController().forward();
		} else if ("/backward".equals(pathInfo)) {
			SessionFiveApplication.getInstance().getAnimationController().backward();
		} else if ("/numberofkeyframes".equals(pathInfo)) {
			returnNumberOfKeyFrames(request, response);
		} else if ("/keyframe".equals(pathInfo)) {
			returnKeyFrameAt(request, response);
		} else if ("/go".equals(pathInfo)) {
			goTo(request, response);
		}
	}

	private void returnKeyFrameAt(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String keyFrameNo = request.getParameter("at");
		if (keyFrameNo != null) {
			int parsedNumber = Integer.parseInt(keyFrameNo);
			if (parsedNumber < SessionFiveApplication.getInstance().getAnimationController()
					.getNumberOfKeyFrames()) {
				byte[] image = SessionFiveApplication.getInstance().getKeyFrame(parsedNumber);

				if (image != null) {
					response.setContentType("image/jpg");
					response.setStatus(HttpServletResponse.SC_OK);

					ServletOutputStream out = response.getOutputStream();
					out.write(image);
					out.close();
				}
			}
		}
	}

	private void goTo(HttpServletRequest request, HttpServletResponse response) {
		String toParameter = request.getParameter("to");
		if (toParameter != null) {
			int parsedNumber = Integer.parseInt(toParameter);
			if (parsedNumber < SessionFiveApplication.getInstance().getAnimationController()
					.getNumberOfKeyFrames()) {
				SessionFiveApplication.getInstance().getAnimationController().goToKeyframeNo(parsedNumber);
			}
		}
	}

	private void returnNumberOfKeyFrames(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		int numberOfKeyFrames = SessionFiveApplication.getInstance().getAnimationController()
				.getNumberOfKeyFrames();

		response.setContentType("text/plain");
		response.setStatus(HttpServletResponse.SC_OK);
		PrintWriter writer = new PrintWriter(response.getOutputStream());
		writer.print(numberOfKeyFrames);
		writer.flush();
		writer.close();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		super.doGet(request, response);
	}

}
