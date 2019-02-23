package com.example.demo.mar.cod.entity.siteA;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CountryCompetition {

	@Id
	private int id;
	private String country;
	private String competition;
	private int levl;
	private String grup;
	private int teamsnr;

	public CountryCompetition() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CountryCompetition(int id, String country, String competition, int levl, String grup, int teamsNr) {
		super();
		this.id = id;
		this.country = country;
		this.competition = competition;
		this.levl = levl;
		this.grup = grup;
		this.teamsnr = teamsNr;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCompetition() {
		return competition;
	}

	public void setCompetition(String competition) {
		this.competition = competition;
	}

	public int getLevl() {
		return levl;
	}

	public void setLevl(int levl) {
		this.levl = levl;
	}

	public String getGrup() {
		return grup;
	}

	public void setGrup(String grup) {
		this.grup = grup;
	}

	public int getTeamsNr() {
		return teamsnr;
	}

	public void setTeamsNr(int teamsNr) {
		this.teamsnr = teamsNr;
	}

	@Override
	public String toString() {
		return "CountryCompetition [id=" + id + ",		 country=" + country + ", 		competition=" + competition + ", 	levl="
				+ levl + ",		 grup=" + grup + ", 	teamsNr=" + teamsnr + "]";
	}

}
