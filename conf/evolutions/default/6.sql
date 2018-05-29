# --- tracker_issues schema

# --- !Ups

CREATE TABLE public.tracker_issues
(
    id bigint NOT NULL,
    title character varying(255) COLLATE pg_catalog."default" NOT NULL,
    description text  COLLATE pg_catalog."default" NOT NULL,
    status smallint NOT NULL,
    created_by bigint NOT NULL,
    created_at timestamp without time zone,
    CONSTRAINT tracker_issues_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

CREATE INDEX tracker_issues_title_idx
    ON public.tracker_issues USING btree
    ("title" COLLATE pg_catalog."default")
    TABLESPACE pg_default;

CREATE INDEX tracker_issues_status_idx
    ON public.tracker_issues USING btree
    (status)
    TABLESPACE pg_default;

CREATE INDEX tracker_issues_created_by_idx
    ON public.tracker_issues USING btree
    (created_by)
    TABLESPACE pg_default;

CREATE SEQUENCE public.tracker_issues_seq
NO MINVALUE
NO MAXVALUE;


# --- !Downs

DROP SEQUENCE tracker_issues_seq;

DROP TABLE IF EXISTS public.tracker_issues CASCADE;


