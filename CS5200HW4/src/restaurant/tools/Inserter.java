package restaurant.tools;

import restaurant.dao.*;
import restaurant.model.*;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;


/**
 * main() runner, used for the app demo.
 * 
 * Instructions:
 * 1. Create a new MySQL schema and then run the CREATE TABLE statements from lecture:
 * http://goo.gl/86a11H.
 * 2. Update ConnectionManager with the correct user, password, and schema.
 */
public class Inserter {

	public static void main(String[] args) throws SQLException {
		// DAO instances.
		UsersDao usersDao = UsersDao.getInstance();
		CreditCardsDao creditCardsDao = CreditCardsDao.getInstance();
		CompaniesDao companiesDao = CompaniesDao.getInstance();
		RestaurantsDao restaurantsDao = RestaurantsDao.getInstance();
		SitDownRestaurantsDao sitDownRestaurantsDao = SitDownRestaurantsDao.getInstance();
		TakeOutRestaurantsDao takeOutRestaurantsDao = TakeOutRestaurantsDao.getInstance();
		FoodCartRestaurantsDao foodCartRestaurantsDao = FoodCartRestaurantsDao.getInstance();
		ReviewsDao reviewsDao = ReviewsDao.getInstance();
		RecommendationsDao recommendationsDao = RecommendationsDao.getInstance();
		ReservationsDao reservationsDao = ReservationsDao.getInstance();

		// INSERT objects from our model.
		Users user1 = new Users("Bruce", "password", "Bruce", "C",
						"bruce@mail.com", "5555555555");
		Users user2 = new Users("TT", "password", "Tony", "D",
						"tony@mail.com", "5555555555");
		Users user3 = new Users("DK", "password", "Daniel", "K",
						"dan@mail.com", "5555555555");
		Users user4 = new Users("James", "password", "James", "M",
						"james@mail.com", "5555555555");
		Users user5 = new Users("Steve", "password", "Steve", "N",
						"steve@mail.com", "5555555555");
		usersDao.create(user1);
		usersDao.create(user2);
		usersDao.create(user3);
		usersDao.create(user4);
		usersDao.create(user5);

		Companies company1 = new Companies("company1", "about company1");
		Companies company2 = new Companies("company2", "about company2");
		Companies company3 = new Companies("company3", "about company3");
		companiesDao.create(company1);
		companiesDao.create(company2);
		companiesDao.create(company3);

		SitDownRestaurants restaurant1 = new SitDownRestaurants("restaurant1", "about restaurant",
						"menu", "hours", true, Restaurants.CuisineType.AFRICAN,
						"street1", "street2", "seattle", "wa", 98195, company1, 100);
		SitDownRestaurants restaurant2 = new SitDownRestaurants("restaurant2", "about restaurant",
						"menu", "hours", true, Restaurants.CuisineType.AMERICAN,
						"street1", "street2", "seattle", "wa", 98195, company1, 200);
		SitDownRestaurants restaurant3 = new SitDownRestaurants("restaurant3", "about restaurant",
						"menu", "hours", true, Restaurants.CuisineType.ASIAN,
						"street1", "street2", "seattle", "wa", 98195, company1, 200);
		sitDownRestaurantsDao.create(restaurant1);
		sitDownRestaurantsDao.create(restaurant2);
		sitDownRestaurantsDao.create(restaurant3);

		TakeOutRestaurants restaurant4 = new TakeOutRestaurants("restaurant4", "about restaurant",
						"menu", "hours", true, Restaurants.CuisineType.EUROPEAN,
						"street1", "street2", "seattle", "wa", 98195, company1, 60);
		TakeOutRestaurants restaurant5 = new TakeOutRestaurants("restaurant5", "about restaurant",
						"menu", "hours", true, Restaurants.CuisineType.HISPANIC,
						"street1", "street2", "seattle", "wa", 98195, company1, 90);
		takeOutRestaurantsDao.create(restaurant4);
		takeOutRestaurantsDao.create(restaurant5);

		FoodCartRestaurants restaurant6 = new FoodCartRestaurants("restaurant6", "about restaurant",
						"menu", "hours", true, Restaurants.CuisineType.HISPANIC,
						"street1", "street2", "bellevue", "wa", 98008, company2, true);
		FoodCartRestaurants restaurant7 = new FoodCartRestaurants("restaurant7", "about restaurant",
						"menu", "hours", true, Restaurants.CuisineType.HISPANIC,
						"street1", "street2", "bellevue", "wa", 98008, company2, true);
		FoodCartRestaurants restaurant8 = new FoodCartRestaurants("restaurant8", "about restaurant",
						"menu", "hours", true, Restaurants.CuisineType.HISPANIC,
						"street1", "street2", "bellevue", "wa", 98008, company2, true);
		FoodCartRestaurants restaurant9 = new FoodCartRestaurants("restaurant9", "about restaurant",
						"menu", "hours", false, Restaurants.CuisineType.AMERICAN,
						"street1", "street2", "bellevue", "wa", 98008, company2, true);
		FoodCartRestaurants restaurant10 = new FoodCartRestaurants("restaurant10", "about restaurant",
						"menu", "hours", true, Restaurants.CuisineType.AMERICAN,
						"street1", "street2", "bellevue", "wa", 98008, company3, false);
		foodCartRestaurantsDao.create(restaurant6);
		foodCartRestaurantsDao.create(restaurant7);
		foodCartRestaurantsDao.create(restaurant8);
		foodCartRestaurantsDao.create(restaurant9);
		foodCartRestaurantsDao.create(restaurant10);

		Reviews review1 = new Reviews("Delightful!", 5.0, user1, restaurant1);
		Reviews review2 = new Reviews("Superb!", 5.0, user1, restaurant2);
		Reviews review3 = new Reviews("Superb!", 5.0, user1, restaurant9);
		Reviews review4 = new Reviews("Not good", 1.0, user4, restaurant9);
		Reviews review5 = new Reviews("Not good at all", 1.0, user4, restaurant10);

		reviewsDao.create(review1);
		reviewsDao.create(review2);
		reviewsDao.create(review3);
		reviewsDao.create(review4);
		reviewsDao.create(review5);

		Recommendations recommendations1 = new Recommendations(user1, restaurant1);
		Recommendations recommendations2 = new Recommendations(user1, restaurant2);
		Recommendations recommendations3 = new Recommendations(user1, restaurant3);
		Recommendations recommendations4 = new Recommendations(user1, restaurant4);
		Recommendations recommendations5 = new Recommendations(user1, restaurant5);
		Recommendations recommendations6 = new Recommendations(user3, restaurant2);
		Recommendations recommendations7 = new Recommendations(user3, restaurant3);
		Recommendations recommendations8 = new Recommendations(user2, restaurant3);

		recommendationsDao.create(recommendations1);
		recommendationsDao.create(recommendations2);
		recommendationsDao.create(recommendations3);
		recommendationsDao.create(recommendations4);
		recommendationsDao.create(recommendations5);
		recommendationsDao.create(recommendations6);
		recommendationsDao.create(recommendations7);
		recommendationsDao.create(recommendations8);

		Reservations reservations1 = new Reservations(new Timestamp(1), new Timestamp(100), 2, user1, restaurant1);
		Reservations reservations2 = new Reservations(new Timestamp(2), new Timestamp(100), 2, user1, restaurant1);
		Reservations reservations3 = new Reservations(new Timestamp(3), new Timestamp(100), 2, user1, restaurant1);
		Reservations reservations4 = new Reservations(new Timestamp(4), new Timestamp(100), 2, user1, restaurant2);
		Reservations reservations5 = new Reservations(new Timestamp(5), new Timestamp(100), 2, user1, restaurant2);
		Reservations reservations6 = new Reservations(new Timestamp(6), new Timestamp(100), 2, user2, restaurant3);
		Reservations reservations7 = new Reservations(new Timestamp(7), new Timestamp(100), 2, user2, restaurant1);
		Reservations reservations8 = new Reservations(new Timestamp(8), new Timestamp(100), 2, user2, restaurant3);
		Reservations reservations9 = new Reservations(new Timestamp(9), new Timestamp(100), 2, user2, restaurant3);

		reservationsDao.create(reservations1);
		reservationsDao.create(reservations2);
		reservationsDao.create(reservations3);
		reservationsDao.create(reservations4);
		reservationsDao.create(reservations5);
		reservationsDao.create(reservations6);
		reservationsDao.create(reservations7);
		reservationsDao.create(reservations8);
		reservationsDao.create(reservations9);


		CreditCards creditCard1 = new CreditCards(Long.parseLong("3499432187650987"), new Date(1), user1);
		CreditCards creditCard2 = new CreditCards(Long.parseLong("3488432187650987"), new Date(1), user1);
		CreditCards creditCard3 = new CreditCards(Long.parseLong("3799432187650987"), new Date(1), user1);
		CreditCards creditCard4 = new CreditCards(Long.parseLong("6011432187650987"), new Date(1), user1);
		CreditCards creditCard5 = new CreditCards(Long.parseLong("6011432187650988"), new Date(1), user1);
		CreditCards creditCard6 = new CreditCards(Long.parseLong("6441432187650987"), new Date(1), user1);
		CreditCards creditCard7 = new CreditCards(Long.parseLong("6451432187650987"), new Date(1), user1);
		CreditCards creditCard8 = new CreditCards(Long.parseLong("5199432187650987"), new Date(1), user1);
		CreditCards creditCard9 = new CreditCards(Long.parseLong("5499432187650987"), new Date(1), user1);
		CreditCards creditCard10 = new CreditCards(Long.parseLong("5499432187650988"), new Date(1), user1);
		CreditCards creditCard11 = new CreditCards(Long.parseLong("5499432187650989"), new Date(1), user1);
		CreditCards creditCard12 = new CreditCards(Long.parseLong("4499432187650987"), new Date(1), user1);
		CreditCards creditCard13 = new CreditCards(Long.parseLong("4499432187650989"), new Date(1), user1);

		creditCardsDao.create(creditCard1);
		creditCardsDao.create(creditCard2);
		creditCardsDao.create(creditCard3);
		creditCardsDao.create(creditCard4);
		creditCardsDao.create(creditCard5);
		creditCardsDao.create(creditCard6);
		creditCardsDao.create(creditCard7);
		creditCardsDao.create(creditCard8);
		creditCardsDao.create(creditCard9);
		creditCardsDao.create(creditCard10);
		creditCardsDao.create(creditCard11);
		creditCardsDao.create(creditCard12);
		creditCardsDao.create(creditCard13);

		// READ.
		usersDao.getUserByUserName("Bruce");
		creditCardsDao.getCreditCardByCardNumber(Long.parseLong("4499432187650989"));
		creditCardsDao.getCreditCardsByUserName("Bruce");
		companiesDao.getCompanyByCompanyName("company1");
		restaurantsDao.getRestaurantByRestaurantId(1);
		restaurantsDao.getRestaurantByCuisine(Restaurants.CuisineType.AMERICAN);
		restaurantsDao.getRestaurantsByCompanyName("company1");
		sitDownRestaurantsDao.getRestaurantByRestaurantId(1);
		sitDownRestaurantsDao.getRestaurantByCuisine(Restaurants.CuisineType.AMERICAN);
		sitDownRestaurantsDao.getRestaurantsByCompanyName("company1");
		takeOutRestaurantsDao.getRestaurantByRestaurantId(4);
		takeOutRestaurantsDao.getRestaurantByCuisine(Restaurants.CuisineType.EUROPEAN);
		takeOutRestaurantsDao.getRestaurantsByCompanyName("company1");
		foodCartRestaurantsDao.getRestaurantByRestaurantId(6);
		foodCartRestaurantsDao.getRestaurantByCuisine(Restaurants.CuisineType.HISPANIC);
		foodCartRestaurantsDao.getRestaurantsByCompanyName("company2");
		reviewsDao.getReviewById(1);
		reviewsDao.getReviewByUserName("Bruce");
		reviewsDao.getReviewsByRestaurantId(1);
		recommendationsDao.getRecommendationById(1);
		recommendationsDao.getRecommendationByUserName("Bruce");
		recommendationsDao.getRecommendationsByRestaurantId(1);
		reservationsDao.getReservationById(1);
		reservationsDao.getReservationByUserName("Bruce");
		reservationsDao.getReservationsByRestaurantId(1);

		creditCardsDao.updateExpiration(creditCard1, new Date(5));
		companiesDao.updateAbout(company1, "new about company1");

		usersDao.delete(user1);
		creditCardsDao.delete(creditCard1);
		companiesDao.delete(company1);
		restaurantsDao.delete(restaurant1);
		sitDownRestaurantsDao.delete(restaurant2);
		takeOutRestaurantsDao.delete(restaurant4);
		foodCartRestaurantsDao.delete(restaurant6);
		reviewsDao.delete(review1);
		recommendationsDao.delete(recommendations1);
		reservationsDao.delete(reservations1);
	}
}
