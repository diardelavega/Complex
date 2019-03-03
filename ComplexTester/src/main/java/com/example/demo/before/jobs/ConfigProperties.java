package com.example.demo.before.jobs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:static/config.properties")
@ConfigurationProperties
public class ConfigProperties {

	@Value("${a.site.base.url}")
	private String aSite;

	@Value("${b.site.base.url}")
	private String bSite;

	@Value("${c.site.base.url}")
	private String cSite;
	
	@Value("${chrome.driver.path}")
	private String chromeDriverPath;
	
	@Value("${page.load.time.seconds}")
	private int pageLoadTime;
	

	
	
	public String getaSite() {
		return aSite;
	}

	public void setaSite(String aSite) {
		this.aSite = aSite;
	}

	public String getbSite() {
		return bSite;
	}

	public void setbSite(String bSite) {
		this.bSite = bSite;
	}

	public String getcSite() {
		return cSite;
	}

	public void setcSite(String cSite) {
		this.cSite = cSite;
	}

	
	public String getChromeDriverPath() {
		return chromeDriverPath;
	}

	public void setChromeDriverPath(String chromeDriverPath) {
		this.chromeDriverPath = chromeDriverPath;
	}

	public int getPageLoadTime() {
		return pageLoadTime;
	}

	public void setPageLoadTime(int pageLoadTime) {
		this.pageLoadTime = pageLoadTime;
	}

	@Override
	public String toString() {
		return "ConfigProperties [aSite=" + aSite + ", bSite=" + bSite + ", cSite=" + cSite + ", chromeDriverPath="
				+ chromeDriverPath + ", pageLoadTime=" + pageLoadTime + "]";
	}


	
}
