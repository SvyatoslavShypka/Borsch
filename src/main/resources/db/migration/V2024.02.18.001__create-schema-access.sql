create schema if not exists access;

create table access.gender(
                              gender_id    numeric(1)    primary key,
                              name_ukr     varchar(50)   not null,
                              name_eng     varchar(50)   not null
);

create unique index ix_gender_gender_id on access.gender (gender_id);

create table access.users(
                             user_id        uuid          primary key,
                             username       varchar(100)   not null,
                             password       varchar(500)  not null,
                             role           varchar(32)   not NULL,
                             enabled        boolean       not null,
                             nickname       varchar(50)  not null,
                             birthday       date,
                             gender_id      integer       not null default 0,
                             created_date   timestamp,
                             updated_date   timestamp,
                             friendgroup_id UUID            nullable,
                             constraint fk_users_gender foreign key(gender_id) references access.gender(gender_id)
);
create unique index ix_users_user_id   on access.users (user_id);
create unique index ix_users_username  on access.users (username);
create        index ix_users_gender_id on access.users (gender_id);
