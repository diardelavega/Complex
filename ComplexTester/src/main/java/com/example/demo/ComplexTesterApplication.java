package com.example.demo;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.example.demo.before.jobs.ConfigProperties;
import com.example.demo.before.jobs.StructLoader;
import com.example.demo.mar.cod.entity.siteA.CCRepo;
import com.example.demo.mar.cod.entity.sites.ASiteCountryCompetition;
import com.example.demo.mar.cod.entity.sites.ASiteRepo;
import com.example.demo.scrap.data.SPHome;
import com.example.demo.scrap.data.SWay;
import com.example.demo.scrap.data.XSHome;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
public class ComplexTesterApplication {

	Logger log = LoggerFactory.getLogger(this.getClass());

	public static void main(String[] args) {
		SpringApplication.run(ComplexTesterApplication.class, args);
	}

	@Autowired
	StructLoader sl; 
	
	@Autowired
////	SPHome sp ;
//	SWay sp;
//	XSHome sp;
	ConfigProperties conpro;

	@Bean
	CommandLineRunner init() {
		try {
			System.out.println(conpro.toString());
			
//			List<ASiteCountryCompetition> temp = asrepo.findAll();
			
//			StructLoader sl = new StructLoader();
			sl.aSiteCountryCompLoad();
			log.info("size = {}", sl.getaSiteCountryCompStruct().length);
			
			
//			sp.countryCompetitionInit();
//			Logger log = LoggerFactory.getLogger(ComplexTesterApplication.class);
//			String br1 = "{\"level\":\"2\"}", br2 = "{\"area_id\":\"9\",\"level\":2,\"item_key\":\"area_id\"}";
//
//			String uri = "https://us.soccerway.com/a/block_competitions_index_club_domestic?block_id=page_competitions_1_block_competitions_index_club_domestic_4"
//					+ "&callback_params={br1}&action=expandItem&params={br2}\n";
//
//			RestTemplate restTemplate = new RestTemplate();
//			String result = restTemplate.getForObject(uri, String.class, br1, br2);
//
//			ObjectMapper mapper = new ObjectMapper();
//			JsonNode jn = mapper.readTree(result.toString());
//			JsonNode aa = jn.get("commands").get(0).get("parameters").get("content");
//			
//			String html = aa.asText();
//			String html ="<ul class=\"competitions\" data-level=\"2\" ><li class=\" odd\" ><div class=\"row\"><a href=\"/national/albania/super-league/c48/\" class=\"competition\" >Superliga</a><span class=\"type\">Domestic league</span><span class=\"season\">2018/2019</span></div></li><li class=\" even\" ><div class=\"row\"><a href=\"/national/albania/league-1/c578/\" class=\"competition\" >1st Division</a><span class=\"type\">Domestic league</span><span class=\"season\">2018/2019</span></div></li><li class=\" odd\" ><div class=\"row\"><a href=\"/national/albania/kategoria-e-dyte/c672/\" class=\"competition\" >2nd Division</a><span class=\"type\">Domestic league</span><span class=\"season\">2018/2019</span></div></li><li class=\" even\" ><div class=\"row\"><a href=\"/national/albania/cup/c202/\" class=\"competition\" >Cup</a><span class=\"type\">Domestic cup</span><span class=\"season\">2018/2019</span></div></li><li class=\" odd\" ><div class=\"row\"><a href=\"/national/albania/super-cup/c840/\" class=\"competition\" >Super Cup</a><span class=\"type\">Domestic super cup</span><span class=\"season\">2018/2019</span></div></li><li class=\" even\" ><div class=\"row\"><a href=\"/national/albania/play-offs-12/c940/\" class=\"competition\" >Play-offs 1/2</a><span class=\"type\">Domestic cup</span><span class=\"season\"></span></div></li><li class=\" odd\" ><div class=\"row\"><a href=\"/national/albania/play-offs-23/c941/\" class=\"competition\" >Play-offs 2/3</a><span class=\"type\">Domestic cup</span><span class=\"season\">2017/2018</span></div></li><li class=\" even\" ><div class=\"row\"><a href=\"/national/albania/kampionati-femrave/c1577/\" class=\"competition\" >Kampionati Femrave</a><span class=\"type\">Domestic league</span><span class=\"season\">2018/2019</span></div></li></ul>";
//			Document supDoc = Jsoup.parse(html);
//			Elements rows = supDoc.getElementsByTag("ul");
//			for( Node r:rows.get(0).childNodes()) {
//				log.info("-----------------------{}", r.toString());
//			}
//			Elements divs = Jsoup.parse(html).getElementsByTag("ul").get(0).getElementsByTag("div");
////			ul.getele
//			for(  Element r:divs) {
//				Element a = r.getElementsByTag("a").get(0);
//				String link =a.attr("href").toString();
//				String compName =a.text();
//				String type =  r.getElementsByTag("span").get(0).text();
//				
//				if(type=="Domestic cup")
//					continue;
//				
//				log.info("-----------------------{}", link);
//				log.info("-----------------------{}", compName);
//				log.info("-----------------------{}", type);
//				log.info("");
//			}

//			log.info("{}", aa);
//			log.info("{}", aa.asText());
//			log.info("{}", aa.textValue());
//			log.info("{}", aa.toString());

//			System.out.println("\n\n\n  " + aa + "  \n\n\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
}
