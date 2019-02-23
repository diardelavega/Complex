package com.example.demo.scrap.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.example.demo.extra.TextCleaner;
import com.example.demo.mar.cod.entity.CompIdSiteLinlRepo;
import com.example.demo.mar.cod.entity.siteA.CCRepo;
import com.example.demo.mar.cod.entity.sites.ASiteCountryCompetition;
import com.example.demo.mar.cod.entity.sites.ASiteRepo;
import com.example.demo.mar.cod.entity.sites.BSiteCountryCompetition;
import com.example.demo.mar.cod.entity.sites.BSiteRepo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class SWay {

	@Autowired
	BSiteRepo brepo;
	@Autowired
	SeleniumDriver selenium;

	private String link, country;
	private BSiteCountryCompetition cc;
	private List<BSiteCountryCompetition> cclist = new ArrayList<>();

	public void countryCompetitionInit() {

//		WebDriver dr = selenium.getDriver("C:/Users/User/Documents/CODE/Resources/Soccerway.html");
		WebDriver dr = selenium.getDriver("https://us.soccerway.com/competitions/club-domestic/?ICID=TN_02_01");
		WebElement countryData = dr.findElement(By.id("page_competitions_1_block_competitions_index_club_domestic_4"));

		String country, compName, link, type = null;
		int idCounter = 1, cupCounter = 0, compCounter = 0;

		List<BSiteCountryCompetition> cclist = new ArrayList<>();
		for (WebElement el : countryData.findElements(By.xpath(".//li"))) {// for countries
			country = TextCleaner.convertNonAscii(el.getText().toString());

			if (!validCountry(country))
				continue;

			el.click();
			WebDriverWait wait = new WebDriverWait(dr, 100);
			wait.until(ExpectedConditions.elementToBeClickable(el));

			compCounter = 0;
			cupCounter = 0;
			try {
				List<WebElement> divs = el.findElements(By.xpath(".//div"));
				for (int i = 1; i < divs.size(); i++) {

					if (compCounter >= 5)// add limited nr of competitions
						break;

					WebElement a = divs.get(i).findElement(By.tagName("a"));
					compName = TextCleaner.convertNonAscii(a.getText());
					if (!validCompetition(compName))
						continue;
					link = a.getAttribute("href");
					type = divs.get(i).findElement(By.xpath(".//span [@class='type']")).getText();

					if (type.contains("cup")) {
						if (cupCounter == 0)
							cupCounter++;
						else
							continue;
					}

					cc = new BSiteCountryCompetition();
					cc.setId(idCounter);
					cc.setLink(link);
					cc.setCountry(country);
					cc.setCompetition(compName);
					cclist.add(cc);
					System.out.println(cc.toString());
					idCounter++;
					compCounter++;
				}
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}

		}

		brepo.saveAll(cclist);
	}

	public boolean validCountry(String country) {
		if (country.contains("World") || country.contains("Friendly") || country.contains("Africa")
				|| country.contains("America") || country.contains("Europe") || country.contains("Asia"))
			return false;
		return true;
	}

	public boolean validCompetition(String compName) {
		if (compName.contains("off") || compName.contains("Super")
//				compName.contains("Torneo") ||  compName.contains("Trophy") || compName.contains("Coupe")
				|| compName.contains("World") || compName.contains("Torneo") || compName.contains("Africa")
				|| compName.contains("America") || compName.contains("Europe") || compName.contains("Asia")
				|| compName.contains("Amateur") || compName.contains("Qualification") || compName.contains("Women")
				|| compName.contains("U21") || compName.contains("U17") || compName.contains("U19")
				|| compName.contains("U18") || compName.contains("Youth") || compName.contains("Juvenil")
				|| compName.contains("Juniores") || compName.contains("Femra") 
				|| compName.contains("Femenina")|| compName.contains("Frauen")
				)
			return false;
		return true;
	}

	public void getComp(int linkCounter, String country) {
		try {
			String br1 = "{\"level\":\"2\"}",
					br2 = "{\"area_id\":\"" + linkCounter + "\",\"level\":2,\"item_key\":\"area_id\"}";

			String uri = "https://us.soccerway.com/a/block_competitions_index_club_domestic?block_id=page_competitions_1_block_competitions_index_club_domestic_4"
					+ "&callback_params={br1}&action=expandItem&params={br2}\n";

			RestTemplate restTemplate = new RestTemplate();
			try {
				String result = restTemplate.getForObject(uri, String.class, br1, br2);

				ObjectMapper mapper = new ObjectMapper();
				JsonNode jn = mapper.readTree(result.toString());
				JsonNode aa = jn.get("commands").get(0).get("parameters").get("content");

				String ul = aa.toString();
				extrapolate(ul, country);
			} catch (Exception e) {
				e.printStackTrace();
			}

//			System.out.println("\n\n\n  " + aa + "  \n\n\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void extrapolate(String html, String country) throws UnsupportedEncodingException {
//		String html ="<ul class=\"competitions\" data-level=\"2\" ><li class=\" odd\" ><div class=\"row\"><a href=\"/national/albania/super-league/c48/\" class=\"competition\" >Superliga</a><span class=\"type\">Domestic league</span><span class=\"season\">2018/2019</span></div></li><li class=\" even\" ><div class=\"row\"><a href=\"/national/albania/league-1/c578/\" class=\"competition\" >1st Division</a><span class=\"type\">Domestic league</span><span class=\"season\">2018/2019</span></div></li><li class=\" odd\" ><div class=\"row\"><a href=\"/national/albania/kategoria-e-dyte/c672/\" class=\"competition\" >2nd Division</a><span class=\"type\">Domestic league</span><span class=\"season\">2018/2019</span></div></li><li class=\" even\" ><div class=\"row\"><a href=\"/national/albania/cup/c202/\" class=\"competition\" >Cup</a><span class=\"type\">Domestic cup</span><span class=\"season\">2018/2019</span></div></li><li class=\" odd\" ><div class=\"row\"><a href=\"/national/albania/super-cup/c840/\" class=\"competition\" >Super Cup</a><span class=\"type\">Domestic super cup</span><span class=\"season\">2018/2019</span></div></li><li class=\" even\" ><div class=\"row\"><a href=\"/national/albania/play-offs-12/c940/\" class=\"competition\" >Play-offs 1/2</a><span class=\"type\">Domestic cup</span><span class=\"season\"></span></div></li><li class=\" odd\" ><div class=\"row\"><a href=\"/national/albania/play-offs-23/c941/\" class=\"competition\" >Play-offs 2/3</a><span class=\"type\">Domestic cup</span><span class=\"season\">2017/2018</span></div></li><li class=\" even\" ><div class=\"row\"><a href=\"/national/albania/kampionati-femrave/c1577/\" class=\"competition\" >Kampionati Femrave</a><span class=\"type\">Domestic league</span><span class=\"season\">2018/2019</span></div></li></ul>";
		Elements divs = Jsoup.parse(html).getElementsByTag("ul").get(0).getElementsByTag("div");
		int cupCounter = 0;
		int compCounter = 0;
		for (Element r : divs) {
			if (compCounter >= 5)// add limited nr of competitions
				break;
			Element a = r.getElementsByTag("a").get(0);
			String link = URLDecoder.decode(a.attr("href").replace("\\\"", ""), "UTF-8");
//			String link = a.attr("href").toString();
			String compName = TextCleaner.convertNonAscii(a.text());
			String type = r.getElementsByTag("span").get(0).text();

			if (compName.contains("off") || compName.contains("Super")
//					compName.contains("Torneo") ||  compName.contains("Trophy") || compName.contains("Coupe")
					|| compName.contains("World") || compName.contains("Torneo") || compName.contains("Africa")
					|| compName.contains("America") || compName.contains("Europe") || compName.contains("Asia")
					|| compName.contains("Amateur") || compName.contains("Qualification") || compName.contains("Women")
					|| compName.contains("U21") || compName.contains("U19") || compName.contains("U18")
					|| compName.contains("Youth") || compName.contains("Femra"))
				continue;

			if (type.contains("cup")) {
				if (cupCounter == 0)
					cupCounter++;
				else
					continue;
			}

			cc = new BSiteCountryCompetition();
//			cc.setId(i);
			cc.setLink(link);
			cc.setCountry(country);
			cc.setCompetition(compName);
			cclist.add(cc);
			System.out.println(cc.toString());
//			i++;
			compCounter++;
		}
	}

//	public String getCompetitions(int swCounter) {
////				System.out.println("CommandLineRunner running in the UnsplashApplication class...");
//		String url = compLink + swCounter + secondPart;
//
//		try {
//			URL urlForGetRequest = new URL(url);
//			String readLine = null;
//			HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
//			conection.setRequestMethod("GET");
////				conection.setRequestProperty("userId", "a1bcdef"); // set userId its a sample here
//			int responseCode = conection.getResponseCode();
//			if (responseCode == HttpURLConnection.HTTP_OK) {
//				BufferedReader in = new BufferedReader(new InputStreamReader(conection.getInputStream()));
//				StringBuffer response = new StringBuffer();
//				while ((readLine = in.readLine()) != null) {
//					response.append(readLine);
//				}
//				in.close();
//
//				ObjectMapper mapper = new ObjectMapper();
//				JsonNode jn = mapper.readTree(response.toString());
//				String aa = jn.get("content").toString();
//				// print result
//				System.out.println("JSON String Result " + aa);
//				// GetAndPost.POSTRequest(response.toString());
//			} else {
//				System.out.println("GET NOT WORKED");
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//
//	}
}
