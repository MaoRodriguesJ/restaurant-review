create table dev.tb_type_of_food (
    type_of_food_seq bigint,
    type_of_food_description varchar(250) not null,
    primary key(type_of_food_seq)
);

create sequence dev.typeoffood_seqtypeoffood minvalue 1 start with 1 increment by 1;

comment on table dev.tb_type_of_food is 'Table with types of food domain';
comment on column dev.tb_type_of_food.type_of_food_seq is 'Sequence unique code for each type of food';
comment on column dev.tb_type_of_food.type_of_food_description is 'Type of food description';

insert into dev.tb_type_of_food(type_of_food_seq, type_of_food_description)
values (nextval('dev.typeoffood_seqtypeoffood'), 'Fast food');

insert into dev.tb_type_of_food(type_of_food_seq, type_of_food_description)
values (nextval('dev.typeoffood_seqtypeoffood'), 'Thai');

insert into dev.tb_type_of_food(type_of_food_seq, type_of_food_description)
values (nextval('dev.typeoffood_seqtypeoffood'), 'Chinese');

insert into dev.tb_type_of_food(type_of_food_seq, type_of_food_description)
values (nextval('dev.typeoffood_seqtypeoffood'), 'Pizza');

insert into dev.tb_type_of_food(type_of_food_seq, type_of_food_description)
values (nextval('dev.typeoffood_seqtypeoffood'), 'Hamburger');

insert into dev.tb_type_of_food(type_of_food_seq, type_of_food_description)
values (nextval('dev.typeoffood_seqtypeoffood'), 'Japanese');

insert into dev.tb_type_of_food(type_of_food_seq, type_of_food_description)
values (nextval('dev.typeoffood_seqtypeoffood'), 'Brazilian');

insert into dev.tb_type_of_food(type_of_food_seq, type_of_food_description)
values (nextval('dev.typeoffood_seqtypeoffood'), 'Italian');

insert into dev.tb_type_of_food(type_of_food_seq, type_of_food_description)
values (nextval('dev.typeoffood_seqtypeoffood'), 'Seafood');

insert into dev.tb_type_of_food(type_of_food_seq, type_of_food_description)
values (nextval('dev.typeoffood_seqtypeoffood'), 'Desert');
