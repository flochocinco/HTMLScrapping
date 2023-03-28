package org.example;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.net.HttpURLConnection;
import java.net.URL;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class HtmlUnitScraper {

	public static void main(String[] args) throws Exception {

		if(getVintedPhotos() == null){
			return;
		}

		String url = "https://www.vinted.fr/member/83349569-viralonso";
		//String url = "https://developer.android.com/reference/android/speech/SpeechRecognizer";
		try(WebClient webClient = new WebClient()){
			webClient.getOptions().setUseInsecureSSL(true);
			webClient.getOptions().setCssEnabled(false);
			webClient.getOptions().setJavaScriptEnabled(false);


			HtmlPage htmlPage = webClient.getPage(url);
			WebResponse response = htmlPage.getWebResponse();
			String content = response.getContentAsString();
			Files.write(new File("e:\\tmp\\htmlpage.html").toPath(), content.getBytes());
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

	public static String getVintedPhotos() throws IOException {

		HttpURLConnection connection = (HttpURLConnection) new URL("https://www.vinted.fr/api/v2/users/83349569/items?page=1&per_page=20&order=relevance").openConnection();
		connection.setRequestMethod("GET");
		//may need to first open a connection to vinted.com to get the cookie
		connection.setRequestProperty("Cookie", "anon_id=6731bfe3-b63f-464d-b74f-be587421abca; v_udt=c2V2MHBpdWFMSDNPelNTY1hZOXRlSzZwSVVKeThOaElDMlc0clE9PS0tSkFrbkFLN1kzYzRGQXd1Vi0taWtBaFIxRzRmcjNxNXlWVm1IblhrUT09; v_sid=d6ef153ebe9b03453b47ebeeb0886fb2; ab.optOut=This-cookie-will-expire-in-2024; OptanonAlertBoxClosed=2023-03-23T13:16:56.116Z; OTAdditionalConsentString=1~; domain_selected=true; eupubconsent-v2=CPpE1RgPpE1RgAcABBENC9CgAAAAAAAAAChQAAAAAAJBAIoABoAHAAeACQAKAAfwBFgCRAEuAL4AZQA2oBzAHOAOoAfIBBwCfgFDAKWAdUA9ACGwEPgI9ASEAkUBK0CbAJtAU2Ap8BV4CwgFxALlAXQAuoBdoC8gGBQMPAxABiwDIQGRgMmAaMA0oBqYDXQG0ANuAboA5YB0gDsAHZgO6AeBA8kDyoHuge9A-QD5QH2AP3AgIBAwCCIEEwIMAQrAhcBDQJBTAAQAAuACgAKgAZAA5AB4AIAAYAAygBoAGoAPIAhgCKAEwAJ8AVQBWACwAG8AOYAegA_ACEgEMARIAjgBLACaAFKALcAYYAyABlgDZAHfAPYA-IB9gH7AQABAwCKQEXARiAjQCOAEmAJSAUEAp4BVwC5gGKANEAawA2kBuAG8AOIAfIBDoCRAEygJ2AUOApEBTQCxQFoALYAXIAu8BeYDBgGEgMNAZEAyQBk4DLgGcgM-AaRA1gDWQG3gN1AcFA5EDlQHLgPHAe0BCECF4YAyAAYABwATwBFgCXAHMAUsA6gCQgEigJaATYApsBcQDAgGHgMjAa6A3QBxIDqAHZgO4ge6B7wD-A0CIAKwAXABDADIAGWANkAdgA_ACAAEFAIwASYAp4BV4C0ALSAawA3gB1QD5AIdARUAkQBOwCkQFyAMJAYwAycBnIDPAGfAOSAcoA_AQAbAAMAA4AEgATwBFgCXAHMAPkApYBvAEhAJFAS0AmwBcQDAgGHgNdAboA4kB1ADswHcQPdA94B9gD-AINCIDYAVgBDADIAGWANkAdgA_ACAAEYAJMAU8Aq4BrADqgHyAQ6AkQBOwCkQFyAMJAZOAzkBnwDkgHKAPwFQGAAKABDACYAFwARwAywB2AEcAKvAWgBaQDeAJBAWwAuQBeYDIgGcgM8AZ8A3IByQDlAH4AQvFAEwBtADmAHgAQUApYB1QEegJFATYAwIBh8DXQNeAbeA4kB7wD7AH8AQPAg2MgKgBDACYAI4AZYA7ACOAFXAK2AbwBJwC0QFsALzAZEAzkBngDPgHJAOUAfgBC8YAUAG0AOYAeABSwCxAHVAR6AkUBNgC8gGBAMPAa6A28BxID3gHxAPsAfwBBscBwAAMAAiABwAHgAXABIADkAH4AUAAvgBkADQAH8ARwAkQBLwCyALMAXwAywBtQDmAOcAdQA7AB3AD5AIAAQWAg4CEAERAJUATaAnwCfgFLAKgAVkAvUBgAGBAMyAawA14BvADjgHSAOqAeQA9AB8gEIAIbAQ-AiIBHoCQgEigJWATEAmWBNgE2gKFAUgApMBTACmwFTAKqAVeArYBXYCygFoALUAXEAuWBdAF1AMCgYeBiADFgGQgMmAZfA0UDRgGlANNAanA10DXgG0ANsAbcA4mBx4HIAOdAdIA6wB2ADswHagO4AeBA8kDygHpQPdA94B8QD5YH2AfaA_GB-wH8AP9AgeBBECDAEGwIVgQ0HQbQAFwAUABUADIAHIAPgBAAC6AGAAZQA0ADUAHgAPoAhgCKAEwAJ8AVQBWACxAFwAXQAxABmADeAHMAPQAfoBDAESAJYATAAmgBRgClAFiALeAYQBhwDIAMoAaIA2QBvgDvAHtAPsA_QB_gEDAIpARYBGACOAEmAJSAUEAp4BVwCxQFoAWkAuYBeQDFAG0ANwAcSA6YDqAIdARUAi8BIICRAEqAJkATsAocBTQCrAFigLYAXAAuQBdoC7wF5gL6AYMAwkBhoDGAGPAMkAZOAyoBlgDLgGcgM-AaJA0gDSQGlgNVAawA2MBt4DdQHFwOSA5UBy4DxwHqgPaAfWA_ACAIEEgINAQeAheQAdAAIAC-AGgAP4AkQBZAC-AGWANqAcwBzgDsAHgAQUAnwBQwClgFZALEAYAAzIBvADqgHbAPQAh8BHoCQgEiwJsAm0BQoCkAFJgLaAXKAugBeQDAgGHgMSAaKA0oBqYDXQG2ANuAcSA6MB2EDyQPKAejA90D3gHxAPsAfsBA8CDAEGwIVkIHgACwAKAAZABcADEAIYATAApgBVAC4AGIAMwAbwA9ACOAFiAMIAb4A7wB9gD_AIoARwAlIBQQCngFXgLQAtIBcwDFAG0AOoAkEBIgCTgEqAKaAWKAtEBbAC4AFyALtAZEAycBnIDPAGfANEAaSA0sBqoDgAHJAO1AeOA_ACCQEKAIXkgGAABgAHAAXAByAF8AMgAkQBZAC5AGWANoAcwA7gCAAEJAJ8AVAArIBmQDXgG8AOqAfYBHoCRQErAJagTYBNoCkwFUgLKAXKAw8BiwDSgGugNyAcSA6QB1gDsAHlAPeAfYA_cCCIEGAIaEoHAACAAFgAUAAyABwAD8AMAAxAB4AEQAJgAVQAuABiADNAIYAiQBHACjAFKALcAYQA2QB3gD8AI4AU8Aq8BaAFpAMUAbgA6gB8gEOgIqAReAkQBYoC2AF2gLzAZEAycBlgDOQGeAM-AaQA1gBt4DgAHagPaAfgBA8CCQELwIalAJQABgALgAkAByAD8AKwAXwAyACOAEiALKAXIBfADLAG1AOYA5wB1ADuAHgAPkAgABCQCKgEiAJtAT4BPwChgFLAKyAWIAuoBgADXgG8AOqAdsA8gB6AD_gI9ASKAmIBMsCbAJtAUgApgBTYCnwFTAK7AXKAvIBgQDDwGLAMmAaIA0qBqQGpwNdA14BxIDsAHcAPKge6B7wD4gH2QP2A_cCBgEDwIJgQYAg2BCsCGhSCeAAuACgAKgAZAA5AB8AIIAYABlADQANQAeQBDAEUAJgATwApABVACwAGIAMwAcwA_QCGAIkAUYApQBYgC3AGEAMoAaIA2QB3wD7AP0AiwBGACOAEpAKCAVcArYBcwC8gGKANoAbgBDoCLwEiAJOATsAocBYoC0AFsALgAXIAu0BeYC-gGGgMYAZEAyQBk4DLAGXAM5AZ4Az6BpAGkwNYA1kBsYDbwG6gOCgcmBygDlwHagPHAe0A_ACEIELw.YAAAAAAAAAAA; OptanonConsent=isGpcEnabled=0&datestamp=Tue+Mar+28+2023+09:36:40+GMT+0200+(Central+European+Summer+Time)&version=202302.1.0&isIABGlobal=false&consentId=6731bfe3-b63f-464d-b74f-be587421abca&hosts=&interactionCount=1&landingPath=NotLandingPage&groups=C0001:1,C0002:0,C0003:0,C0004:0,STACK42:0,C0015:0&geolocation=FR;&AwaitingReconsent=false; _vinted_fr_session=NjJkSVdlUnJVeHRrak1mUkE2QUtubWhzeDEzNGNoV2ZTaHd4amFvb0xwWjdGQmY2TnM0Z0dRZXdYVGNhU2tQdzZqTlgxeE91a3RBeUdRZmNUaGtKUXpxN0ZlRGdGUzdkUDQyT09Ham5NZFNHbkdESHhGUFg2bW5OMC9JTnlXSHV5ZTJOb2VQeVlLaFRTeXRERmFudFZ0WmNTZzE2ODNUUEpKUW1HRy85MDhnaldPaDMrU0RNTmhVWTBpSVAwOXovWk9tejAzbis2dFR5T2NFZDczdjhZVnYrd2t3cVR5bHVNSGN6MDZmNnJjYkVnUDVpZXZpblhqWjFBRUw3dHVPcjE4SjhZaXVCaVhtTVgwOTRDMlIyUkpTUWdWckRDc2JOcGU1WnFKZlB2bFZiWHVKSnRQSzhOekIrMEtlZEpTSldrZ1k2Zk1qMEt2TGxDUm1VRVV1MEdMdmVDZkcrRmdpNUM2R1k4NzJ2dXNVZ01SanJwTzdpenQ0RlBtVDFZMzhIbEljOEtSVzd1MG9JUFZINk9OblM5VUFvVEV4SVEvdUVYTUdVZXM5MXFKT2U0dDFxbkZIMk5YczZZaVMvckV5dFNMWDdiY0NXQmR1SFBKR0RWSXhoNlAwcHVuZURKeVY0UTJ2N1JNRXU1eE9xY0grVlNnNnhCdWppeDhLTlZxUExUY0lGb3M4bWpYSkRPR2hJTGtDL0J3S0NxUktBaE1XaVkweEhCd0wvbWtkNG9xS2pKRWUyQ1hwbElCMksyQzBZU1lxaUhDbnVQWklTRlFGZnp0N000OWJYdmhzTDFoTzRHZmMrRUZrRHFaUENzT1FIQXJhL3pPaGUrbXQyOXFFditrSWxMYmh4NlU2aVFBZHgvcDdPY3I2VjRnQkw3bGRxeVk3MXhxYkdWMlJOU1FycUFEZHdpZDBTVlJTQm1sQm1BZSttOWlnTGE1d3pwdUNKd3FEQ1RNODhtQkJxajgwUTFBNUd6TktGVlpFRUFyUHUvRkIxZndKd3lBZnY4cUMyaVRHNTBMUzJWNEtBOGNaZVM5V2ZSYXY4UStMSmtuVGM2QTA1MVZjMUZkRE9kaG10YVVPVnBZYU1mZnhlZlpITU5pdHdPeGpRRTRWZFI4Qmw0RzVpNWRoejdwdlM3VU5GU3JISUZOcDVmWXB5VDF1VTN6QU9MZlRzTE55SWkxRXo3SnRkWmxpYTFNSU5UWHhpcm50WmkwZS9rRElMbXA4aHloa3NNaTJ5YlRjaGhrT3dSdWdCYlBWT1V6R0dNSlVVSTl2TlRPOUZRRVAvSGdpcERhY1NTQ1c3b2p6cjFGR2d0WXZQUDF6TjllMWZsNEdKTHloQTh3S3hsL1lxbXdKdGZtRGVYMjRtMjVuYis0cUZ1ZEtRR0tWTDNqYjNvWXc0ZWJmc3BzWEE0OFBtV3h5cE1ncVZ4SHZNM3duQzdGVU00RVZCMjh2cUJMVDFWcnZMMk5Bd1ExekUzbTFKdGcxUnBPTmZ5TUR5VEYwPS0tazVaZGtqZFpGVGVPMGJqS2pna29nUT09--a13f80268d5fe11134a1450dc96ec081477f5f23; __cf_bm=dO5ljnCFyBy2hr.aEIRD4p1TTclQ83GBh.UyF0oPYj4-1679992284-0-Aa9VhamemWleIq/3jnu+Pad5E++F8vvpVAHDweJfDCthwkAetJDOLDx+3PlpYEyge4m0Dfdykp/j8YRAT+jBzyIIuBwlidhjUbQm9Kqqym9J; _dd_s=rum=0&expire=1679993184886; viewport_size=859");

		int responseCode = connection.getResponseCode();
		if(responseCode == 200){
			StringBuilder response = new StringBuilder();
			Scanner scanner = new Scanner(connection.getInputStream());
			while(scanner.hasNextLine()){
				String line = scanner.nextLine();
				String[] fullSizeUrls = line.split("full_size_url");
				if(fullSizeUrls.length > 1){
					for(String split : new ArrayList<>(Arrays.asList(fullSizeUrls).subList(1, fullSizeUrls.length))){
						response.append(split.split("\"")[2]);
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
