package org.example;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.File;
import java.nio.file.Files;

public class HtmlUnitScraper {
	
	public static void main(String[] args) throws Exception {

		String url = "https://www.vinted.fr/member/83349569-viralonso";
	    //String url = "https://developer.android.com/reference/android/speech/SpeechRecognizer";
	    try(WebClient webClient = new WebClient()){
			webClient.getOptions().setUseInsecureSSL(true);
			webClient.getOptions().setCssEnabled(false);
			webClient.getOptions().setJavaScriptEnabled(false);


			HtmlPage htmlPage = webClient.getPage(url);
			WebResponse response = htmlPage.getWebResponse();
			String content = response.getContentAsString();
			Files.write(new File("s:\\tmp\\htmlpage.html").toPath(), content.getBytes());
			System.out.println(htmlPage.getTitleText());

			DomNodeList<DomNode> domNode = htmlPage.querySelectorAll(".feed-grid__item");
			System.out.println("Found "+ domNode.size() + " elements");
			domNode.forEach(it -> System.out.println(it.toString()));
		}
		//System.out.println(domNode.asText());
		
		/*List<HtmlAnchor> anchors =  htmlPage.getAnchors();
		for (HtmlAnchor anchor : anchors) {
			System.out.println(anchor.getAttribute("href"));
		}*/
	}
}