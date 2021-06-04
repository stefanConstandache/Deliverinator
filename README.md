# Deliverinator

**Description**
  - A team project made for Program Engineering class.
  - The project is a food delivery app made in Android Studio using Kotlin language.
  - We used Firebase as the database and cloud storage.
  - The interface is simple and intuitive:
    - Login activity with email and password fields.
    - Register activity with the basic email, password and phone fields.
    - After registration the user must verify his account using the link which was sent to his email address.
  - There are 3 types of users:
    - Admin
    - Restaurant
    - Client
  - An admin account is predefined and cannout be made using the register activity.
  - The admin user can create restaurant account or one.
  - Admin dashboard has 2 fragments:
    - Create restaurant fragment
      - Here are some field which must be filled by admin so the restaurant account can be made.
    - Delete restaurant fragment
      - Here a list of restaurants is displayed with a delete button for each element.
  - Client dashboard has 2 fragments:
    - Account fragment, where the client can change his password, name or address.
    - Restaurants fragment, where the client can chose from which restaurant to order.
      - By pressing on a restaurant a new activity is started and the list with the menu items are displayed.
      - Here the client can chose the quantity of each product he wants to buy.
      - By pressing the button "Finish order" it starts the shopping cart activity where the items
      the client wants to buy are displayed.
      - Before confirming the order the client must enter the address where the items must be delivered.
      - After confirming, a notification that the order was sent is received by the client.
      The restaurant user will receive a notification that it has new orders.
  - Restaurant dashboard has 3 fragments:
    - Account fragment, where he can change the image of the restaurant, the description and phone number.
    - Menu fragment, where he can add or delete items that are displayed on the client's screen.
    - Order fragment, where he can see the orders that must be prepared for the clients.
      - When an order is finished he can delete it from the list.
      
## Screenshots

![Imgur](https://i.imgur.com/5BJohW2.jpg =250X250)
