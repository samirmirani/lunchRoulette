--
-- PostgreSQL database dump
--

# --- !Ups
SET default_transaction_read_only = off;

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;


COMMENT ON DATABASE lunch IS 'lunch roullete database';


CREATE SCHEMA users;


ALTER SCHEMA users OWNER TO app;


CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;




COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = users, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;



CREATE TABLE pairs (
    id integer NOT NULL,
    userida integer NOT NULL,
    useridb integer NOT NULL
);


ALTER TABLE users.pairs OWNER TO app;


COMMENT ON TABLE pairs IS 'pairs of users.';


CREATE SEQUENCE pairs_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE users.pairs_id_seq OWNER TO app;


ALTER SEQUENCE pairs_id_seq OWNED BY pairs.id;



CREATE TABLE users (
    id integer NOT NULL,
    name character(128),
    email character varying(256),
    active boolean DEFAULT false
);


ALTER TABLE users.users OWNER TO app;



CREATE SEQUENCE users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE users.users_id_seq OWNER TO app;


ALTER SEQUENCE users_id_seq OWNED BY users.id;


ALTER TABLE ONLY pairs ALTER COLUMN id SET DEFAULT nextval('pairs_id_seq'::regclass);


ALTER TABLE ONLY users ALTER COLUMN id SET DEFAULT nextval('users_id_seq'::regclass);


ALTER TABLE ONLY pairs
    ADD CONSTRAINT pairs_pkey PRIMARY KEY (id);



ALTER TABLE ONLY users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--


# --- !Downs
