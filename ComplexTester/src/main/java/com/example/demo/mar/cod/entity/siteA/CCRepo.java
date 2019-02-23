package com.example.demo.mar.cod.entity.siteA;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CCRepo extends JpaRepository<CountryCompetition, Integer> {

}
