create user salomao with encrypted password 'dev';
alter user salomao with superuser;
create schema if not exists dev authorization salomao;