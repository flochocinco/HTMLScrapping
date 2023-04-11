package org.example;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

import java.net.HttpURLConnection;
import java.net.URL;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class HtmlUnitScraper {

	public static void main(String[] args) throws Exception {

		String cookieSession = process(); //30min
		if(cookieSession == null){
			System.err.println("Cannot get cookie");
			return;
		}

		if(getVintedPhotos(cookieSession) == null){
			return;
		}

	}

	// find all images without alternate text
	// and give them a red border
	static String process(){
		try(WebClient webClient = new WebClient()) {
			webClient.getOptions().setUseInsecureSSL(true);
			webClient.getOptions().setCssEnabled(false);
			webClient.getOptions().setJavaScriptEnabled(false);


			HtmlPage htmlPage = webClient.getPage("https://www.vinted.fr/");
			WebResponse response = htmlPage.getWebResponse();
			//get "Cookie" from response
			String cookie = response.getResponseHeaders().stream()
					.filter(it -> "Set-Cookie".equals(it.getName()) && it.getValue().contains("_vinted_fr"))
					.findAny()
					.orElse(new NameValuePair("", null))
					.getValue();
			System.out.println(cookie);
			return cookie;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static String getVintedPhotos(String cookie) throws IOException {

		//String url = "https://www.vinted.fr/member/41318663-mytopdressing";
		String url = "https://www.vinted.fr/api/v2/users/41318663/items?page=1&per_page=20&order=relevance";
		HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		connection.setRequestMethod("GET");
		//may need to first open a connection to vinted.com to get the cookie
		connection.setRequestProperty("Cookie", cookie);

		int responseCode = connection.getResponseCode();
		if(responseCode == 200){
			StringBuilder response = new StringBuilder();
			Scanner scanner = new Scanner(connection.getInputStream());
			while(scanner.hasNextLine()){
				String line = scanner.nextLine();
				String[] fullSizeUrls = line.split("\"image_no\":1,");
				if(fullSizeUrls.length > 1){
					for(String split : new ArrayList<>(Arrays.asList(fullSizeUrls).subList(1, fullSizeUrls.length))){
						response.append(split.split("url\":\"")[1].split("\"")[0]);
						response.append("\n");
					}
				}
			}
			scanner.close();

			System.out.println(response);
			return response.toString();
		}else{
			System.err.println("Error code: " + responseCode);
		}

		// an error happened
		return null;
	}
}
