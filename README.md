# restaurant-review
This is a project for a challenge in a hiring process. It was made within two weeks (around 60-70hrs of work) following this main requirements:

- User must be able to create an account and log in.
    - Regular User: Can rate and leave a comment for a restaurant
    - Owner: Can create restaurants and reply to comments about owned restaurants
    - Admin: Can edit/delete all users, restaurants, comments, and reviews
- Reviews should have:
    - A 5 star based rating
    - Date of the visit
    - Comment 
- When a Regular User logs in, they will see a Restaurant List ordered by Average Rating
- When an Owner logs in, they will see a Restaurant List - only the ones owned by them, and the reviews pending reply
- Owners can reply to each review once
- Restaurants detailed view should have:
    - The overall average rating
    - The highest rated review
    - The lowest rated review
    - Last reviews with rate, comment, and reply
- Restaurant List can be filtered by Rating
- REST API. Make it possible to perform all user actions via the API, including authentication (If a mobile application and you don’t know how to create your own backend you can use Firebase.com or similar services to create the API).
- Functional UI/UX design is needed. You are not required to create a unique design, however, do follow best practices to make the project as functional as possible.

A running version can be found in: https://toptal-restaurant-review.herokuapp.com

The e-mail functions doesn't work (e.g. forgot password) because I don't own a e-mail server.

Maybe you will find issues regards running this application locally due to frontend version o the star component used (since node-sass was deprecated). I did not had time to review it yet.

