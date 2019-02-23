package com.example.demo.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "match_halli")
public class Matches {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="match_Id")
	private long matchId;
	
	
	private String t1;
	private String t2;

	@Column(name="comp_Id")
	private Integer compId;
	@Column(name="tim")
	private LocalTime time;
	@Column(name="dat")
	private LocalDate date;
	
	@Column(name="t1_Ht_Score")
	private Integer t1HtScore;
	@Column(name="t2_Ht_Score")
	private Integer t2HtScore;
	@Column(name="t1_Ft_Score")
	private Integer t1FtScore;
	@Column(name="t2_Ft_Score")
	private Integer t2FtScore;
	
	@Column(name="t1_Et_Score")
	private Integer t1EtScore;
	@Column(name="t2_Et_Score")
	private Integer t2EtScore;
	@Column(name="t1_Pt_Score")
	private Integer t1PtScore;
	@Column(name="t2_Pt_Score")
	private Integer t2PtScore;
	
	@Column(name="t1_Yellow")
	private Integer t1Yellow;
	@Column(name="t2_Yellow")
	private Integer t2Yellow;
	@Column(name="t1_Red")
	private Integer t1Red;
	@Column(name="t2_Red")
	private Integer t2Red;

	@Column(name="week_Nr")
	private Integer weekNr;

	@Column(name="insert_dat")
	private LocalDateTime insDat;
	
//    private String timeOfDay; //morning, midday, afternoon, evening
	@Transient
	private Boolean neutral;
	@Transient
	private Integer t1FanPerc;
	@Transient
	private Integer t2FanPerc;

	public Matches() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Matches(long matchId, Integer compId, String t1, String t2, LocalTime time, LocalDate date, Integer t1HtScore,
			Integer t2HtScore, Integer t1FtScore, Integer t2FtScore, Integer weekNr, Boolean neutral, Integer t1FanPerc,
			Integer t2FanPerc) {
		super();
		this.matchId = matchId;
		this.compId = compId;
		this.t1 = t1;
		this.t2 = t2;
		this.time = time;
		this.date = date;
		this.t1HtScore = t1HtScore;
		this.t2HtScore = t2HtScore;
		this.t1FtScore = t1FtScore;
		this.t2FtScore = t2FtScore;
		this.weekNr = weekNr;
		this.neutral = neutral;
		this.t1FanPerc = t1FanPerc;
		this.t2FanPerc = t2FanPerc;
	}

	
	
	public Matches(long matchId, Integer compId, String t1, String t2, LocalTime time, LocalDate date,
			Integer t1HtScore, Integer t2HtScore, Integer t1FtScore, Integer t2FtScore, Integer t1EtScore,
			Integer t2EtScore, Integer t1PtScore, Integer t2PtScore, Integer weekNr, Boolean neutral, Integer t1FanPerc,
			Integer t2FanPerc) {
		super();
		this.matchId = matchId;
		this.compId = compId;
		this.t1 = t1;
		this.t2 = t2;
		this.time = time;
		this.date = date;
		this.t1HtScore = t1HtScore;
		this.t2HtScore = t2HtScore;
		this.t1FtScore = t1FtScore;
		this.t2FtScore = t2FtScore;
		this.t1EtScore = t1EtScore;
		this.t2EtScore = t2EtScore;
		this.t1PtScore = t1PtScore;
		this.t2PtScore = t2PtScore;
		this.weekNr = weekNr;
		this.neutral = neutral;
		this.t1FanPerc = t1FanPerc;
		this.t2FanPerc = t2FanPerc;
	}

	public long getMatchId() {
		return matchId;
	}

	public void setMatchId(long matchId) {
		this.matchId = matchId;
	}

	public Integer getCompId() {
		return compId;
	}

	public void setCompId(Integer compId) {
		this.compId = compId;
	}

	public String getT1() {
		return t1;
	}

	public void setT1(String t1) {
		this.t1 = t1;
	}

	public String getT2() {
		return t2;
	}

	public void setT2(String t2) {
		this.t2 = t2;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Integer getT1HtScore() {
		return t1HtScore;
	}

	public void setT1HtScore(Integer t1HtScore) {
		this.t1HtScore = t1HtScore;
	}

	public Integer getT2HtScore() {
		return t2HtScore;
	}

	public void setT2HtScore(Integer t2HtScore) {
		this.t2HtScore = t2HtScore;
	}

	public Integer getT1FtScore() {
		return t1FtScore;
	}

	public void setT1FtScore(Integer t1FtScore) {
		this.t1FtScore = t1FtScore;
	}

	public Integer getT2FtScore() {
		return t2FtScore;
	}

	public void setT2FtScore(Integer t2FtScore) {
		this.t2FtScore = t2FtScore;
	}

	public Integer getWeekNr() {
		return weekNr;
	}

	public void setWeekNr(Integer weekNr) {
		this.weekNr = weekNr;
	}

	public Boolean getNeutral() {
		return neutral;
	}

	public void setNeutral(Boolean neutral) {
		this.neutral = neutral;
	}

	public Integer getT1FanPerc() {
		return t1FanPerc;
	}

	public void setT1FanPerc(Integer t1FanPerc) {
		this.t1FanPerc = t1FanPerc;
	}

	public Integer getT2FanPerc() {
		return t2FanPerc;
	}

	public void setT2FanPerc(Integer t2FanPerc) {
		this.t2FanPerc = t2FanPerc;
	}

	public Integer getT1EtScore() {
		return t1EtScore;
	}

	public void setT1EtScore(Integer t1EtScore) {
		this.t1EtScore = t1EtScore;
	}

	public Integer getT2EtScore() {
		return t2EtScore;
	}

	public void setT2EtScore(Integer t2EtScore) {
		this.t2EtScore = t2EtScore;
	}

	public Integer getT1PtScore() {
		return t1PtScore;
	}

	public void setT1PtScore(Integer t1PtScore) {
		this.t1PtScore = t1PtScore;
	}

	public Integer getT2PtScore() {
		return t2PtScore;
	}

	public void setT2PtScore(Integer t2PtScore) {
		this.t2PtScore = t2PtScore;
	}

	public Integer getT1Yellow() {
		return t1Yellow;
	}

	public void setT1Yellow(Integer t1Yellow) {
		this.t1Yellow = t1Yellow;
	}

	public Integer getT2Yellow() {
		return t2Yellow;
	}

	public void setT2Yellow(Integer t2Yellow) {
		this.t2Yellow = t2Yellow;
	}

	public Integer getT1Red() {
		return t1Red;
	}

	public void setT1Red(Integer t1Red) {
		this.t1Red = t1Red;
	}

	public Integer getT2Red() {
		return t2Red;
	}

	public void setT2Red(Integer t2Red) {
		this.t2Red = t2Red;
	}

	public LocalDateTime getInsDat() {
		return insDat;
	}

	public void setInsDat(LocalDateTime insDat) {
		this.insDat = insDat;
	}
	
	

	
}
