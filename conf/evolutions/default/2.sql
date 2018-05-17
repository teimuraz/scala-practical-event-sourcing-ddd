# --- aggregate_versions

# --- !Ups

CREATE TABLE public.aggregate_versions (
	aggregate_root_type int4 NOT NULL,
	aggregate_root_id bigint NOT NULL,
	current_version int4 NOT NULL,
	CONSTRAINT aggregate_versions_pk PRIMARY KEY (aggregate_root_type,aggregate_root_id)
)
WITH (
	OIDS=FALSE
) ;

# --- !Downs

DROP TABLE IF EXISTS public.aggregate_versions CASCADE;
