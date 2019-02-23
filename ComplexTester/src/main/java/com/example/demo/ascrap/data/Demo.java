package com.example.demo.ascrap.data;

import java.io.UnsupportedEncodingException;

import com.example.demo.scrap.data.SPHome;
import com.example.demo.scrap.data.SWay;

public class Demo {

	public static void main(String[] args) {
//		SPHome sp = new SPHome();
		SWay sw = new SWay();
		try {
			sw.countryCompetitionInit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
