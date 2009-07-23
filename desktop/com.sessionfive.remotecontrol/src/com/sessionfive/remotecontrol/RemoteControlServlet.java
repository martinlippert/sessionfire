package com.sessionfive.remotecontrol;

import java.io.IOException;
import java.io.InputStream;
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

		System.out.println("servlet initialized: " + host + ":" + port);

	}

	@Override
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
		} else if ("/nextImage".equals(pathInfo)) {
			returnImages(request, response, pathInfo);
		} else if ("/prevImage".equals(pathInfo)) {
			returnImages(request, response, pathInfo);
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
				SessionFiveApplication.getInstance().getAnimationController().goTo(parsedNumber);
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

	private void returnImages(HttpServletRequest request, HttpServletResponse response,
			String pathInfo) throws IOException {
		InputStream in;
		if ("/nextImage".equals(pathInfo)) {
			in = SessionFiveApplication.getInstance().getAnimationController().nextImage();
		} else {// if ("/prevImage".equals(pathInfo)) {
			in = SessionFiveApplication.getInstance().getAnimationController().prevImage();
		}

		if (in != null) {
			response.setContentType("image/jpg");
			response.setStatus(HttpServletResponse.SC_OK);

			ServletOutputStream out = response.getOutputStream();
			byte[] buf = new byte[1024];
			int count = 0;
			while ((count = in.read(buf)) >= 0) {
				out.write(buf, 0, count);
			}
			in.close();
			out.close();
		}
	}

}
