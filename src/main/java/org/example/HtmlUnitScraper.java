package org.example;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

import java.net.HttpURLConnection;
import java.net.URL;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

public class HtmlUnitScraper {

	public static String cookie = null;

	public static void main(String[] args) throws Exception {

		String cookieSession = getCookieSession(); //30min
		if(cookieSession == null){
			System.err.println("Cannot get cookie");
			return;
		}

		List<String> vintedPhotos = getVintedPhotos(cookieSession);
		if(vintedPhotos.isEmpty()){
			return;
		}

		vintedPhotos.forEach(System.out::println);
	}

	// find all images without alternate text
	// and give them a red border
	static String getCookieSession(){
		if(isCookieValid()){
			return cookie;
		}
		try(WebClient webClient = new WebClient()) {
			webClient.getOptions().setUseInsecureSSL(true);
			webClient.getOptions().setCssEnabled(false);
			webClient.getOptions().setJavaScriptEnabled(false);


			HtmlPage htmlPage = webClient.getPage("https://www.vinted.fr/");
			WebResponse response = htmlPage.getWebResponse();
			//get "Cookie" from response
			String cookie2 = response.getResponseHeaders().stream()
					.filter(it -> "Set-Cookie".equals(it.getName()) && it.getValue().contains("_vinted_fr"))
					.findAny()
					.orElse(new NameValuePair("", null))
					.getValue();
			System.out.println(cookie2);
			cookie = cookie2;
			return cookie;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 *
	 * @param dateStr like "Thu, 20 Apr 2023 20:03:25 GMT
	 * @return boolean
	 */
	public static boolean isExpired(String dateStr){
		SimpleDateFormat format = new SimpleDateFormat("EEEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
		try {
			Date date = format.parse(dateStr);
			return Instant.now().isAfter(date.toInstant());
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	public static String getExpireDate(String cookie){
		return cookie.split("expires=")[1].split(";")[0];
	}

	public static boolean isCookieValid(){
		return cookie != null && !isExpired(getExpireDate(cookie));
	}

	public static List<String> getVintedPhotos(String cookie) throws IOException {

		List<String> urls = new ArrayList<>();

		//String url = "https://www.vinted.fr/member/41318663-mytopdressing";
		String url = "https://www.vinted.fr/api/v2/users/41318663/items?page=1&per_page=20&order=relevance";
		HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		connection.setRequestMethod("GET");
		//may need to first open a connection to vinted.com to get the cookie
		connection.setRequestProperty("Cookie", cookie);

		int responseCode = connection.getResponseCode();
		if(responseCode == 200){
			Scanner scanner = new Scanner(connection.getInputStream());
			while(scanner.hasNextLine()){
				String line = scanner.nextLine();
				String[] fullSizeUrls = line.split("\"image_no\":1,");
				if(fullSizeUrls.length > 1){
					for(String split : new ArrayList<>(Arrays.asList(fullSizeUrls).subList(1, fullSizeUrls.length))){
						urls.add(split.split("url\":\"")[1].split("\"")[0]);
					}
				}
			}
			scanner.close();

		}else{
			System.err.println("Error code: " + responseCode);
		}

		// an error happened
		return urls;
	}
}
