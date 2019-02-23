package com.example.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ComplexTesterApplicationTests {

	@Test
	public void contextLoads() {
		 final String uri = "https://us.soccerway.com/a/block_competitions_index_club_domestic?block_id=page_competitions_1_block_competitions_index_club_domestic_4"
		 		+ "&callback_params={\"level\":\"2\"}&action=expandItem&params={\"area_id\":\"9\",\"level\":2,\"item_key\":\"area_id\"}\n" ;
		 		
	     
		    RestTemplate restTemplate = new RestTemplate();
		    String result = restTemplate.getForObject(uri, String.class);
		     
		    System.out.println(result);
	}

}

