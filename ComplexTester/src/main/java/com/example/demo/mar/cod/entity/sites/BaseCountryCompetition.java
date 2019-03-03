package com.example.demo.mar.cod.entity.sites;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class BaseCountryCompetition {
	@Id
	private Integer id;
	private String country;
	private String competition;
	private String link;
	private Short valid;
	private Short level;
	@Column(name="insert_dat")
	private LocalDateTime insDat;

	public BaseCountryCompetition() {
		super();
	}

	public BaseCountryCompetition(int id, String country, String competition, String link) {
		super();
		this.id = id;
		this.country = country;
		this.competition = competition;
		this.link = link;
	}

	public BaseCountryCompetition(int id, String country, String competition, String link, short valid, short level) {
		super();
		this.id = id;
		this.country = country;
		this.competition = competition;
		this.link = link;
		this.valid = valid;
		this.level = level;
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

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public boolean isValid() {
		if(valid==1)
			return true;
		else
			return false;
	}

	public void setValid(int i) {
		this.valid = (short) i;
	}

	public short getLevel() {
		return level;
	}

	public void setLevel(short level) {
		this.level = level;
	}

	
	public short getValid() {
		return valid;
	}

	public void setValid(short valid) {
		this.valid = valid;
	}

	public LocalDateTime getInsDat() {
		return insDat;
	}

	public void setInsDat(LocalDateTime insDat) {
		this.insDat = insDat;
	}

//	@Override
	public String toString() {
		return "BaseCountryCompetition [id=" + id + ", country=" + country + ", competition=" + competition + ", link="
				+ link + ", valid=" + valid + ", level=" + level + "]";
	}

}
