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
      return ok(index.render(allIngredients));
  }

  public static Result submit() throws Exception{
    ArrayList<String> userInput = new ArrayList<String>();

    // Get data
    DynamicForm dynamicForm = new DynamicForm().bindFromRequest();
    String gender = dynamicForm.get("gender");
    System.out.println("Gender is "+ gender);
    String weight = dynamicForm.get("weight");
    String height = dynamicForm.get("height");
    String age = dynamicForm.get("age");
    String actFac = dynamicForm.get("actFac");
    String ingredients = dynamicForm.get("ingredients");

    String[] inputString = ingredients.split(",");
    for( int i=0;i<inputString.length;i++ ) {
        userInput.add(inputString[i]);
    }

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
    recRule.setInput(allRecipes,userInput,calNeed);
    rulesEngine.registerRule(recRule);
    rulesEngine.unregisterRule(tdeeRule);
    rulesEngine.fireRules();
    ArrayList<Recipe> recommended = recRule.getRecommended();
    ArrayList<Recipe> subrecommended = recRule.getSubRecommended();
    String show = bmiRule.getResult() + "\n" + tdeeRule.getResult();
    return ok(resultPage.render(show, recommended, subrecommended));
}

public static ArrayList<Recipe> getAllRecipe() {
    ArrayList<Recipe> allRecipe = new ArrayList<Recipe>();
    String[] recipes = {
            "PadKapraoMoo#Pork, Basil, Garlic, Chilli#340#http://thaifoodandtravel.com/blog/wp-content/uploads/2012/02/basil-pork-01.jpg",
            "PadKapraoKoong#Shrimp, Basil, Garlic, Chilli#300#http://www.thai-thaifood.com/bilder/1985c.jpg",
            "LabMoo#Pork, Toastedrice, Coriander, Shallot, Culantro, Chilli#130#http://i.ytimg.com/vi/6PHLtctOnr8/maxresdefault.jpg",
            "LabKai#Chicken, Toastedrice, Coriander, Shallot, Culantro, Chilli#125#http://www.bloggang.com/data/narellan/picture/1389326761.jpg",
            "NamTokMoo#Pork, Toastedrice, Coriander, Shallot, Culantro, Chilli#165#https://s3-ap-southeast-1.amazonaws.com/photo.wongnai.com/photos/2015/03/18/1afffd3e441a450da74a0af3276dd794.jpg",
            "SomTumThai#Papaya, Tomato, Lime, Cowpea, Peanut#55#http://urban-realtor.com/wp-content/uploads/2015/07/%E0%B8%AA%E0%B9%89%E0%B8%A1%E0%B8%95%E0%B8%B3%E0%B9%84%E0%B8%97%E0%B8%A2.jpg",
            "GrilledChicken#Chicken#165#http://webboard.yenta4.com/uploads/2015/03/31/222909-attachment.jpg",
            "StirFriedPork#Pork, Garlic#245#http://cms.toptenthailand.net/file/picture/20140905154746646/20140905154746646.jpg",
            "FriedMorningGlory#Morningglory, Garlic, Chilli#210#http://s3.amazonaws.com/foodspotting-ec2/reviews/1096563/thumb_600.jpg?1322904402",
            "BoiledEgg#Egg#75#http://p4.isanook.com/me/0/ud/0/4445/scac.jpg",
            "KhaiToon#Egg#75#http://www.9leang.com/wp-content/uploads/2010/05/1205586800.jpg?844e19",
            "FriedEgg#Egg#165#http://thumbs.dreamstime.com/z/traditional-fried-egg-cracked-shell-whole-egg-1970504.jpg",
            "Omelette#Egg#200#http://images.bigoven.com/image/upload/v1419434325/sharp-cheddar-omelet.jpg",
            "YumWoonSen#Glassnoodle, Pork, Lime, Onion, Tomato, Shallot, Celery, Shrimp#120#http://esthete.in.th/sites/default/files/1101.jpg",
            "KhaoRadNaKai#Rice, Chicken, Flour, Carrot, Springonion#400#http://www.manager.co.th/asp-bin/Image.aspx?ID=2193900",
            "PadFakthongWithEgg#Pumpkin, Egg#255#http://www.poomillust.com/wp-content/uploads/2012/05/11_12.jpg",
            "FriedCabbage#Cabbage#230#http://f.ptcdn.info/455/011/000/1382946947-DSC04873-o.jpg",
            "YumCannedFish#Cannedfish, Shallot, Lime, Chilli, Springonion#55#http://i1152.photobucket.com/albums/p494/totikky/E220E330E1B0E250E320E010E230E300E1B0E4B0E2D0E070E410E0B0E1A0E460_zpsde101db2.jpg",
            "PadKuichaiWithLiver#Chiveflower, Liver#210#http://www.bloggang.com/data/golf2poom/picture/1339680312.jpg",
            "FriedLettuceWithPork#Lettuce, Pork, Garlic#230#http://www.bloggang.com/data/khunmai/picture/1311241449.jpg",
            "FriedVegetables#Lettuce, Garlic, Mushroom, Carrot, Kale, Babycorn, Broccoli, Cauliflower#210#http://drinkingalkalinewater.com/wp-content/uploads/%E0%B8%9C%E0%B8%B1%E0%B8%94%E0%B8%9C%E0%B8%B1%E0%B8%81%E0%B8%A3%E0%B8%A7%E0%B8%A1%E0%B8%A1%E0%B8%B4%E0%B8%95%E0%B8%A3.jpg",
            "YumBoiledEgg#Egg, Shallot, Lime, Chilli#105#https://i.ytimg.com/vi/piPHv_RF4Bw/hqdefault.jpg",
            "KaiPadKhing#Chicken, Mushroom, Garlic, Ginger, Chilli, Springonion#210#https://i.ytimg.com/vi/MJZpAwgQbSw/maxresdefault.jpg",
            "TomYumKoong#Shrimp, Kaffir, Mushroom, Chilli, Lemongrass, Coriander, Shallot, Coconutmilk, Galangal, Lime#65#http://www.engtest.net/2010/user_images/3354.jpg",
            "TomYumHed#Kaffir, Mushroom, Chilli, Lemongrass, Coriander, Shallot, Galangal, Lime, Tomato#30#http://images.thaiza.com/224/224_201505221525451..jpg",
            "TomKhaKai#Chicken, Kaffir, Mushroom, Chilli, Lemongrass, Coriander, Shallot, Coconutmilk, Galangal, Lime, Tomato#210#http://www.bloggang.com/data/japansainlook/picture/1287494969.jpg",
            "FriedRicewithSeafood#Rice, Chilli, Lemongrass, Lime, Shrimp, Squid, Kaffir#400#https://s3-ap-southeast-1.amazonaws.com/photo.wongnai.com/photos/2014/07/17/281b041a188a492ca1821a17ba158ddd.jpg",
            "TomChubChai#Porkrib, Cabbage, Lettuce, Kale, Radish, ChineseCabbage, Garlic#180#http://2.bp.blogspot.com/-mpHSdP7pZYM/VEeQ8n7_DnI/AAAAAAAAAuE/8v8LpaeE1E8/s1600/2lwjY.JPG",
            "TomKlongFish#Tamarind, Chilli, Basil, Kaffir, Galangal, Lemongrass, Tomato, Shallot, Fish#60#http://pim.in.th/images/all-side-dish-fish/tom-klong-pladuk-yang/tom-klong-pladuk-yang-19.JPG",
            "FriedFishWithCrab#Rice, Egg, Crab, Springonion#610#http://photo.wongnai.com/photos/2014/07/17/281b041a188a492ca1821a17ba158ddd.jpg",
            "GlassnoodlePadThai#Glassnoodle, Shrimp, Peanut, Egg, Chiveflower, Beansprout, Turnip, Lime#520#https://www.goodfoodgoodlife.in.th/%E0%B9%80%E0%B8%A3%E0%B8%B7%E0%B9%88%E0%B8%AD%E0%B8%87%E0%B8%99%E0%B9%88%E0%B8%B2%E0%B8%A3%E0%B8%B9%E0%B9%89/%E0%B9%84%E0%B8%AD%E0%B9%80%E0%B8%94%E0%B8%B5%E0%B8%A2%E0%B8%AA%E0%B8%B9%E0%B8%95%E0%B8%A3%E0%B8%AD%E0%B8%B2%E0%B8%AB%E0%B8%B2%E0%B8%A3/FileUpload/download/MediaFile_351phat-thai_460x300.jpg"
    };
    for (int i = 0; i < recipes.length; i++) {
        String line = recipes[i];
        String[] splited = line.split("#");
        String name = splited[0];
        String[] ing = (splited[1]).split(", ");
        String calString = splited[2];
        double cal = Double.parseDouble(calString);
        String img = splited[3];
        allRecipe.add(new Recipe(name, ing, cal, img));
        for( int j=0;j<ing.length;j++ ) {
            if(!allIngredients.contains(ing[j])) {
                allIngredients.add(ing[j]);
            }
        }
    }
    return allRecipe;
}

}
