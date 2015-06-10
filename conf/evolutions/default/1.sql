# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table answer (
  answer_id                 integer not null,
  create_date               timestamp,
  user_name                 varchar(255),
  interview_interview_id    integer,
  constraint pk_answer primary key (answer_id))
;

create table check_variant (
  check_variant_id          integer not null,
  answer_answer_id          integer,
  user_var                  varchar(255),
  parent_question_question_id integer,
  variant_variant_id        integer,
  constraint pk_check_variant primary key (check_variant_id))
;

create table interview (
  interview_id              integer not null,
  interview_title           varchar(255),
  req_user_name             boolean,
  interview_text            VARCHAR(4095),
  interview_owner_user_id   integer,
  create_date               timestamp,
  constraint pk_interview primary key (interview_id))
;

create table question (
  question_id               integer not null,
  question_title            varchar(255),
  required                  boolean,
  many_variants             boolean,
  user_variant              boolean,
  question_parent_interview_id integer,
  constraint pk_question primary key (question_id))
;

create table user (
  user_id                   integer not null,
  user_login                varchar(255),
  user_first_name           varchar(255),
  user_last_name            varchar(255),
  reg_date                  timestamp,
  is_admin                  boolean,
  user_pass                 varchar(255),
  constraint uq_user_user_login unique (user_login),
  constraint pk_user primary key (user_id))
;

create table variant (
  variant_id                integer not null,
  variant                   varchar(255),
  variant_parent_question_id integer,
  constraint pk_variant primary key (variant_id))
;

create sequence answer_seq;

create sequence check_variant_seq;

create sequence interview_seq;

create sequence question_seq;

create sequence user_seq;

create sequence variant_seq;

alter table answer add constraint fk_answer_interview_1 foreign key (interview_interview_id) references interview (interview_id) on delete restrict on update restrict;
create index ix_answer_interview_1 on answer (interview_interview_id);
alter table check_variant add constraint fk_check_variant_answer_2 foreign key (answer_answer_id) references answer (answer_id) on delete restrict on update restrict;
create index ix_check_variant_answer_2 on check_variant (answer_answer_id);
alter table check_variant add constraint fk_check_variant_parentQuestio_3 foreign key (parent_question_question_id) references question (question_id) on delete restrict on update restrict;
create index ix_check_variant_parentQuestio_3 on check_variant (parent_question_question_id);
alter table check_variant add constraint fk_check_variant_variant_4 foreign key (variant_variant_id) references variant (variant_id) on delete restrict on update restrict;
create index ix_check_variant_variant_4 on check_variant (variant_variant_id);
alter table interview add constraint fk_interview_interviewOwner_5 foreign key (interview_owner_user_id) references user (user_id) on delete restrict on update restrict;
create index ix_interview_interviewOwner_5 on interview (interview_owner_user_id);
alter table question add constraint fk_question_questionParent_6 foreign key (question_parent_interview_id) references interview (interview_id) on delete restrict on update restrict;
create index ix_question_questionParent_6 on question (question_parent_interview_id);
alter table variant add constraint fk_variant_variantParent_7 foreign key (variant_parent_question_id) references question (question_id) on delete restrict on update restrict;
create index ix_variant_variantParent_7 on variant (variant_parent_question_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists answer;

drop table if exists check_variant;

drop table if exists interview;

drop table if exists question;

drop table if exists user;

drop table if exists variant;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists answer_seq;

drop sequence if exists check_variant_seq;

drop sequence if exists interview_seq;

drop sequence if exists question_seq;

drop sequence if exists user_seq;

drop sequence if exists variant_seq;

