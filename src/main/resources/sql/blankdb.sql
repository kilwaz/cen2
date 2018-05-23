drop table if exists encoded_progress;
drop table if exists source_info;
drop table if exists source;
drop table if exists person;
drop table if exists picture;
drop table if exists clip;
drop table if exists process;

drop table if exists flyway_testing_migrate;
drop table if exists flyway_schema_history;

create table source(
    uuid char(36) NOT NULL,
    file_name varchar(1000),
    encoded_file_name varchar(1000),
    url varchar(1000),
    encoded_url varchar(1000),
    name varchar(200),
    encoded_progress_id char(36),
    source_info_json varchar(8000)
    CHECK (source_info_json IS NULL OR JSON_VALID(source_info_json)),
    PRIMARY KEY (uuid));

create table encoded_progress(
    uuid char(36) NOT NULL,
    total_frames int,
    pass_phase int,
    pass_1_progress int,
    pass_1_file_name varchar(1000),
    pass_2_progress int,
    pass_2_file_name varchar(1000),
    PRIMARY KEY (uuid));

create table person(
    uuid char(36) NOT NULL,
    name varchar(200),
    PRIMARY KEY (uuid));

create table process(
    uuid char(36) NOT NULL,
    command varchar(2000),
    is_command_line bool,
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