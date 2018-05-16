drop table if exists source;
drop table if exists person;
drop table if exists picture;
drop table if exists clip;
drop table if exists process;

create table source(
    uuid char(36) NOT NULL,
    file_name varchar(1000),
    url varchar(1000),
    name varchar(200),
    PRIMARY KEY (uuid));

create table person(
    uuid char(36) NOT NULL,
    name varchar(200),
    PRIMARY KEY (uuid));

create table picture(
    uuid char(36) NOT NULL,
    source char(36),
    PRIMARY KEY (uuid));

create table clip(
    uuid char(36) NOT NULL,
    source char(36),
    clip_length int,
    PRIMARY KEY (uuid));

create table process(
    uuid char(36) NOT NULL,
    command varchar(2000),
    isCommandLine bool,
    PRIMARY KEY (uuid));