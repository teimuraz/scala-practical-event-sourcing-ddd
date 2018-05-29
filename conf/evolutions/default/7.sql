# --- tracker_members schema

# --- !Ups

CREATE TABLE public.tracker_issue_assignees (
	issue_id bigint NOT NULL,
	member_id bigint NOT NULL,
	CONSTRAINT tracker_issue_assignees_pk PRIMARY KEY (issue_id,member_id)
)
WITH (
	OIDS=FALSE
) ;
CREATE INDEX tracker_issue_assignees_member_id_idx ON public.tracker_issue_assignees (member_id) ;

# --- !Downs

DROP TABLE IF EXISTS public.tracker_issue_assignees CASCADE;


