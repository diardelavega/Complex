package com.example.demo.mar.cod.entity.sites;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "C_site_country_competition")
public class CSiteCountryCompetition extends BaseCountryCompetition {
	@Column(name="grup", length=5)
	String grup;
	
	

	public CSiteCountryCompetition() {
		super();
	}

	public CSiteCountryCompetition(int id, String country, String competition, String link) {
		super(id, country, competition, link);
	}

	
	public CSiteCountryCompetition(String grup) {
		super();
		this.grup = grup;
	}

	public String getGrup() {
		return grup;
	}

	public void setGrup(String grup) {
		this.grup = grup;
	}

	@Override
	public String toString() {
		return "CSiteCountryCompetition [grup=" + grup + "] "+ super.toString();
	}

	
}
