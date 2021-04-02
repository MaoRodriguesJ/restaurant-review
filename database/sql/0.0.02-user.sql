create table dev.tb_user (
    user_seq bigint,
    username varchar(250) not null,
    password_hash varchar(250) not null,
    display_name varchar(250) not null,
    reset_token varchar(128),
    token_expiration_date timestamp,
    user_enabled boolean,
    primary key (user_seq),
    unique(username)
);

create sequence dev.user_sequser minvalue 1 start with 10 increment by 1;

comment on table dev.tb_user is 'Table with registred users';
comment on column dev.tb_user.user_seq is 'Sequence unique code for each user';
comment on column dev.tb_user.username is 'User email';
comment on column dev.tb_user.password_hash is 'User password';
comment on column dev.tb_user.display_name is 'User name';
comment on column dev.tb_user.reset_token is 'Token used to reset the password';
comment on column dev.tb_user.user_enabled is 'Indicates if the user is enabled';
comment on column dev.tb_user.token_expiration_date is 'Expiration date for the token generated to reset the password';

insert into dev.tb_user(user_seq, username, password_hash, display_name, reset_token, token_expiration_date, user_enabled)
values (1, 'admin@email.com', '$2a$10$0ZxJTJZDbTE0z8LB1CHsleqiguw4ON8m1ZB0qYMFEshsAN/dsUHay', 'admin', null, null, 'true');

insert into dev.tb_user(user_seq, username, password_hash, display_name, reset_token, token_expiration_date, user_enabled)
values (2, 'user@email.com', '$2a$10$0ZxJTJZDbTE0z8LB1CHsleqiguw4ON8m1ZB0qYMFEshsAN/dsUHay', 'user', null, null, 'true');

insert into dev.tb_user(user_seq, username, password_hash, display_name, reset_token, token_expiration_date, user_enabled)
values (3, 'owner@email.com', '$2a$10$0ZxJTJZDbTE0z8LB1CHsleqiguw4ON8m1ZB0qYMFEshsAN/dsUHay', 'owner', null, null, 'true');

insert into dev.tb_user(user_seq, username, password_hash, display_name, reset_token, token_expiration_date, user_enabled)
values (4, 'owner2@email.com', '$2a$10$0ZxJTJZDbTE0z8LB1CHsleqiguw4ON8m1ZB0qYMFEshsAN/dsUHay', 'owner2', null, null, 'true');

create table dev.tb_role (
    role_id bigint,
    role_name varchar(100) not null,
    primary key (role_id)
);

comment on table dev.tb_role is 'Table with user roles';
comment on column dev.tb_role.role_id is 'Role identifier';
comment on column dev.tb_role.role_name is 'Role name';

insert into dev.tb_role(role_id, role_name) values (1, 'ROLE_ADMIN');
insert into dev.tb_role(role_id, role_name) values (2, 'ROLE_USER');
insert into dev.tb_role(role_id, role_name) values (3, 'ROLE_OWNER');

create table dev.rl_user_role (
    user_seq bigint references dev.tb_user (user_seq),
    role_id bigint references dev.tb_role (role_id),
    primary key (user_seq, role_id)
);

comment on table dev.rl_user_role is 'Table with N:N relation between users and roles';

insert into dev.rl_user_role (user_seq, role_id) values (1, 1);
insert into dev.rl_user_role (user_seq, role_id) values (2, 2);
insert into dev.rl_user_role (user_seq, role_id) values (3, 3);
insert into dev.rl_user_role (user_seq, role_id) values (4, 3);
