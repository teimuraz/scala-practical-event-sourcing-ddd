# --- membership_members schema

# --- !Ups

CREATE TABLE public.membership_members
(
    id bigint NOT NULL,
    name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    email character varying(100) COLLATE pg_catalog."default" NOT NULL,
    role integer NOT NULL,
    became_member_at timestamp without time zone,
    CONSTRAINT pending_users_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

CREATE INDEX membership_members_name_idx
    ON public.membership_members USING btree
    ("name" COLLATE pg_catalog."default")
    TABLESPACE pg_default;


CREATE INDEX membership_members_email_idx
    ON public.membership_members USING btree
    (email COLLATE pg_catalog."default")
    TABLESPACE pg_default;


# --- !Downs

DROP TABLE IF EXISTS public.membership_members CASCADE;


