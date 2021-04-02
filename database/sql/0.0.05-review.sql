create table dev.tb_review (
    review_seq bigint,
    restaurant bigint not null,
    review_author bigint not null,
    rate int not null,
    comment varchar(500),
    visit_date date not null,
    reply varchar(500),
    reply_date date,
    primary key(review_seq),
    constraint fk_restaurant foreign key(restaurant) references dev.tb_restaurant(restaurant_seq),
    constraint fk_author foreign key(review_author) references dev.tb_user(user_seq),
    constraint rate_range check (rate in (0,1,2,3,4,5))
);

create sequence dev.review_seqreview minvalue 1 start with 10 increment by 1;

comment on table dev.tb_review is 'Table with registred reviews';
comment on column dev.tb_review.review_seq is 'Sequence unique code for each review';
comment on column dev.tb_review.restaurant is 'Foreign key to the restaurant that received the review';
comment on column dev.tb_review.review_author is 'Foreign key to the user that made de review';
comment on column dev.tb_review.rate is 'Rate of the restaurant visit. Must be a value between 0 and 5';
comment on column dev.tb_review.comment is 'Review comment';
comment on column dev.tb_review.visit_date is 'Restaurant visit date';
comment on column dev.tb_review.reply is 'Restaurant reply to the review';
comment on column dev.tb_review.reply_date is 'Restaurant date of reply';

insert into dev.tb_review(
    review_seq,
    restaurant,
    review_author,
    rate,
    comment,
    visit_date,
    reply,
    reply_date
) values (1, 1, 2, 1, 'Comment 1', '2021-01-23', null, null);

insert into dev.tb_review(
    review_seq,
    restaurant,
    review_author,
    rate,
    comment,
    visit_date,
    reply,
    reply_date
) values (2, 1, 2, 3, 'Comment 3', '2021-01-22', null, null);

insert into dev.tb_review(
    review_seq,
    restaurant,
    review_author,
    rate,
    comment,
    visit_date,
    reply,
    reply_date
) values (3, 1, 2, 5, 'Comment 5', '2021-01-21', 'Reply 5', '2021-01-21');

insert into dev.tb_review(
    review_seq,
    restaurant,
    review_author,
    rate,
    comment,
    visit_date,
    reply,
    reply_date
) values (4, 2, 2, 1, 'Comment 7', '2021-01-22', null, null);
