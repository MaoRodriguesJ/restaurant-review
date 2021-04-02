create or replace view dev.vw_restaurant_list as
select
    restaurant.restaurant_seq,
    restaurant.restaurant_owner,
    restaurant.restaurant_name,
    restaurant.restaurant_description,
    restaurant.restaurant_address,
    restaurant.restaurant_latitude,
    restaurant.restaurant_longitude,
    food.type_of_food_description,
    coalesce(avg(review.rate), 0) AS average_rate,
    sum(case when review is null then 0 when review.reply is null then 1 else 0 end) as pending_replies
from dev.tb_restaurant restaurant
inner join dev.tb_type_of_food food on restaurant.restaurant_type_of_food = food.type_of_food_seq
left join dev.tb_review review on review.restaurant = restaurant.restaurant_seq
group by
    restaurant.restaurant_seq,
    restaurant.restaurant_name,
    restaurant.restaurant_owner,
    restaurant.restaurant_description,
    restaurant.restaurant_address,
    restaurant.restaurant_latitude,
    restaurant.restaurant_longitude,
    food.type_of_food_seq;

comment on view dev.vw_restaurant_list is 'View to easy query restaraunt list with average rate information'
