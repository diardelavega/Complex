package com.example.demo.mar.cod.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompIdSiteLinlRepo extends JpaRepository<CompIdSiteLink, Integer> {

}
