package cards.threenotfour.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import cards.threenotfour.Controller;

/**
 * This guy just sits waiting for any data
 * 
 * @author sg3809
 * 
 */
public class MessageReceiver implements Runnable {

	@Override
	public void run() {

		try {
			// Open a server port to allow clients to send message
			ServerSocket serverSocket = new ServerSocket(Controller.SERVER_PORT);
			Socket clientSocket = null;
			BufferedReader bufferedReader = null;

			while (true) {
				// Accept connection from an incoming client
				clientSocket = serverSocket.accept();

				bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

				StringBuilder stringBuilder = new StringBuilder(100);
				String request = "";
				do {
					stringBuilder.append(request);
					request = bufferedReader.readLine();
				} while (!request.equals(""));

				// Send it to process the request
				processRequest(stringBuilder.toString(), clientSocket);

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void processRequest(String request, Socket clientSocket) {
		JSONParser parser = new JSONParser();

		try {
			JSONObject object = (JSONObject) parser.parse(request);

			String command = (String) object.get(Controller.REQUEST);

			if (command != null && command.equals(Controller.NEW_GAME)) {
				Controller.addPlayer(clientSocket.getInetAddress());
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}