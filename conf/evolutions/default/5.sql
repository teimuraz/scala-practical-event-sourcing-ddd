# --- tracker_members schema

# --- !Ups

CREATE TABLE public.tracker_members
(
    id bigint NOT NULL,
    name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    email character varying(100) COLLATE pg_catalog."default" NOT NULL,
    role integer NOT NULL,
    organization_id bigint NOT NULL,
    became_member_at timestamp without time zone,
    CONSTRAINT tracker_members_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

CREATE INDEX tracker_members_name_idx
    ON public.tracker_members USING btree
    ("name" COLLATE pg_catalog."default")
    TABLESPACE pg_default;


CREATE INDEX tracker_members_email_idx
    ON public.tracker_members USING btree
    (email COLLATE pg_catalog."default")
    TABLESPACE pg_default;

CREATE INDEX tracker_members_organization_id_idx
    ON public.tracker_members USING btree
    (organization_id)
    TABLESPACE pg_default;

CREATE SEQUENCE public.tracker_members_seq
NO MINVALUE
NO MAXVALUE;


# --- !Downs

DROP SEQUENCE tracker_members_seq;

DROP TABLE IF EXISTS public.tracker_members CASCADE;


