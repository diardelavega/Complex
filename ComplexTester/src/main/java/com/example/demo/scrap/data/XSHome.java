package com.example.demo.scrap.data;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.entity.Matches;
import com.example.demo.entity.MatchesRepo;
import com.example.demo.mar.cod.entity.sites.CSiteCountryCompetition;
import com.example.demo.mar.cod.entity.sites.CSiteRepo;

@Component
public class XSHome {

	@Autowired
	CSiteRepo Crepo;
	@Autowired
	MatchesRepo mrepo;

	private Logger log = LoggerFactory.getLogger(this.getClass());

	private CSiteCountryCompetition cs;
	private List<CSiteCountryCompetition> cclist = new ArrayList<>();
	private Matches match;
	private String country = "", competition = "", grup = "", url = "";
	int idCounter = 1;
	private List<Matches> matchesList = new ArrayList<>();
	Map<String, Integer> compProcess = new HashMap<>();

	Set<String> competitionsSet;// set of competitions for a country
	Map<String, Set<String>> ccAllreadyIn = new HashMap<String, Set<String>>();

	public void countryCompetitionInit() throws IOException {
		String baseUrl = "https://www.xscores.com/soccer/leagueresults";
		Document doc;
		String curentUrl;
		boolean grupFlag = false;// signal weather it was processed in grup phase
		boolean countryAllreadyInSkipper = false, compAllreadyInSkipper = false, grupAllreadyInSkipper = false;// as a
																												// signal
																												// to
																												// show
																												// which
																												// comps
																												// are
																												// allready
																												// in

		loadCcList();
//		String baseUrl = "C:/Users/User/Documents/CODE/Resources/XScores.html";
////		String baseUrl ="C:\\Users\\User\\Documents\\CODE\\Resources\\XScores.html";
//		File input = new File(baseUrl);
//		doc = Jsoup.parse(input, "UTF-8");

		String tempstr;
		if (cclist.size() == 0) {
			curentUrl = baseUrl;
			idCounter = 1;
		} else
			curentUrl = url;

		int compCounter = 0;

		try {
			doc = getDoc(curentUrl);
//			internalData(doc);
			Element form = doc.getElementById("theForm");
			Elements opts = form.getElementById("countryName").getElementsByTag("option");
			for (Element e : opts) {
				if (ccAllreadyIn.size() > 0)
					if (!countryAllreadyInSkipper) {
						if (!country.equals(e.text()))// continue till the last country in
							continue;
						else {
							countryAllreadyInSkipper = true;
							continue;
						}
					}

				country = e.text();
				if (!validCountry(country))
					continue;
				url = urlBuilder(curentUrl, country, competition, grup);
				if (!curentUrl.equals(url)) {
					curentUrl = url;
					doc = getDoc(curentUrl);
					form = doc.getElementById("theForm");
				}
				compCounter = ccAllreadyIn.get(country) != null ? ccAllreadyIn.get(country).size() : 0;

//				Set<String> competitionsSet = new HashSet<>();
				Element leagueElem = null;
				try {
					leagueElem = form.getElementById("leagueName");
				} catch (Exception e4) {
					e4.printStackTrace();
				}
				if(leagueElem==null)
					continue;
				Elements ligs = leagueElem.getElementsByTag("option");
				for (Element e2 : ligs) {
					if (compCounter >= 4)// get up to 5 competitions
						break;
					if (compCounter > 0)
						if (!compAllreadyInSkipper)
							if (!e2.text().equals(competition))// continue till the last competition in
								continue;
							else {
								compAllreadyInSkipper = true;
								continue;
							}
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
						Elements grups = form.getElementById("league1Select").getElementById("leagueName1")								.getElementsByTag("option");
						if (grups.size() <= 1) {
							internalData(doc);
							continue;
						}
						for (Element e3 : grups) {
							if (!grupAllreadyInSkipper) {
								if (!e3.text().equals(grup))// continue till the last competition in
									continue;
								else {
									grupAllreadyInSkipper = true;
									continue;
								}
							}

							grup = e3.text();
							if (!validCompetition(grup))
								continue;
							grup = grup.replace(competition, "");
							if (!grup.equals("")) {// if actually has a group
								url = urlBuilder(curentUrl, country, competition, grup);
								if (!curentUrl.equals(url)) {
									curentUrl = url;
									doc = getDoc(curentUrl);
									form = doc.getElementById("theForm");
								}
								if (ccAllreadyIn.get(country).contains(stringer(competition, grup)))
									continue;// go to next competition
								else {
									internalData(doc);// url with group
									grupFlag = true;
									grup = "";// reset group to empty
									continue;
								}
							}
						} // for of grups
					} catch (Exception e1) {
						log.error("Something whent wrong at the groups");
						e1.printStackTrace();
					}
					if (grupFlag) {
						grupFlag = false;
						continue;
					} else {
						if (ccAllreadyIn.get(country) != null) {
							if (ccAllreadyIn.get(country).contains(stringer(competition, grup)))
								continue;// go to next competition
						} else// if it was not processed in the grup section
							internalData(doc);// url with group
					}
				} // for competition elments
				ccAllreadyIn.put(country, competitionsSet);// add country<set of comps>

			} // for country elements

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Crepo.saveAll(cclist);
		}
	}

	public void grupProces() {
		grup = grup.replace(competition, "");

	}

	private boolean isInList(String country2, String competition2) {
		for (CSiteCountryCompetition c : cclist) {
			if (c.getCountry().equals(country2))
				if (c.getCompetition().equals(competition2))
					return true;
		}
		return false;
	}

	private void loadCcList() {
		cclist = Crepo.findAll();
		if (cclist.size() > 0) {
			idCounter = cclist.get(cclist.size() - 1).getId();
			url = cclist.get(cclist.size() - 1).getLink();
			country = cclist.get(cclist.size() - 1).getCountry();
			competition = cclist.get(cclist.size() - 1).getCompetition();
			initMapSet();
		}
	}

	private void initMapSet() {
		String ctr, cmp, grp;
		CSiteCountryCompetition c = cclist.get(0);
		ctr = c.getCountry();
		grp = c.getGrup();
		cmp = c.getCompetition();
		competitionsSet = new HashSet<>();
		competitionsSet.add(stringer(cmp, grp));
		for (int i = 1; i < cclist.size(); i++) {
			c = cclist.get(i);

			if (!c.getCountry().equals(ctr)) {
				ccAllreadyIn.put(ctr, competitionsSet);
				ctr = c.getCountry();
				competitionsSet = new HashSet<>();
			} else {
				grp = c.getGrup();
				cmp = c.getCompetition();
				competitionsSet.add(stringer(cmp, grp));
			}
		}
		ccAllreadyIn.put(ctr, competitionsSet);
	}

	public boolean isCountryAllreadyIn(String ctr) {
		if (ccAllreadyIn.get(ctr) != null)
			return true;
		return false;
	}

	public boolean isaCompetitionGroupAllreadyIn(String ctr, String cmp, String grp) {
		Set<String> tmpset = ccAllreadyIn.get(ctr);
		if (tmpset != null)
			if (tmpset.contains(stringer(cmp, grp)))
				return true;
		return false;
	}

	public String stringer(String comp, String grp) {
		if (!grp.equals(""))
			return comp + "_" + grp;// save to the set of comps
		else
			return comp;
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
						match.setT1FtScore(strToIntGols(ft1));
						match.setT2FtScore(strToIntGols(ft2));
					}

					temp = d.getElementsByClass("score_et score_cell").text().split(" - ");
					if (temp.length > 1) {
						String et1 = temp[0];
						String et2 = temp[1];
						match.setT1EtScore(strToIntGols(et1));
						match.setT2EtScore(strToIntGols(et2));
					}

					temp = d.getElementsByClass("score_pen score_cell").text().split(" - ");
					if (temp.length > 1) {
						String pt1 = temp[0];
						String pt2 = temp[1];
						match.setT1PtScore(strToIntGols(pt1));
						match.setT2PtScore(strToIntGols(pt2));
					}

					match.setCompId(idCounter);
					match.setInsDat(LocalDateTime.now());
					matchesList.add(match);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		saveMatches();

		cs = new CSiteCountryCompetition();
		cs.setCompetition(competition);
		cs.setCountry(country);
		cs.setId(idCounter);
		cs.setLink(url);
		cs.setGrup(grup);
		cs.setValid(1);
		cs.setInsDat(LocalDateTime.now());
		log.info(cs.toString());
		Crepo.save(cs);

		competitionsSet.add(stringer(competition, grup));// save to the set of comps
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

}
