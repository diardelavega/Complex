package com.example.demo;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.entity.Matches;
import com.example.demo.entity.MatchesRepo;
import com.example.demo.mar.cod.entity.sites.CSiteCountryCompetition;
import com.example.demo.mar.cod.entity.sites.CSiteRepo;

public class ScrapXSSoup {

	@Autowired
	CSiteRepo Crepo;
	@Autowired
	MatchesRepo mrepo;

	private Logger log = LoggerFactory.getLogger(this.getClass());

	private CSiteCountryCompetition cs;
	private List<CSiteCountryCompetition> cclist = new ArrayList<>();
	private Matches match;
	private String url;
	private String country = "", competition = "", grup = "", link = "";
	int idCounter = 1;
	private List<Matches> matchesList = new ArrayList<>();
	private WebDriver dr = null;

	@Test
	public void action() throws IOException {
		String baseUrl = "https://www.xscores.com/soccer/leagueresults";
		Document doc;
//		String baseUrl = "C:/Users/User/Documents/CODE/Resources/XScores.html";
////		String baseUrl ="C:\\Users\\User\\Documents\\CODE\\Resources\\XScores.html";
//		File input = new File(baseUrl);
//		doc = Jsoup.parse(input, "UTF-8");

		String curentUrl = baseUrl;
		int compCounter = 0;

		try {
			doc = getDoc(curentUrl);
//			internalData(doc);
			Element form = doc.getElementById("theForm");
			Elements opts = form.getElementById("countryName").getElementsByTag("option");
			for (Element e : opts) {
				country = e.text();
				if (!validCountry(country))
					continue;
				url = urlBuilder(curentUrl, country, competition, grup);
				if (!curentUrl.equals(url)) {
					curentUrl = url;
					doc = getDoc(curentUrl);
					form = doc.getElementById("theForm");
				}
				compCounter = 0;

				Elements ligs = form.getElementById("leagueName").getElementsByTag("option");
				for (Element e2 : ligs) {
					if (compCounter >= 5)// get up to 5 competitions
						break;
					competition = e2.text();
					if (!validCompetition(competition))
						continue;
					url = urlBuilder(curentUrl, country, competition, grup);
					if (!curentUrl.equals(url)) {
						curentUrl = url;
						doc = getDoc(curentUrl);
						form = doc.getElementById("theForm");
					}

					try {
						Elements grups = form.getElementById("league1Select").getElementById("leagueName1")
								.getElementsByTag("option");
						if (grups.size() <= 1) {
							internalData(doc);
							continue;
						}
						for (Element e3 : grups) {
							grup = e3.text();
							if (!validCompetition(grup))
								continue;
							url = urlBuilder(curentUrl, country, competition, grup);
							if (!curentUrl.equals(url)) {
								curentUrl = url;
								doc = getDoc(curentUrl);
								form = doc.getElementById("theForm");
							}
							internalData(doc);// url with group
						}
						continue;// go to next competition
					} catch (Exception e1) {
						log.error("Something whent wrong at the groups");
					}

					internalData(doc);// it hasnt entered in groups
				} // for competition elments

			} // for country elements

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Crepo.saveAll(cclist);
		}

	}

	public Document getDoc(String url) {
		Document doc = null;
		try {
			doc = Jsoup.connect(url)
					.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
					.maxBodySize(0).timeout(600000).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doc;
	}

	private void saveMatches() {
		try {
			mrepo.saveAll(matchesList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		matchesList.clear();

	}

	private void internalData(Document doc) {
//		WebElement parent = doc.findElement(By.id("scoretable"));

		Element table = doc.getElementById("scoretable");
		Elements divs = table.select("div[class='score_row round_header score_header'], "/* round */
				+ "div[class='score_row padded_date country_header'], "/* date time */
				+ "div[class='score_row match_line e_true'], "
				+ "div[ class='score_row match_line o_true']");/* match */

		if (divs.size() == 0) {
			log.info("No Matches Here  {}", url);
			return;
		}
		int week = 0;
		LocalDate date = null;
		for (Element d : divs) {
			if (d.attr("class").endsWith("score_header")) {
				week = strToInt(d.text().replace("Round ", ""), 0);
			} else if (d.attr("class").endsWith("country_header")) {
				date = dater(d.text());
			} else if (d.attr("class").endsWith("o_true") || d.attr("class").endsWith("e_true")) {
				try {
					match = new Matches();
					match.setDate(date);
					match.setWeekNr(week);

					Element time = d.getElementsByClass("score_ko score_cell").get(0);
					match.setTime(LocalTime.parse(time.text()));

					Element teamsNcards = d.getElementsByClass("score_teams  score_cell").get(0);
					String t1 = teamsNcards.getElementsByClass("score_home_txt").text();
					String t2 = teamsNcards.getElementsByClass("score_away_txt").text();
					match.setT1(t1);
					match.setT2(t2);
//
					String yel_hom = teamsNcards.getElementsByClass("y_cards").get(0).text();
					String red_hom = teamsNcards.getElementsByClass("r_cards").get(0).text();
					match.setT1Yellow(strToIntCart(yel_hom));
					match.setT1Red(strToIntCart(red_hom));
//
					String yel_awa = teamsNcards.getElementsByClass("y_cards").get(1).text();
					String red_awa = teamsNcards.getElementsByClass("y_cards").get(1).text();
					match.setT2Yellow(strToIntCart(yel_awa));
					match.setT2Red(strToIntCart(red_awa));
//
					String[] temp = null;
					temp = d.getElementsByClass("score_ht score_cell").text().split(" - ");
					if (temp.length > 1) {
						String ht1 = temp[0];
						String ht2 = temp[1];
						match.setT1HtScore(strToIntGols(ht1));
						match.setT2HtScore(strToIntGols(ht2));
					}

					temp = d.getElementsByClass("score_score score_cell").text().split(" - ");
					if (temp.length > 1) {
						String ft1 = temp[0];
						String ft2 = temp[1];
						match.setT1HtScore(strToIntGols(ft1));
						match.setT2HtScore(strToIntGols(ft2));
					}

					temp = d.getElementsByClass("score_et score_cell").text().split(" - ");
					if (temp.length > 1) {
						String et1 = temp[0];
						String et2 = temp[1];
						match.setT1HtScore(strToIntGols(et1));
						match.setT2HtScore(strToIntGols(et2));
					}

					temp = d.getElementsByClass("score_pen score_cell").text().split(" - ");
					if (temp.length > 1) {
						String pt1 = temp[0];
						String pt2 = temp[1];
						match.setT1HtScore(strToIntGols(pt1));
						match.setT2HtScore(strToIntGols(pt2));
					}

					match.setCompId(idCounter);
					matchesList.add(match);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		try {
			mrepo.saveAll(matchesList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		cs = new CSiteCountryCompetition();
		cs.setCompetition(competition);
		cs.setCountry(country);
		cs.setId(idCounter);
		cs.setLink(link);
		cs.setGrup(grup);
		log.info(cs.toString());
		cclist.add(cs);
		idCounter++;

	}

	private Integer strToIntGols(String scor) {
		return strToInt(scor, -1);
	}

	private Integer strToIntCart(String cart) {
		return strToInt(cart, 0);
	}

	public int strToInt(String s, int def) {
		int i = def;
		try {
			i = Integer.parseInt(s);
		} catch (NumberFormatException e) {
//			e.printStackTrace();
		}
		return i;
	}

	public LocalDate dater(String arg) {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MM-yyyy");
			LocalDate localDate = LocalDate.parse(arg, formatter);
			return localDate;
		} catch (Exception e) {
//			e.printStackTrace();
		}
		return LocalDate.now();
	}

	public boolean validCountry(String country) {
		country = country.toLowerCase();
		if (country.contains("world") || country.contains("friendly") || country.contains("africa")
				|| country.contains("america") || country.contains("europe") || country.contains("asia"))
			return false;
		return true;
	}

	public boolean validCompetition(String compName) {
		compName = compName.toLowerCase();
		if (compName.contains("off") || compName.contains("super") || compName.contains("non")
//				compName.contains("torneo") ||  compName.contains("trophy") || compName.contains("coupe")
				|| compName.contains("world") || compName.contains("torneo") || compName.contains("africa")
				|| compName.contains("america") || compName.contains("turope") || compName.contains("asia")
				|| compName.contains("amateur") || compName.contains("qualification") || compName.contains("women")
				|| compName.contains("u21") || compName.contains("u17") || compName.contains("u19")
				|| compName.contains("u18") || compName.contains("youth") || compName.contains("juvenil")
				|| compName.contains("juniores") || compName.contains("femra") || compName.contains("femenina")
				|| compName.contains("frauen"))
			return false;
		return true;
	}

	public String urlBuilder(String url, String country, String comp, String grup) {
		country = country.replace(" ", "_").toLowerCase();
		comp = comp.replace(" ", "_").toLowerCase();
		long count = url.chars().filter(ch -> ch == '/').count();
		if (count <= 4) {
			url += "/" + country + "/" + comp + "/2018-2019";
		} else {
			String[] temp = url.split("/");
			temp[5] = country;
			temp[6] = comp;
			url = "";
			for (int i = 0; i < temp.length; i++) {
				if (i > 2 && temp[i].equals(""))
					continue;
				url += temp[i] + "/";
			}
		}

		if (!grup.equals(""))
			url += grup;

		return url;
	}

	public WebDriver getDriver(String url) {
		if (dr != null)
			dr.close();
		String chromeDriverPath = "src/main/resources/static/chromedriver.exe";
		System.setProperty("webdriver.chrome.driver", chromeDriverPath);
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1200", "--ignore-certificate-errors",
				"--silent");
		dr = new ChromeDriver(options);
		dr.manage().timeouts().pageLoadTimeout(50, TimeUnit.SECONDS);
		dr.get(url);
		return dr;
	}

}
