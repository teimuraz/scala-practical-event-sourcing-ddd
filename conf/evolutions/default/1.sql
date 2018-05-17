# --- events_journal

# --- !Ups

CREATE TABLE public.events_journal (
  event_offset bigserial NOT NULL,
  aggregate_root_type int4 NOT NULL,
  aggregate_root_id int8 NOT NULL,
  event_type varchar(255) NOT NULL,
  event jsonb NOT NULL,
  aggregate_version int4 NOT NULL,
  created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT events_journal_pk PRIMARY KEY (event_offset)
)
WITH (
OIDS=FALSE
) TABLESPACE pg_default;;
CREATE INDEX events_journal_aggregate_root_type_idx ON public.events_journal USING btree (aggregate_root_type, aggregate_root_id) ;
CREATE INDEX events_journal_event_offset_idx ON public.events_journal USING btree (event_offset, aggregate_root_type) ;

# --- !Downs

DROP TABLE IF EXISTS public.events_journal CASCADE;


