package com.example.demo.mar.cod.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CompIdSiteLink {
	@Id
	private int compid;
	private String site1Link;
	private String site2Link;
	private String site3Link;

	public CompIdSiteLink() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CompIdSiteLink(int compid, String site1Link, String site2Link, String site3Link) {
		super();
		this.compid = compid;
		this.site1Link = site1Link;
		this.site2Link = site2Link;
		this.site3Link = site3Link;
	}

	public int getCompId() {
		return compid;
	}

	public void setCompId(int compid) {
		this.compid = compid;
	}

	public String getSite1Link() {
		return site1Link;
	}

	public void setSite1Link(String site1Link) {
		this.site1Link = site1Link;
	}

	public String getSite2Link() {
		return site2Link;
	}

	public void setSite2Link(String site2Link) {
		this.site2Link = site2Link;
	}

	public String getSite3Link() {
		return site3Link;
	}

	public void setSite3Link(String site3Link) {
		this.site3Link = site3Link;
	}

	@Override
	public String toString() {
		return "CompIdSiteLink [compid=" + compid + ", site1Link=" + site1Link + ", site2Link=" + site2Link
				+ ", site3Link=" + site3Link + "]";
	}

}
