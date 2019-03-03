package com.example.demo.scrap.data;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class JSoupParse {
	public static Document getDoc(String url) {
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
}
