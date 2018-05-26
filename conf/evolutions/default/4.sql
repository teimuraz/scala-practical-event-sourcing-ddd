# --- membership_organizations schema

# --- !Ups

CREATE TABLE public.membership_organizations
(
    id bigint NOT NULL,
    name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    owners_count integer NOT NULL,
    CONSTRAINT membership_organizations_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

CREATE SEQUENCE public.membership_organizations_seq
NO MINVALUE
NO MAXVALUE;


# --- !Downs

DROP SEQUENCE membership_organizations_seq;

DROP TABLE IF EXISTS public.membership_organizations CASCADE;


