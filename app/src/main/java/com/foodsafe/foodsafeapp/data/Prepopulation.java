package com.foodsafe.foodsafeapp.data;

import com.foodsafe.foodsafeapp.model.Alimento;
import com.foodsafe.foodsafeapp.model.Receita;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Prepopulation {
    public static List<Alimento> getAlimentos() {
        List<Alimento> alimentos = new ArrayList<>();

        alimentos.add(new Alimento("Apple", new ArrayList<>(), "Fresh and crisp apple", "ic_apple"));
        alimentos.add(new Alimento("Banana", new ArrayList<>(), "Ripe banana", "ic_banana"));
        alimentos.add(new Alimento("Orange", new ArrayList<>(), "Juicy orange", "ic_orange"));
        alimentos.add(new Alimento("Strawberry", new ArrayList<>(), "Sweet strawberry", "ic_strawberry"));
        alimentos.add(new Alimento("Grapes", new ArrayList<>(), "A bunch of grapes", "ic_grapes"));
        alimentos.add(new Alimento("Watermelon", new ArrayList<>(), "Refreshing watermelon", "ic_watermelon"));
        alimentos.add(new Alimento("Pineapple", new ArrayList<>(), "Tropical pineapple", "ic_pineapple"));
        alimentos.add(new Alimento("Mango", new ArrayList<>(), "Sweet and juicy mango", "ic_mango"));
        alimentos.add(new Alimento("Blueberry", new ArrayList<>(), "Small, sweet blueberries", "ic_blueberry"));
        alimentos.add(new Alimento("Raspberry", new ArrayList<>(), "Tart and sweet raspberry", "ic_raspberry"));
        alimentos.add(new Alimento("Avocado", new ArrayList<>(), "Creamy avocado", "ic_avocado"));
        alimentos.add(new Alimento("Carrot", new ArrayList<>(), "Crunchy carrot", "ic_carrot"));
        alimentos.add(new Alimento("Broccoli", new ArrayList<>(), "Healthy broccoli florets", "ic_broccoli"));
        alimentos.add(new Alimento("Spinach", new ArrayList<>(), "Leafy green spinach", "ic_spinach"));
        alimentos.add(new Alimento("Tomato", new ArrayList<>(), "Ripe red tomato", "ic_tomato"));
        alimentos.add(new Alimento("Cucumber", new ArrayList<>(), "Cooling cucumber", "ic_cucumber"));
        alimentos.add(new Alimento("Bell Pepper", new ArrayList<>(), "Colorful bell pepper", "ic_bell_pepper"));
        alimentos.add(new Alimento("Onion", new ArrayList<>(), "Aromatic onion", "ic_onion"));
        alimentos.add(new Alimento("Garlic", new ArrayList<>(), "Pungent garlic clove", "ic_garlic"));
        alimentos.add(new Alimento("Potato", new ArrayList<>(), "Versatile potato", "ic_potato"));
        alimentos.add(new Alimento("Sweet Potato", new ArrayList<>(), "Sweet and nutritious sweet potato", "ic_sweet_potato"));
        alimentos.add(new Alimento("Lettuce", new ArrayList<>(), "Crisp lettuce leaves", "ic_lettuce"));
        alimentos.add(new Alimento("Mushroom", new ArrayList<>(), "Earthy mushrooms", "ic_mushroom"));
        alimentos.add(new Alimento("Corn", new ArrayList<>(), "Sweet corn on the cob", "ic_corn"));
        alimentos.add(new Alimento("Zucchini", new ArrayList<>(), "Summer squash", "ic_zucchini"));

        alimentos.add(new Alimento("Chicken Breast",
                Arrays.asList("Vegan Diet", "Vegetarian Diet"),
                "Lean source of protein", "ic_chicken"));

        alimentos.add(new Alimento("Beef",
                Arrays.asList("Vegan Diet", "Vegetarian Diet"),
                "Rich in iron", "ic_beef"));

        alimentos.add(new Alimento("Pork",
                Arrays.asList("Vegan Diet", "Vegetarian Diet"),
                "Pork meat", "ic_pork"));

        alimentos.add(new Alimento("Salmon",
                Arrays.asList("Fish", "Seafood Allergy", "Vegan Diet", "Vegetarian Diet"),
                "Rich in omega-3 fatty acids", "ic_salmon"));

        alimentos.add(new Alimento("Tuna",
                Arrays.asList("Fish", "Seafood Allergy", "Vegan Diet", "Vegetarian Diet"),
                "Tuna fish", "ic_tuna"));

        alimentos.add(new Alimento("Shrimp",
                Arrays.asList("Shellfish", "Seafood Allergy", "Vegan Diet", "Vegetarian Diet"),
                "Shrimp", "ic_shrimp"));

        alimentos.add(new Alimento("Crab",
                Arrays.asList("Shellfish", "Seafood Allergy", "Vegan Diet", "Vegetarian Diet"),
                "", "ic_crab"));

        alimentos.add(new Alimento("Lobster",
                Arrays.asList("Shellfish", "Seafood Allergy", "Vegan Diet", "Vegetarian Diet"),
                "", "ic_lobster"));

        alimentos.add(new Alimento("Clams",
                Arrays.asList("Shellfish", "Seafood Allergy", "Vegan Diet", "Vegetarian Diet"),
                "", "ic_clams"));

        alimentos.add(new Alimento("Scallops",
                Arrays.asList("Shellfish", "Seafood Allergy", "Vegan Diet", "Vegetarian Diet"),
                "", "ic_scallops"));

        alimentos.add(new Alimento("Oysters",
                Arrays.asList("Shellfish", "Seafood Allergy", "Vegan Diet", "Vegetarian Diet"),
                "", "ic_oysters"));

        alimentos.add(new Alimento("Mussels",
                Arrays.asList("Shellfish", "Seafood Allergy", "Vegan Diet", "Vegetarian Diet"),
                "", "ic_mussels"));

        alimentos.add(new Alimento("Cod",
                Arrays.asList("Fish", "Seafood Allergy", "Vegan Diet", "Vegetarian Diet"),
                "", "ic_cod"));

        alimentos.add(new Alimento("Haddock",
                Arrays.asList("Fish", "Seafood Allergy", "Vegan Diet", "Vegetarian Diet"),
                "", "ic_haddock"));

        alimentos.add(new Alimento("Trout",
                Arrays.asList("Fish", "Seafood Allergy", "Vegan Diet", "Vegetarian Diet"),
                "", "ic_trout"));

        alimentos.add(new Alimento("Sardines",
                Arrays.asList("Fish", "Seafood Allergy", "Vegan Diet", "Vegetarian Diet"),
                "", "ic_sardines"));

        alimentos.add(new Alimento("Anchovies",
                Arrays.asList("Fish", "Seafood Allergy", "Vegan Diet", "Vegetarian Diet"),
                "", "ic_anchovies"));

        alimentos.add(new Alimento("Rice", new ArrayList<>(), "A staple food", "ic_rice"));

        alimentos.add(new Alimento("Pasta",
                Arrays.asList("Gluten", "Celiac Disease / Gluten Sensitivity"),
                "Made from wheat flour", "ic_pasta"));

        alimentos.add(new Alimento("Bread",
                Arrays.asList("Gluten", "Celiac Disease / Gluten Sensitivity"),
                "Bread", "ic_bread"));

        alimentos.add(new Alimento("Oats", new ArrayList<>(), "Often eaten for breakfast", "ic_oats"));
        alimentos.add(new Alimento("Quinoa", new ArrayList<>(), "A healthy grain", "ic_quinoa"));
        alimentos.add(new Alimento("Lentils", new ArrayList<>(), "A type of legume", "ic_lentils"));

        return alimentos;
    }

    public static List<Receita> getReceitas() {
        List<Receita> receitas = new ArrayList<>();

        receitas.add(new Receita("Fruit Salad", "A simple and healthy fruit salad.",
                "Apple, Banana, Orange, Strawberry, Grapes",
                "Chop fruits and mix them together.",
                "Vegan Diet, Vegetarian Diet",
                "ic_fruit_salad", new ArrayList<>()));

        receitas.add(new Receita("Green Smoothie", "A nutritious green smoothie.",
                "Spinach, Banana, Pineapple, Water",
                "Blend all ingredients until smooth.",
                "Vegan Diet, Vegetarian Diet",
                "ic_green_smoothie", new ArrayList<>()));

        receitas.add(new Receita("Avocado Toast", "A popular and easy breakfast.",
                "Avocado, Bread, Salt, Pepper",
                "Toast bread, mash avocado on top, season with salt and pepper.",
                "Celiac Disease / Gluten Sensitivity, Vegan Diet, Vegetarian Diet",
                "ic_avocado_toast", Arrays.asList("Gluten")));

        receitas.add(new Receita("Vegetable Omelette", "A classic omelette with vegetables.",
                "Eggs, Bell Pepper, Onion, Mushroom",
                "Whisk eggs, pour into a hot pan, add vegetables, fold and cook.",
                "Animal Product, Egg Allergy, Vegetarian Diet",
                "ic_vegetable_omelette", Arrays.asList("Egg Allergy, Vegan Diet")));

        receitas.add(new Receita("Chicken Salad", "A simple chicken salad.",
                "Chicken Breast, Lettuce, Tomato, Cucumber",
                "Cook chicken and mix with vegetables.",
                "Animal Product, Vegetarian Diet, Vegan Diet",
                "ic_chicken_salad", Arrays.asList("Animal Product, Vegetarian Diet, Vegan Diet")));

        receitas.add(new Receita("Beef Stir-fry", "A quick and flavorful beef stir-fry.",
                "Beef, Broccoli, Soy Sauce, Ginger",
                "Stir-fry ingredients.",
                "Animal Product, Vegetarian Diet, Vegan Diet",
                "ic_beef_stir_fry", Arrays.asList("Animal Product, Vegetarian Diet, Vegan Diet")));

        receitas.add(new Receita("Baked Salmon", "A healthy and delicious baked salmon.",
                "Salmon, Lemon, Dill",
                "Bake salmon.",
                "Animal Product, Seafood Allergy, Vegetarian Diet, Vegan Diet",
                "ic_baked_salmon", Arrays.asList("Seafood Allergy", "Animal Product", "Vegetarian Diet", "Vegan Diet")));

        receitas.add(new Receita("Shrimp Scampi with Zucchini Noodles",
                "A low-carb shrimp scampi.",
                "Shrimp, Zucchini, Garlic, Butter",
                "Cook shrimp with garlic and butter.",
                "Animal Product, Seafood Allergy, Dairy Allergy, Vegetarian Diet, Vegan Diet",
                "ic_shrimp_scampi", Arrays.asList("Seafood Allergy", "Dairy Allergy", "Animal Product", "Vegetarian Diet", "Vegan Diet")));

        receitas.add(new Receita("Mushroom Risotto", "A creamy mushroom risotto.",
                "Arborio Rice, Mushroom, Onion, Parmesan Cheese",
                "Cook rice with mushrooms and cheese.",
                "Animal Product, Lactose Intolerance, Dairy Allergy, Vegetarian Diet, Vegan Diet",
                "ic_mushroom_risotto", Arrays.asList("Dairy Allergy", "Animal Product, Vegan Diet")));

        receitas.add(new Receita("Lentil Soup", "A hearty lentil soup.",
                "Lentils, Carrot, Celery, Onion",
                "Cook all ingredients.",
                "Vegan Diet, Vegetarian Diet",
                "ic_lentil_soup", new ArrayList<>()));

        receitas.add(new Receita("Quinoa Salad", "A refreshing quinoa salad.",
                "Quinoa, Cucumber, Tomato, Lemon Juice",
                "Mix ingredients.",
                "Vegan Diet, Vegetarian Diet",
                "ic_quinoa_salad", new ArrayList<>()));

        receitas.add(new Receita("Sweet Potato Fries",
                "Baked sweet potato fries.",
                "Sweet Potato, Olive Oil, Paprika",
                "Bake fries.",
                "Vegan Diet, Vegetarian Diet",
                "ic_sweet_potato_fries", new ArrayList<>()));

        receitas.add(new Receita("Spaghetti with Marinara Sauce",
                "Pasta with tomato sauce.",
                "Spaghetti, Tomato, Garlic, Basil",
                "Cook pasta and sauce.",
                "Celiac Disease / Gluten Sensitivity, Vegan Diet, Vegetarian Diet",
                "ic_spaghetti_marinara", Arrays.asList("Gluten")));

        receitas.add(new Receita("Garlic Bread",
                "Toasted garlic bread.",
                "Bread, Garlic, Butter, Parsley",
                "Toast bread with garlic butter.",
                "Celiac Disease / Gluten Sensitivity, Dairy Allergy",
                "ic_garlic_bread", Arrays.asList("Gluten", "Dairy Allergy")));

        receitas.add(new Receita("Oatmeal with Berries",
                "Warm oatmeal with berries.",
                "Oats, Milk, Blueberries, Raspberries",
                "Cook oats with milk.",
                "Animal Product, Lactose Intolerance, Dairy Allergy, Vegetarian Diet, Vegan Diet",
                "ic_oatmeal_berries", Arrays.asList("Dairy Allergy", "Animal Product")));

        receitas.add(new Receita("Pancakes",
                "Fluffy pancakes.",
                "Flour, Eggs, Milk, Sugar",
                "Cook batter on griddle.",
                "Animal Product, Celiac Disease / Gluten Sensitivity, Egg Allergy, Lactose Intolerance, Dairy Allergy, Vegetarian Diet, Vegan Diet",
                "ic_pancakes", Arrays.asList("Gluten", "Egg Allergy", "Dairy Allergy", "Animal Product")));

        receitas.add(new Receita("Waffles",
                "Crispy waffles.",
                "Flour, Eggs, Milk, Butter",
                "Cook in waffle iron.",
                "Animal Product, Celiac Disease / Gluten Sensitivity, Egg Allergy, Lactose Intolerance, Dairy Allergy, Vegetarian Diet, Vegan Diet",
                "ic_waffles", Arrays.asList("Gluten", "Egg Allergy", "Dairy Allergy", "Animal Product")));

        receitas.add(new Receita("French Toast",
                "Golden French toast.",
                "Bread, Eggs, Milk, Cinnamon",
                "Cook soaked bread in pan.",
                "Animal Product, Celiac Disease / Gluten Sensitivity, Egg Allergy, Lactose Intolerance, Dairy Allergy, Vegetarian Diet, Vegan Diet",
                "ic_french_toast", Arrays.asList("Gluten", "Egg Allergy", "Dairy Allergy", "Animal Product")));

        receitas.add(new Receita("Caesar Salad",
                "Classic Caesar salad.",
                "Lettuce, Croutons, Parmesan, Dressing",
                "Mix ingredients.",
                "Animal Product, Celiac Disease / Gluten Sensitivity, Lactose Intolerance, Dairy Allergy, Vegetarian Diet, Vegan Diet",
                "ic_caesar_salad", Arrays.asList("Gluten", "Dairy Allergy", "Animal Product")));

        receitas.add(new Receita("Caprese Salad",
                "Italian tomato and mozzarella salad.",
                "Tomato, Mozzarella, Basil, Balsamic",
                "Assemble ingredients.",
                "Animal Product, Lactose Intolerance, Dairy Allergy, Vegetarian Diet, Vegan Diet",
                "ic_caprese_salad", Arrays.asList("Dairy Allergy", "Animal Product")));

        receitas.add(new Receita("Bruschetta",
                "Toasted tomato bread.",
                "Bread, Tomato, Garlic, Basil",
                "Top toasted bread with mixture.",
                "Celiac Disease / Gluten Sensitivity, Vegan Diet, Vegetarian Diet",
                "ic_bruschetta", Arrays.asList("Gluten")));

        receitas.add(new Receita("Minestrone Soup",
                "Vegetable soup with pasta.",
                "Vegetables, Beans, Pasta, Tomato Broth",
                "Cook ingredients.",
                "Celiac Disease / Gluten Sensitivity, Vegan Diet, Vegetarian Diet",
                "ic_minestrone_soup", Arrays.asList("Gluten")));

        receitas.add(new Receita("Fettuccine Alfredo",
                "Creamy Alfredo pasta.",
                "Fettuccine, Butter, Cream, Parmesan",
                "Cook pasta and sauce.",
                "Animal Product, Celiac Disease / Gluten Sensitivity, Lactose Intolerance, Dairy Allergy, Vegetarian Diet, Vegan Diet",
                "ic_fettuccine_alfredo", Arrays.asList("Gluten", "Dairy Allergy", "Animal Product")));

        receitas.add(new Receita("Lasagna",
                "Italian baked lasagna.",
                "Lasagna Noodles, Cheese, Meat Sauce",
                "Assemble and bake.",
                "Animal Product, Celiac Disease / Gluten Sensitivity, Lactose Intolerance, Dairy Allergy, Vegetarian Diet, Vegan Diet",
                "ic_lasagna", Arrays.asList("Gluten", "Dairy Allergy", "Animal Product")));

        return receitas;
    }
}
