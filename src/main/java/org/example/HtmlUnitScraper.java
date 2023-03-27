package org.example;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class HtmlUnitScraper {
	
	public static void main(String[] args) throws Exception {
		
	    String url = "https://developer.android.com/reference/android/speech/SpeechRecognizer";
	    try(WebClient webClient = new WebClient()){
			webClient.getOptions().setUseInsecureSSL(true);
			webClient.getOptions().setCssEnabled(false);
			webClient.getOptions().setJavaScriptEnabled(false);


			HtmlPage htmlPage = webClient.getPage(url);
			System.out.println(htmlPage.getTitleText());

			DomNodeList<DomNode> domNode = htmlPage.querySelectorAll(".api-signature");
			domNode.forEach(it -> System.out.println(it.asText()));
		}
		//System.out.println(domNode.asText());
		
		/*List<HtmlAnchor> anchors =  htmlPage.getAnchors();
		for (HtmlAnchor anchor : anchors) {
			System.out.println(anchor.getAttribute("href"));
		}*/
	}
}