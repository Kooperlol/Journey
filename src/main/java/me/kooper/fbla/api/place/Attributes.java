package me.kooper.fbla.api.place;

import java.util.HashMap;

public class Attributes {

    /* Stores categories and conditions with their formatted name as
    the key and API reference for the value */
    private final HashMap<String, String> categories = new HashMap<>();
    private final HashMap<String, String> conditions = new HashMap<>();

    // request to get the conditions and store them in its hashmap
    public HashMap<String, String> getConditions() {
        if (conditions.isEmpty()) {
            setConditions();
        }
        return conditions;
    }

    // request to get the categories and store them in its hashmap
    public HashMap<String, String> getCategories() {
        if (categories.isEmpty()) {
            setCategories();
        }
        return categories;
    }

    // sets the conditions with its formatted name and its API reference
    private void setConditions() {
        conditions.put("Dogs Allowed", "dogs.yes");
        conditions.put("Dogs Leashed", "dogs.leashed");
        conditions.put("Dogs Prohibited", "no_dogs");
        conditions.put("Egg Free", "egg_free");
        conditions.put("Entrance Fee", "fee");
        conditions.put("Free Entrance", "no_fee.no");
        conditions.put("Free Internet", "internet_access.free");
        conditions.put("Gluten Free", "gluten_free");
        conditions.put("Halal", "halal,halal.only");
        conditions.put("Kosher", "kosher,kosher.only");
        conditions.put("Organic", "organic,organic.only");
        conditions.put("Soy Free", "soy_free");
        conditions.put("Sugar Free", "sugar_free");
        conditions.put("Vegetarian", "vegetarian,vegan,vegan.only,vegetarian.only");
        conditions.put("Wheelchair Access", "wheelchair.yes");
    }

    // sets the categories with its formatted name and its API reference
    private void setCategories() {
        categories.put("Activities", "activity");
        categories.put("Airport", "airport");
        categories.put("American Restaurant", "catering.restaurant.american");
        categories.put("Antique Store", "commercial.antiques");
        categories.put("Apartments", "accommodation.apartment");
        categories.put("Aquarium", "entertainment.aquarium");
        categories.put("Arcade", "entertainment.amusement_arcade");
        categories.put("Art Galleries", "entertainment.culture.arts_centre");
        categories.put("Asian Restaurant", "catering.restaurant.asian");
        categories.put("Bakery", "commercial.food_and_drink.bakery");
        categories.put("Bar", "catering.bar");
        categories.put("BBQ Restaurant", "catering.restaurant.barbecue");
        categories.put("Beach", "beach");
        categories.put("Bicycle Rental", "rental.bicycle");
        categories.put("Bicycle Store", "commercial.outdoor_and_sport.bicycle");
        categories.put("Boat Rental", "rental.boat");
        categories.put("Book Store", "commercial.books");
        categories.put("Bowling", "entertainment.bowling_alley");
        categories.put("Burger Restaurant", "catering.restaurant.burger");
        categories.put("Cafe", "catering.cafe");
        categories.put("Camping", "camping");
        categories.put("Car Charging Station", "service.vehicle.charging_station");
        categories.put("Car Rental", "rental.car");
        categories.put("Car Wash", "service.vehicle.car_wash");
        categories.put("Chinese Restaurant", "catering.restaurant.chinese");
        categories.put("Cinema", "entertainment.cinema");
        categories.put("Clothing Store", "commercial.clothing");
        categories.put("Community Center", "activity.community_center");
        categories.put("Convenience Store", "commercial.convenience");
        categories.put("Deli", "commercial.food_and_drink.deli");
        categories.put("Department Store", "commercial");
        categories.put("Discount Store", "commercial.discount_store");
        categories.put("Dog Park", "pet.dog_park");
        categories.put("Electronic Store", "commercial.elektronics");
        categories.put("Entertainment", "entertainment");
        categories.put("Escape Room", "entertainment.escape_game");
        categories.put("Fast Food Restaurant", "catering.fast_food");
        categories.put("Fish and Chips Restaurant", "catering.restaurant.fish_and_chips");
        categories.put("Fishing Store", "commercial.outdoor_and_sport.fishing");
        categories.put("Food and Drink Store", "commercial.food_and_drink");
        categories.put("Food Court", "catering.food_court");
        categories.put("Forests", "natural.forest");
        categories.put("French Restaurant", "catering.restaurant.french");
        categories.put("Garden", "leisure.park.garden");
        categories.put("Gas Station", "commercial.gas");
        categories.put("German Restaurant", "catering.restaurant.german");
        categories.put("Gift Shop", "commercial.gift_and_souvenir");
        categories.put("Guest House", "accommodation.guest_house");
        categories.put("Historical Buildings", "building.historic");
        categories.put("Horse Riding", "sport.horse_riding");
        categories.put("Hotels", "accommodation.hotel");
        categories.put("Hunting Store", "commercial.outdoor_and_sport.water_sports");
        categories.put("Ice Cream", "catering.ice_cream");
        categories.put("Ice Rink", "sport.ice_rink");
        categories.put("Indian Restaurant", "catering.restaurant.indian");
        categories.put("Italian Restaurant", "catering.restaurant.italian");
        categories.put("Japanese Restaurant", "catering.restaurant.japanese");
        categories.put("Jewelry Store", "commercial.jewelry");
        categories.put("Korean Restaurant", "catering.restaurant.korean");
        categories.put("Lottery Tickets", "commercial.tickets_and_lottery");
        categories.put("Marketplace", "commercial.marketplace");
        categories.put("Media Store", "commercial.video_and_music");
        categories.put("Mexican Restaurant", "catering.restaurant.mexican");
        categories.put("Mini Golf", "entertainment.miniature_golf");
        categories.put("Motel", "accommodation.motel");
        categories.put("Mountain", "natural.mountain");
        categories.put("Museum", "entertainment.museum");
        categories.put("National Park", "national_park");
        categories.put("Nature Reserves", "leisure.park.nature_reserve");
        categories.put("Nature", "natural");
        categories.put("Noodle Restaurant", "catering.restaurant.noodle");
        categories.put("Outdoor and Sports Store", "commercial.outdoor_and_sport");
        categories.put("Park", "leisure.park");
        categories.put("Picnic Area", "leisure.picnic.picnic_site");
        categories.put("Pizza Restaurant", "catering.restaurant.pizza");
        categories.put("Places to stay", "accommodation");
        categories.put("Planetarium", "entertainment.planetarium");
        categories.put("Playground", "leisure.playground");
        categories.put("Pub", "catering.pub");
        categories.put("Public Transportation", "public_transport");
        categories.put("Rental", "rental");
        categories.put("Restaurant", "catering.restaurant");
        categories.put("Sandwich Restaurant", "catering.restaurant.sandwich");
        categories.put("Seafood Restaurant", "catering.restaurant.seafood");
        categories.put("Shopping Mall", "commercial.shopping_mall");
        categories.put("Shopping", "commercial");
        categories.put("Spa", "leisure.spa");
        categories.put("Sport Stadium", "sport.stadium");
        categories.put("Sports Club", "activity.sport_club");
        categories.put("Spring", "natural.water.spring");
        categories.put("Steak Restaurant", "catering.restaurant.steak_house");
        categories.put("Supermarket", "commercial.supermarket");
        categories.put("Sushi Restaurant", "catering.restaurant.sushi");
        categories.put("Swimming Pool", "sport.swimming_pool");
        categories.put("Taxi", "service.taxi");
        categories.put("Thai Restaurant", "catering.restaurant.thai");
        categories.put("Theatre", "entertainment.culture.theatre");
        categories.put("Theme Park", "entertainment.theme_park");
        categories.put("Toy Store", "commercial.toy_and_game");
        categories.put("Travel Agency", "service.travel_agency");
        categories.put("Turkish Restaurant", "catering.restaurant.turkish");
        categories.put("Vietnamese Restaurant", "catering.restaurant.vietnamese");
        categories.put("Water Park", "entertainment.water_park");
        categories.put("Wings Restaurant", "catering.restaurant.wings");
        categories.put("Zoo", "entertainment.zoo");
    }

}
