create table dev.tb_restaurant (
    restaurant_seq bigint,
    restaurant_owner bigint not null,
    restaurant_name varchar(250) not null,
    restaurant_description varchar(500) not null,
    restaurant_type_of_food bigint not null,
    restaurant_address varchar(500) not null,
    restaurant_latitude float not null,
    restaurant_longitude float not null,
    primary key(restaurant_seq),
    constraint fk_owner foreign key(restaurant_owner) references dev.tb_user(user_seq)
);

create sequence dev.restaurant_seqrestaurant minvalue 1 start with 10 increment by 1;

comment on table dev.tb_restaurant is 'Table with registred restaurants';
comment on column dev.tb_restaurant.restaurant_seq is 'Sequence unique code for each restaurant';
comment on column dev.tb_restaurant.restaurant_owner is 'Foreign key to the user that owns the restaurant';
comment on column dev.tb_restaurant.restaurant_name is 'Restaurant email';
comment on column dev.tb_restaurant.restaurant_description is 'Restaurant description';
comment on column dev.tb_restaurant.restaurant_type_of_food is 'Foreign key to the type of food';
comment on column dev.tb_restaurant.restaurant_address is 'Restaurant address';
comment on column dev.tb_restaurant.restaurant_latitude is 'Restaurant latitude';
comment on column dev.tb_restaurant.restaurant_longitude is 'Restaurant longitude';
