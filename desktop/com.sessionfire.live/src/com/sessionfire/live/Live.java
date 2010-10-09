package com.sessionfire.live;

import java.io.IOException;
import java.net.URI;

import net.tootallnate.websocket.WebSocketClient;
import net.tootallnate.websocket.WebSocketClient.Draft;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.sessionfive.app.SessionFiveApplication;
import com.sessionfive.core.Presentation;

public class Live {

	private SocketClient client;

	public Live() {
	}

	public void setEnabled(boolean selected) {
		if (!selected && isConnected()) {
			disconnect();
		} else if (selected && !isConnected()) {
			connect();
		}
	}

	public void disconnect() {
		try {
			client.close();
			client = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void connect() {
		try {
			URI uri = new URI("ws://localhost:3000//socket.io/websocket");
			client = new SocketClient(uri, Draft.DRAFT76);
			client.connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isConnected() {
		return client != null;
	}

	public void syncShapeFocus(int shapeNo, Presentation presentation) {
		if (isConnected()) {
			try {
				client.send("{\"action\":\"focusChanged\", \"shapeNo\":"
						+ shapeNo + ", \"presentation\":\""
						+ presentation.getId() + "\"}");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	protected static class SocketClient extends WebSocketClient {

		private static final String FRAME = "~m~";
		private static final String HEARTBEAT = "~h~";

		public SocketClient(URI serverUri, Draft draft) {
			super(serverUri, draft);
		}

		@Override
		public void onMessage(String message) {
			System.out.println("message arrived: " + message);

			if (message != null && message.length() > 0) {
				if (message.startsWith(FRAME)) {
					int secondFrame = message.indexOf(FRAME, FRAME.length());
					if (message.length() > secondFrame + FRAME.length()) {
						String realMessage = message.substring(secondFrame
								+ FRAME.length());
						System.out.println("decoded message: " + realMessage);

						if (realMessage.startsWith(HEARTBEAT)) {
							try {
								this.send(realMessage);
							} catch (IOException e) {
								e.printStackTrace();
							}
						} else {
							try {
								JSONObject parsedMessage = (JSONObject) new JSONParser()
										.parse(realMessage);
								if ("focusChanged".equals(parsedMessage
										.get("action"))) {
									int shapeNo = ((Long) parsedMessage
											.get("shapeNo")).intValue();
									String presentationID = (String) parsedMessage
											.get("presentation");

									String id = SessionFiveApplication
											.getInstance().getPresentation()
											.getId();
									if (id != null && id.equals(presentationID)) {
										SessionFiveApplication.getInstance()
												.getAnimationController()
												.goToKeyframeNo(shapeNo);
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}

		@Override
		public void onOpen() {
			System.out.println("socket opened");
		}

		@Override
		public void onClose() {
			System.out.println("socket closed");
		}

		@Override
		public void send(String message) throws IOException {
			String encodedMessage = FRAME + message.length() + FRAME + message;
			super.send(encodedMessage);
		}

	}

}
