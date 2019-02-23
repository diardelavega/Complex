package com.example.demo.scrap.data;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SWayTest {

	@Test
	public void testCountryCompetitionInit() {
		
		SWay sw = new SWay();
		sw.countryCompetitionInit();
	}

}
