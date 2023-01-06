

select d.id from demographics d
where exists (
              select * from demographics x
              where d.area = x.area AND d.city = x.city AND max(x.year) = d.year
              group by x.area, x.city
          );

drop table reduce_demos;


create table reduce_demos as
SELECT t1.id
FROM demographics t1
WHERE t1.id = (SELECT t2.id
               FROM demographics t2
               WHERE t2.area = t1.area
                 AND t2.city = t1.city
               ORDER BY t2.year DESC
    LIMIT 1);

delete from demographics where id not in (select id from reduce_demos);



select area, city, count(*) from demographics
group by area, city;

select count(*) from cities c, demographics d
where upper(c.city) = upper(d.city)
  and upper(c.country) = upper(d.area);


select count(*) from cities where cities.population is not null
union
select count(*) from cities where cities.population is null;


select country, count(*) from cities where country not in(
    select area from demographics
)
group by country order by count(*) desc;


create table tmp_area_counts as
select country, 'UNK' as state, count(*) from cities where population is null group by country
union
select country, 'KNW' as state, count(*) from cities where population is not null group by country
order by country, state desc;

select * from tmp_area_counts where country not in (select country from tmp_area_counts where state = 'KNW');

select * from demo_backups where city like 'Rio%';


select 'UNK', count(*) from cities where population is null
union
select 'KNW', count(*) from cities where population is not null;



select min(population), max(population) from cities where population is not null;

select country, city, population from cities order by population desc limit 20;


Brazil
British Caribbean Territories
Curacao and Sint Maarten
Czech Republic
Cyprus
Denmark
Jamaica
Japan
INSERT INTO MY_TABLE(country) VALUES ('Jordan');
INSERT INTO MY_TABLE(country) VALUES ('Kazakhstan');
Lebanon
Luxembourg
Malawi
Maldives
Malta
Montenegro
Morocco
Panama
Peru
Republic of Korea
Samoa
Seychelles
Sierra Leone
Singapore
Solomon Islands
Syrian Arab Republic
Trinidad and Tobago
United Arab Emirates










select c.country, c.city, d.city, d.area
from cities c, demographics d
where SUBSTRING_INDEX(upper(c.city), ',', 1) = upper(d.city)
  AND upper(c.country) = upper(d.area);

update cities c, demographics d
set c.population = d.value
where c.population is null
  and SUBSTRING_INDEX(SUBSTRING_INDEX(upper(c.city), ',', 1), '-', 1) = SUBSTRING_INDEX(upper(d.city), ' (', 1)
  AND upper(c.country) = upper(d.area);

select c.country, c.city, 'c'
from cities c
where c.country = 'United States of America';





select city, population from cities where country = 'Australia' order by city; # and population is null;

select * from demographics where city like 'Sant%';
select * from demographics where area like 'United States%' and city like 'Nash%';
select * from demographics where area like 'Guam';


select c.country, c.city
from cities c, demographics d
where upper(c.city) = upper(d.city)
  AND upper(c.country) = upper(d.area);


select * from demo_backups where city = 'Melbourne';



insert into demographics select * from demo_backups where demo_backups.id not in (select id from demographics);


select count(*) from reduce_demos;


delete from reduce_demos;

insert into reduce_demos
Select id
from demographics t
         inner join
     (SELECT area, city, MAX(year) as max_year, MAX(value) as max_value
      FROM demographics
      GROUP BY area, city) a
     on a.area = t.area and a.city = t.city and a.max_year = t.year and a.max_value = value
order by t.area, t.city;

select * from demographics t
                  inner join (
    select area, city from demographics group by area, city having count(*) > 1) a
                             on t.area = a.area and t.city = a.city;


select type, record_type, count(*) from demographics t
                                            inner join (
    select area, city from demographics group by area, city having count(*) = 1) a
                                                       on t.area = a.area and t.city = a.city group by type, record_type;


select * from demographics where record_type = 'Sample survey - de facto';


select count(*) from demo_backups b
                         left join demo_backups db on b.area = db.area and b.city = db.city and b.year = db.year
where db.city is null;



select * from cities where country like '%elgiu%' order by city;


select SUBSTRING_INDEX(upper(city), ' (', 1), city, area, value from demographics where area like '%elgiu%';


UPDATE
    cities c,
    demographics d
SET
    c.population = d.value
where upper(c.city) = upper(d.city)
  and upper(c.country) = upper(d.area);

SELECT
    area, SUBSTRING_INDEX(city, ' (', 1) AS substring
FROM demographics;


UPDATE
    cities c
SET
    c.country = 'Venezuela (Bolivarian Republic of)'
where c.country like 'Venezuela%';


insert into demographics
select * from demo_backups;

delete from demographics where type = 'Urban agglomeration';

select count(*) from demographics
group by area, city;




delete from demographics;


select * from demographics;

create table demo_backups as select * from demographics;

select id, population from cities where population is not null;

