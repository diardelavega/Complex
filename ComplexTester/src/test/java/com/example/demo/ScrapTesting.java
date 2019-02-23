package com.example.demo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.entity.Matches;
import com.example.demo.entity.MatchesRepo;
import com.example.demo.mar.cod.entity.sites.CSiteCountryCompetition;
import com.example.demo.mar.cod.entity.sites.CSiteRepo;
import com.example.demo.scrap.data.SeleniumDriver;

public class ScrapTesting {

	@Autowired
	CSiteRepo Crepo;
	@Autowired
	MatchesRepo mrepo;
	@Autowired
	SeleniumDriver selenium;

	private CSiteCountryCompetition cs;
	private List<CSiteCountryCompetition> cclist = new ArrayList<>();
	private Matches match;
	private String url;
	private String country = "", competition = "", grup = "", link = "";
	int idCounter = 1;
	private List<Matches> matchesList= new ArrayList<>();
	private WebDriver dr = null;

	@Test
	public void testxs() {
//		WebDriver dr = getDriver("C:/Users/User/Documents/CODE/Resources/XScores.html");
		// String baseUrl = "https://www.xscores.com/soccer/leagueresults";

		try {
			String baseUrl = "https://www.xscores.com/soccer/leagueresults/albania/premier_league/2018-2019/r/0";
			dr = getDriver(baseUrl);

			String curentUrl;
			int cupCounter = 0, compCounter = 0;
			curentUrl = baseUrl;

			List<WebElement> countryEl = dr.findElement(By.id("countryName")).findElements(By.tagName("option"));
			for (WebElement el : countryEl) {
				country = el.getAttribute("value");
				if (!validCountry(country))
					continue;
				url = urlBuilder(curentUrl, country, competition, grup);
				if (!curentUrl.equals(url)) {
					curentUrl = url;
					dr = getDriver(curentUrl);
				}
				compCounter = 0;

				List<WebElement> compEl = dr.findElement(By.id("leagueName")).findElements(By.tagName("option"));
				for (WebElement el2 : compEl) {
					if (compCounter >= 5)// get up to 5 competitions
						break;
					competition = el2.getAttribute("value");
					if (!validCompetition(competition))
						continue;
					url = urlBuilder(curentUrl, country, competition, grup);
					if (!curentUrl.equals(url)) {
						curentUrl = url;
						dr = getDriver(curentUrl);
					}

					WebElement group = null;
					List<WebElement> groups = null;
					try {
						group = dr.findElement(By.id("leagueName1"));
						if (group != null)
							groups = group.findElements(By.tagName("option"));
						if (groups != null && groups.size() > 0) {
							for (WebElement r : groups) {
								grup = r.getAttribute("value");
								if (!validCompetition(grup))
									continue;
								url = urlBuilder(curentUrl, country, competition, grup);
								if (!curentUrl.equals(url)) {
									curentUrl = url;
									dr = getDriver(curentUrl);
								}
								internalData(dr);// url with group
							}
							continue;
						}
					} catch (Exception e) {
					}

					internalData(dr);
				} // for competitions

				saveMatches();
			} // for countries

			Crepo.saveAll(cclist);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dr.close();
		}
	}

	private void saveMatches() {
		try {
			mrepo.saveAll(matchesList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		matchesList.clear();

	}

	private void internalData(WebDriver dr) {
		WebElement parent = dr.findElement(By.id("scoretable"));

		List<WebElement> divs = parent.findElements(By.xpath(".//div["
				+ " @class='score_row round_header score_header' or "/* round */
				+ " @class='score_row padded_date country_header' or "/* date time */
				+ " @class='score_row match_line e_true' or @class='score_row match_line o_true' " + "]"));/* match */

		if (divs.size() == 0) {
			System.out.println("No Matches Here");
			return;
		}
		int week = 0;
		LocalDate date = null;
		for (WebElement d : divs) {
			if (d.getAttribute("class").endsWith("score_header")) {
				week = strToInt(d.getText().replace("Round ", ""), 0);

			} else if (d.getAttribute("class").endsWith("country_header")) {
				date = dater(d.getText());
			} else if (d.getAttribute("class").endsWith("o_true") || d.getAttribute("class").endsWith("e_true")) {
				try {
					match = new Matches();
					match.setDate(date);
					match.setWeekNr(week);

					WebElement timed = d.findElement(By.xpath(".//div[@class='score_ko score_cell']"));
					match.setTime(LocalTime.parse(timed.getText()));

					WebElement teamsNcards = d.findElement(By.xpath(".//div[@class='score_teams  score_cell']"));
					String t1 = teamsNcards.findElement(By.xpath("./div[1]/div[1]")).getText();
					String t2 = teamsNcards.findElement(By.xpath("./div[2]/div[1]")).getText();
					match.setT1(t1);
					match.setT2(t2);

					String yel_hom = teamsNcards.findElement(By.xpath("./div[1]/div[2]/div[2]")).getText();
					String red_hom = teamsNcards.findElement(By.xpath("./div[1]/div[2]/div[3]")).getText();
					match.setT1Yellow(strToIntCart(yel_hom));
					match.setT1Red(strToIntCart(red_hom));

					String yel_awa = teamsNcards.findElement(By.xpath("./div[2]/div[2]/div[2]")).getText();
					String red_awa = teamsNcards.findElement(By.xpath("./div[2]/div[2]/div[3]")).getText();
					match.setT2Yellow(strToIntCart(yel_awa));
					match.setT2Red(strToIntCart(red_awa));

					String ht = d.findElement(By.xpath("./div[4]")).getText();
					if (ht.split(" - ").length > 1) {
						String ht1 = ht.split(" - ")[0];
						String ht2 = ht.split(" - ")[1];
						match.setT1HtScore(strToIntGols(ht1));
						match.setT2HtScore(strToIntGols(ht2));
					}

					String ft = d.findElement(By.xpath("./div[5]")).getText();
					if (ft.split(" - ").length > 1) {
						String ft1 = ft.split(" - ")[0];
						String ft2 = ft.split(" - ")[1];
						match.setT1FtScore(strToIntGols(ft1));
						match.setT2FtScore(strToIntGols(ft2));
					}

//					String et = d.findElement(By.xpath("./div[6]")).getText();
//					if (et.split(" - ").length > 1) {
//						String et1 = et.split(" - ")[0];
//						String et2 = et.split(" - ")[1];
//						match.setT1EtScore(strToIntGols(et1));
//						match.setT2EtScore(strToIntGols(et2));
//					}
//					String pt = d.findElement(By.xpath("./div[7]")).getText();
//					if (pt.split(" - ").length > 1) {
//						String pt1 = et.split(" - ")[0];
//						String pt2 = et.split(" - ")[1];
//						match.setT1PtScore(strToIntGols(pt1));
//						match.setT2PtScore(strToIntGols(pt2));
//					}
					match.setCompId(idCounter);
					matchesList.add(match);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		cs= new CSiteCountryCompetition();
		cs.setCompetition(competition);
		cs.setCountry(country);
		cs.setId(idCounter);
		cs.setLink(link);
		cs.setGrup(grup);
		System.out.println(cs.toString());
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

//	@Test
	public void test() {
		System.setProperty("webdriver.chrome.driver", "./src/main/resources/static/chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		String baseURL = "http://demo.guru99.com/test/newtours/register.php";
		driver.get(baseURL);
		Select drpCountry = new Select(driver.findElement(By.name("country")));
		drpCountry.selectByVisibleText("ANTARCTICA");

		// Selecting Items in a Multiple SELECT elements
//		driver.get("http://jsbin.com/osebed/2");
//		Select fruits = new Select(driver.findElement(By.id("fruits")));
//		fruits.selectByVisibleText("Banana");
//		fruits.selectByIndex(1);
	}
}
