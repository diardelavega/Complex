package com.example.demo.scrap.data;

import java.util.List;

import com.example.demo.entity.Matches;

public interface MatchesGetter {

	public List<Matches> findCompetitionsMatches(int comp);

	public void storeCompetitionsMatches(int comp);

	public List<Matches> findCompetitionsMatchesWithOdds(int comp);

	public void storeCompetitionsMatchesWithOdds(int comp);

}
