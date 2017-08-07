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

create table "Nodes"
(
	"id" varchar(255) primary key not null
	/*"name" varchar(255) not null default (''),
	"comment" varchar(4096) not null default (''),
	"isComment" boolean not null default (true),
	"progress" int not null default (0),
	"x" int not null default (0),
	"y" int not null default (0)*/
);

create table "KeyValueNodeData"
(
	"idData" SERIAL primary key not null,
	"idNode" varchar(255) not null REFERENCES "Nodes",
	"key" varchar(256) not null default (''),
	"value" varchar(4096) not null default ('')
);

create table "Links"
(
	"idLink" SERIAL primary key not null,
	"idNode" varchar(255) not null REFERENCES "Nodes",
	"idSubnode" varchar(255) not null REFERENCES "Nodes"
);