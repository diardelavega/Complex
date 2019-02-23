package com.example.demo.scrap.data;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.extra.TextCleaner;
import com.example.demo.mar.cod.entity.CompIdSiteLink;
import com.example.demo.mar.cod.entity.CompIdSiteLinlRepo;
import com.example.demo.mar.cod.entity.siteA.CCRepo;
import com.example.demo.mar.cod.entity.siteA.CountryCompetition;
import com.example.demo.mar.cod.entity.sites.ASiteCountryCompetition;
import com.example.demo.mar.cod.entity.sites.ASiteRepo;

@Component
public class SPHome {

	@Autowired
	ASiteRepo arepo;
	@Autowired
//	CompIdSiteLinlRepo idlinkrepo;
	SeleniumDriver selenium;

	public void countryCompetitionInit() throws UnsupportedEncodingException {
//		String chromeDriverPath = "src/main/resources/static/chromedriver.exe";
//		System.setProperty("webdriver.chrome.driver", chromeDriverPath);
//		ChromeOptions options = new ChromeOptions();
//		options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1200", "--ignore-certificate-errors",
//				"--silent");
//		WebDriver driver = new ChromeDriver(options);
//
//		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
//
//		driver.get("C:/Users/User/Documents/CODE/Resources/SoccerPunter.html");
		WebDriver dr = selenium.getDriver("C:/Users/User/Documents/CODE/Resources/SoccerPunter.html");
		List<WebElement> opList = dr.findElements(By.xpath("//select[@onchange='goComp(this)']/option"));// .get(4);
//		System.out.println(opList.size());

		String s, a;
		String[] temp;
		String prevCountry = "";
		int sameCountryCounter = 1;
		List<ASiteCountryCompetition> cclist = new ArrayList<>();
		ASiteCountryCompetition cc;
//		List<CompIdSiteLink> idLinkList = new ArrayList<>();
//		CompIdSiteLink idl;
		String word;// temporary word
		int i = 0;
		for (WebElement o : opList) {
			s = TextCleaner.convertNonAscii(o.getText());
			a = URLDecoder.decode(o.getAttribute("value"), "UTF-8");
			if (
//					s.contains("Cup") || s.contains("Coppa") || s.contains("Copa")|| s.contains("Coupe") || 
			s.contains("off") || s.contains("Trophy") || s.contains("Coupe") || s.contains("World")|| s.contains("Torneo")
					|| s.contains("Africa") || s.contains("America") || s.contains("Europe") || s.contains("Asia")
					|| s.contains("Qualification")
			/* || s.endsWith("2015") */) {
				continue;
			} else {
				temp = a.split("/");
				if (temp.length <= 1)
					continue;
				try {
					i++;
					cc = new ASiteCountryCompetition();
//					idl = new CompIdSiteLink();
					cc.setId(i);
//					idl.setCompId(i);
//					idl.setSite1Link(a);

					cc.setLink(a);
					cc.setCountry(TextCleaner.convertNonAscii(temp[2].replace("-", " ")));
					word = TextCleaner.convertNonAscii(temp[3].replace("-2018-2019", "").replaceAll("-", " "));
					word = word.replace(" 2019", "");
					cc.setCompetition(word);

//					if (prevCountry.equals(cc.getCountry())) {
//						sameCountryCounter++;
//						cc.setLevl(sameCountryCounter);
//					} else {
//						cc.setLevl(1);
//						sameCountryCounter = 1;
//					}
//					prevCountry = cc.getCountry();

					cclist.add(cc);
//					idLinkList.add(idl);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} // else
		} // for

		arepo.saveAll(cclist);
//		idlinkrepo.saveAll(idLinkList);

	}
}
