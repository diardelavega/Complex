package com.example.demo.mar.cod.entity.sites;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CSiteRepo extends JpaRepository<CSiteCountryCompetition, Integer> {

}
