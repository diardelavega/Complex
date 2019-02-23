
--DROP TABLE IF EXISTS  test.comp_id_site_link;
create table test.comp_id_site_link(
	compid SMALLINT primary key,
	site1Link varchar(200),
	site2Link varchar(200),
	site3Link varchar(200)
);



create table test.predict(
	match_id int primary key,
	ht_head char CHECK(ht_head in ('1','x','2') ),
	ft_head char CHECK(ht_head in ('1','x','2') ),

	ht_gg char CHECK(ft_over in ('y','n') ),
	ft_gg char CHECK(ft_over in ('y','n') ),

	ft_over char CHECK(ft_over in ('y','n') ),
  	ft_under char CHECK(ft_under in ('y','n') ),
  	ht_2plus char CHECK(ht_2plus in ('y','n') ),

  	ht_ft CHAR(2) CHECK( ht_ft in ('11', '1x', '12',    'x1', 'xx', 'x2',    '21', '2x', '22') ),

  	t1_ht_score TINYINT,
  	t2_ht_score TINYINT,
  	tot_ht_score TINYINT,

	t1_ft_score TINYINT,
  	t2_ft_score TINYINT,
  	tot_ft_score TINYINT,
);


CREATE table odds(
  match_id int primary key,

  ft1 numeric(6,3),
  ftx numeric(6,3),
  ft2 numeric(6,3),

  ft_over numeric(6,3),
  ft_under numeric(6,3),
  ht_2plus numeric(6,3)-- ht total score >=2 goals

);


CREATE table additional_odds(
  match_id int primary key,

  ht1 numeric(6,3),
  htx numeric(6,3),
  ht2 numeric(6,3),

  ht_Gg numeric(6,3),
  ft_Gg numeric(6,3),

  hft11 numeric(6,3),
  hft1x numeric(6,3),
  hft12 numeric(6,3),

  dshx2 numeric(6,3),
  dsh1x numeric(6,3),
  dsh12 numeric(6,3),

  hftx1 numeric(6,3),
  hftxx numeric(6,3),
  hftx2 numeric(6,3),

  hft21 numeric(6,3),
  hft2x numeric(6,3),
  hft22 numeric(6,3),
);
