-- Database: "mindMap"

-- DROP DATABASE "mindMap";

/*CREATE DATABASE "mindMap"
  WITH OWNER = postgres
       ENCODING = 'UTF8'
       TABLESPACE = pg_default
       LC_COLLATE = 'Russian_Russia.1251'
       LC_CTYPE = 'Russian_Russia.1251'
       CONNECTION LIMIT = -1;

*/

todo: add foreigner keys

create table "Nodes"
(
	"id" SERIAL primary key not null,
	"name" varchar(255) not null default (''),
	"comment" varchar(255) not null default (''),
	"isComment" boolean not null default (true),
	"progress" int not null default (0),
	"x" int not null default (0),
	"y" int not null default (0),
	"nodeType" int not null default (0)
);

create table "Links"
(
	"idLink" SERIAL primary key not null,
	"node" int not null,
	"subnode" int not null,
	"linkType" int not null default (0)
);

--create table "Users"

--create table "Roots"
--(
--	"idUser" SERIAL primary key not null,
--	"node" int not null
--);