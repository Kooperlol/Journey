package me.kooper.fbla.models;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class Attributes {

    /* Stores categories and conditions with their formatted name as
    the key and API reference for the value */
    private final HashMap<String, String> CATEGORIES;
    private final HashMap<String, String> CONDITIONS;

    public Attributes() {
        CATEGORIES = new LinkedHashMap<>();
        CONDITIONS = new LinkedHashMap<>();
    }

    // request to get the conditions and store them in its hashmap
    public HashMap<String, String> getCONDITIONS() {
        if (CONDITIONS.isEmpty()) {
            setConditions();
        }
        return CONDITIONS;
    }

    // request to get the categories and store them in its hashmap
    public HashMap<String, String> getCATEGORIES() {
        if (CATEGORIES.isEmpty()) {
            setCategories();
        }
        return CATEGORIES;
    }

    // sets the conditions with its formatted name and its API reference
    private void setConditions() {
        CONDITIONS.put("Dogs Allowed", "dogs.yes");
        CONDITIONS.put("Dogs Leashed", "dogs.leashed");
        CONDITIONS.put("Dogs Prohibited", "no_dogs");
        CONDITIONS.put("Egg Free", "egg_free");
        CONDITIONS.put("Entrance Fee", "fee");
        CONDITIONS.put("Free Entrance", "no_fee.no");
        CONDITIONS.put("Free Internet", "internet_access.free");
        CONDITIONS.put("Gluten Free", "gluten_free");
        CONDITIONS.put("Halal", "halal,halal.only");
        CONDITIONS.put("Kosher", "kosher,kosher.only");
        CONDITIONS.put("Organic", "organic,organic.only");
        CONDITIONS.put("Soy Free", "soy_free");
        CONDITIONS.put("Sugar Free", "sugar_free");
        CONDITIONS.put("Vegetarian", "vegetarian,vegan,vegan.only,vegetarian.only");
        CONDITIONS.put("Wheelchair Access", "wheelchair.yes");
    }

    // sets the categories with its formatted name and its API reference
    private void setCategories() {
        CATEGORIES.put("Activities", "activity");
        CATEGORIES.put("Airport", "airport");
        CATEGORIES.put("American Restaurant", "catering.restaurant.american");
        CATEGORIES.put("Antique Store", "commercial.antiques");
        CATEGORIES.put("Apartments", "accommodation.apartment");
        CATEGORIES.put("Aquarium", "entertainment.aquarium");
        CATEGORIES.put("Arcade", "entertainment.amusement_arcade");
        CATEGORIES.put("Art Galleries", "entertainment.culture.arts_centre");
        CATEGORIES.put("Asian Restaurant", "catering.restaurant.asian");
        CATEGORIES.put("Bakery", "commercial.food_and_drink.bakery");
        CATEGORIES.put("Bar", "catering.bar");
        CATEGORIES.put("BBQ Restaurant", "catering.restaurant.barbecue");
        CATEGORIES.put("Beach", "beach");
        CATEGORIES.put("Bicycle Rental", "rental.bicycle");
        CATEGORIES.put("Bicycle Store", "commercial.outdoor_and_sport.bicycle");
        CATEGORIES.put("Boat Rental", "rental.boat");
        CATEGORIES.put("Book Store", "commercial.books");
        CATEGORIES.put("Bowling", "entertainment.bowling_alley");
        CATEGORIES.put("Burger Restaurant", "catering.restaurant.burger");
        CATEGORIES.put("Cafe", "catering.cafe");
        CATEGORIES.put("Camping", "camping");
        CATEGORIES.put("Car Charging Station", "service.vehicle.charging_station");
        CATEGORIES.put("Car Rental", "rental.car");
        CATEGORIES.put("Car Wash", "service.vehicle.car_wash");
        CATEGORIES.put("Chinese Restaurant", "catering.restaurant.chinese");
        CATEGORIES.put("Cinema", "entertainment.cinema");
        CATEGORIES.put("Clothing Store", "commercial.clothing");
        CATEGORIES.put("Community Center", "activity.community_center");
        CATEGORIES.put("Convenience Store", "commercial.convenience");
        CATEGORIES.put("Deli", "commercial.food_and_drink.deli");
        CATEGORIES.put("Department Store", "commercial");
        CATEGORIES.put("Discount Store", "commercial.discount_store");
        CATEGORIES.put("Dog Park", "pet.dog_park");
        CATEGORIES.put("Electronic Store", "commercial.elektronics");
        CATEGORIES.put("Entertainment", "entertainment");
        CATEGORIES.put("Escape Room", "entertainment.escape_game");
        CATEGORIES.put("Fast Food Restaurant", "catering.fast_food");
        CATEGORIES.put("Fish and Chips Restaurant", "catering.restaurant.fish_and_chips");
        CATEGORIES.put("Fishing Store", "commercial.outdoor_and_sport.fishing");
        CATEGORIES.put("Food and Drink Store", "commercial.food_and_drink");
        CATEGORIES.put("Food Court", "catering.food_court");
        CATEGORIES.put("Forests", "natural.forest");
        CATEGORIES.put("French Restaurant", "catering.restaurant.french");
        CATEGORIES.put("Garden", "leisure.park.garden");
        CATEGORIES.put("Gas Station", "commercial.gas");
        CATEGORIES.put("German Restaurant", "catering.restaurant.german");
        CATEGORIES.put("Gift Shop", "commercial.gift_and_souvenir");
        CATEGORIES.put("Guest House", "accommodation.guest_house");
        CATEGORIES.put("Historical Buildings", "building.historic");
        CATEGORIES.put("Horse Riding", "sport.horse_riding");
        CATEGORIES.put("Hotels", "accommodation.hotel");
        CATEGORIES.put("Hunting Store", "commercial.outdoor_and_sport.water_sports");
        CATEGORIES.put("Ice Cream", "catering.ice_cream");
        CATEGORIES.put("Ice Rink", "sport.ice_rink");
        CATEGORIES.put("Indian Restaurant", "catering.restaurant.indian");
        CATEGORIES.put("Italian Restaurant", "catering.restaurant.italian");
        CATEGORIES.put("Japanese Restaurant", "catering.restaurant.japanese");
        CATEGORIES.put("Jewelry Store", "commercial.jewelry");
        CATEGORIES.put("Korean Restaurant", "catering.restaurant.korean");
        CATEGORIES.put("Lottery Tickets", "commercial.tickets_and_lottery");
        CATEGORIES.put("Marketplace", "commercial.marketplace");
        CATEGORIES.put("Media Store", "commercial.video_and_music");
        CATEGORIES.put("Mexican Restaurant", "catering.restaurant.mexican");
        CATEGORIES.put("Mini Golf", "entertainment.miniature_golf");
        CATEGORIES.put("Motel", "accommodation.motel");
        CATEGORIES.put("Mountain", "natural.mountain");
        CATEGORIES.put("Museum", "entertainment.museum");
        CATEGORIES.put("National Park", "national_park");
        CATEGORIES.put("Nature Reserves", "leisure.park.nature_reserve");
        CATEGORIES.put("Nature", "natural");
        CATEGORIES.put("Noodle Restaurant", "catering.restaurant.noodle");
        CATEGORIES.put("Outdoor and Sports Store", "commercial.outdoor_and_sport");
        CATEGORIES.put("Park", "leisure.park");
        CATEGORIES.put("Picnic Area", "leisure.picnic.picnic_site");
        CATEGORIES.put("Pizza Restaurant", "catering.restaurant.pizza");
        CATEGORIES.put("Places to stay", "accommodation");
        CATEGORIES.put("Planetarium", "entertainment.planetarium");
        CATEGORIES.put("Playground", "leisure.playground");
        CATEGORIES.put("Pub", "catering.pub");
        CATEGORIES.put("Public Transportation", "public_transport");
        CATEGORIES.put("Rental", "rental");
        CATEGORIES.put("Restaurant", "catering.restaurant");
        CATEGORIES.put("Sandwich Restaurant", "catering.restaurant.sandwich");
        CATEGORIES.put("Seafood Restaurant", "catering.restaurant.seafood");
        CATEGORIES.put("Shopping Mall", "commercial.shopping_mall");
        CATEGORIES.put("Shopping", "commercial");
        CATEGORIES.put("Spa", "leisure.spa");
        CATEGORIES.put("Sport Stadium", "sport.stadium");
        CATEGORIES.put("Sports Club", "activity.sport_club");
        CATEGORIES.put("Spring", "natural.water.spring");
        CATEGORIES.put("Steak Restaurant", "catering.restaurant.steak_house");
        CATEGORIES.put("Supermarket", "commercial.supermarket");
        CATEGORIES.put("Sushi Restaurant", "catering.restaurant.sushi");
        CATEGORIES.put("Swimming Pool", "sport.swimming_pool");
        CATEGORIES.put("Taxi", "service.taxi");
        CATEGORIES.put("Thai Restaurant", "catering.restaurant.thai");
        CATEGORIES.put("Theatre", "entertainment.culture.theatre");
        CATEGORIES.put("Theme Park", "entertainment.theme_park");
        CATEGORIES.put("Toy Store", "commercial.toy_and_game");
        CATEGORIES.put("Travel Agency", "service.travel_agency");
        CATEGORIES.put("Turkish Restaurant", "catering.restaurant.turkish");
        CATEGORIES.put("Vietnamese Restaurant", "catering.restaurant.vietnamese");
        CATEGORIES.put("Water Park", "entertainment.water_park");
        CATEGORIES.put("Wings Restaurant", "catering.restaurant.wings");
        CATEGORIES.put("Zoo", "entertainment.zoo");
    }

}
