package controllers;

import play.*;
import play.data.*;
import play.mvc.*;
import rules.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import model.Recipe;
import views.html.*;
import org.easyrules.api.RulesEngine;
import static org.easyrules.core.RulesEngineBuilder.aNewRulesEngine;

public class Application extends Controller {
  public static ArrayList<Recipe> allRecipes;
  public static ArrayList<String> allIngredients;

  public static Result index() {
      allIngredients = new ArrayList<String>();
      allRecipes = getAllRecipe();
      return ok(index.render(""));
  }

  public static Result submit() throws Exception{
      ArrayList<String> s = new ArrayList<String>();
      s.add("Egg");

      // Get data
      DynamicForm dynamicForm = new DynamicForm().bindFromRequest();
      String gender = dynamicForm.get("gender");
      System.out.println("Gender is "+ gender);
      String weight = dynamicForm.get("weight");
      String height = dynamicForm.get("height");
      String age = dynamicForm.get("age");
      String actFac = dynamicForm.get("actFac");
      String ingredients = dynamicForm.get("ingredients");
      double heightDouble = Double.parseDouble(height);
      double weightDouble = Double.parseDouble(weight);
      double aFactorDouble = Double.parseDouble(actFac);
      int ageInt = Integer.parseInt(age);

      // see ingredients
      System.out.println(ingredients);

      //Define Rule
      BMIRule bmiRule = new BMIRule();
      TDEERule tdeeRule = new TDEERule();
      RecommendRule recRule = new RecommendRule();
      bmiRule.setInput(heightDouble, weightDouble);

      //fire to rule
      RulesEngine rulesEngine = aNewRulesEngine().withSilentMode(true).build();
      rulesEngine.registerRule(bmiRule);
      rulesEngine.fireRules();
      double need =  bmiRule.getNeed();
      tdeeRule.setInput(weightDouble, heightDouble, gender, ageInt, aFactorDouble, need);
      rulesEngine.registerRule(tdeeRule);
      rulesEngine.unregisterRule(bmiRule);
      rulesEngine.fireRules();
      double calNeed = tdeeRule.getTDEEperMeal();
      recRule.setInput(allRecipes,s,calNeed);
      rulesEngine.registerRule(recRule);
      rulesEngine.unregisterRule(tdeeRule);
      rulesEngine.fireRules();
      ArrayList<Recipe> rec = recRule.getRecommended();
      String show = bmiRule.getResult() + "\n" + tdeeRule.getResult();
      return ok(resultPage.render(show, calNeed));
  }

  public static ArrayList<Recipe> getAllRecipe() {
    ArrayList<Recipe> allRecipe = new ArrayList<Recipe>();
    String[] recipes = {
            "PadKapraoMoo#Pork, Basil, Garlic, Chilli#340",
            "PadKapraoKoong#Shrimp, Basil, Garlic, Chilli#300",
            "LabMoo#Pork, Toastedrice, Coriander, Shallot, Culantro, Chilli#130",
            "LabKai#Chicken, Toastedrice, Coriander, Shallot, Culantro, Chilli#125",
            "NamTokMoo#Pork, Toastedrice, Coriander, Shallot, Culantro, Chilli#165",
            "SomTumThai#Papaya, Tomato, Lime, Cowpea, Peanut#55",
            "GrilledChicken#Chicken#165",
            "StirFriedPork#Pork, Garlic#245",
            "FriedMorningGlory#Morningglory, Garlic, Chilli#210",
            "BoiledEgg#Egg#75",
            "KhaiToon#Egg#75",
            "FriedEgg#Egg#165",
            "Omelette#Egg#200",
            "YumWoonSen#Glassnoodle, Pork, Lime, Onion, Tomato, Shallot, Celery, Shrimp#120",
            "KhaoRadNaKai#Rice, Chicken, Flour, Carrot, Springonion#400",
            "PadFakthongWithEgg#Pumpkin, Egg#255",
            "FriedCabbage#Cabbage#230",
            "YumCannedFish#Cannedfish, Shallot, Lime, Chilli, Springonion#55",
            "PadKuichaiWithLiver#Chiveflower, Liver#210",
            "FriedLettuceWithPork#Lettuce, Pork, Garlic#230",
            "FriedVegetables#Lettuce, Garlic, Mushroom, Carrot, Kale, Babycorn, Broccoli, Cauliflower#210",
            "YumBoiledEgg#Egg, Shallot, Lime, Chilli#105",
            "KaiPadKhing#Chicken, Mushroom, Garlic, Ginger, Chilli, Springonion#210",
            "TomYumKoong#Shrimp, Kaffir, Mushroom, Chilli, Lemongrass, Coriander, Shallot, Coconutmilk, Galangal, Lime#65",
            "TomYumHed#Kaffir, Mushroom, Chilli, Lemongrass, Coriander, Shallot, Galangal, Lime, Tomato#30",
            "TomKhaKai#Chicken, Kaffir, Mushroom, Chilli, Lemongrass, Coriander, Shallot, Coconutmilk, Galangal, Lime, Tomato#210",
            "FriedRicewithSeafood#Rice, Chilli, Lemongrass, Lime, Shrimp, Squid, Kaffir#400",
            "TomChubChai#Porkrib, Cabbage, Lettuce, Kale, Radish, ChineseCabbage, Garlic#180",
            "TomKlongFish#Tamarind, Chilli, Basil, Kaffir, Galangal, Lemongrass, Tomato, Shallot, Fish#60",
            "FriedFishWithCrab#Rice, Egg, Crab, Springonion#610",
            "GlassnoodlePadThai#Glassnoodle, Shrimp, Peanut, Egg, Chiveflower, Beansprout, Turnip, Lime#520"
    };
    for (int i = 0; i < recipes.length; i++) {
        String line = recipes[i];
        String[] splited = line.split("#");
        String name = splited[0];
        String[] ing = (splited[1]).split(", ");
        String calString = splited[2];
        double cal = Double.parseDouble(calString);
        allRecipe.add(new Recipe(name, ing, cal));
        for( int j=0;j<ing.length;j++ ) {
            if(!allIngredients.contains(ing[j])) {
                allIngredients.add(ing[j]);
            }
        }
    }


    return allRecipe;
}

}
