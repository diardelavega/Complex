package com.example.demo.before.jobs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.example.demo.mar.cod.entity.sites.ASiteCountryCompetition;
import com.example.demo.mar.cod.entity.sites.ASiteRepo;


@Component
public class StructLoader {

	@Autowired
	ASiteRepo asrepo;

	private ASiteCountryCompetition[] aSiteCountryCompStruct;

	public void init() {
	}

	public void aSiteCountryCompLoad() {
//		ASiteCountryCompetition [] aSiteCountryCompStruct = new ASiteCountryCompetition[asrepo.findAll().size()] {(ASiteCountryCompetition) asrepo.findAll();};
		List<ASiteCountryCompetition> temp = asrepo.findAll();
		aSiteCountryCompStruct = new ASiteCountryCompetition[temp.size()];
		aSiteCountryCompStruct = temp.toArray(aSiteCountryCompStruct);
	}

	public void bSiteCountryCompLoad() {

	}

	public void cSiteCountryCompLoad() {

	}

	public ASiteCountryCompetition[] getaSiteCountryCompStruct() {
		return aSiteCountryCompStruct;
	}

	public void setaSiteCountryCompStruct(ASiteCountryCompetition[] aSiteCountryCompStruct) {
		this.aSiteCountryCompStruct = aSiteCountryCompStruct;
	}
	
	
}
