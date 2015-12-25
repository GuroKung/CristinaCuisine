package rules;

import org.easyrules.annotation.Action;
import org.easyrules.annotation.Condition;
import org.easyrules.annotation.Rule;
import model.Recipe;

import java.util.ArrayList;

@Rule(name = "Recommend rule", description = "Recommend recipes")
public class RecommendRule {
    private ArrayList<Recipe> recipes;
    private ArrayList<ArrayList<String>> missIngredientsAll;
    private ArrayList<String> ingredients;
    private ArrayList<Recipe> recommended;
    private ArrayList<Recipe> subrecommended;
    private ArrayList<ArrayList<String>> missIngredientsRec;
    private double needCal;

    @Condition
    public boolean checkInput() {
        //The rule should be applied only if the user's response is yes (duke friend)
        recommended = new ArrayList<Recipe>();
        subrecommended = new ArrayList<Recipe>();
        missIngredientsRec = new ArrayList<ArrayList<String>>();
        missIngredientsAll = new ArrayList<ArrayList<String>>();
        return (recipes!=null)&(ingredients!=null);
    }

    @Action(order = 1)
    public void initialRule(){
        for( int i=0;i<recipes.size();i++ ) {
            ArrayList<String> missIngredients = recipes.get(i).getMissIngredient(ingredients);
            missIngredientsAll.add(missIngredients);
        }
    }
    @Action(order = 2)
    public void primaryRecommend() {
        for( int i=0;i<recipes.size();i++ ) {
            if (missIngredientsAll.get(i).size() == 0) {
                recommended.add(recipes.get(i));
            }
            else if (missIngredientsAll.get(i).size() <= 2) {
                subrecommended.add(recipes.get(i));
                missIngredientsRec.add(missIngredientsAll.get(i));
            }
        }
    }
    @Action(order = 3)
    public void secondaryRecommend() {
        for( int i=0;i<recommended.size();i++ ) {
            if( recommended.get(i).getCal()>needCal ) {
                recommended.remove(i);
                missIngredientsRec.remove(i);
                i--;
            }
        }
        for( int i=0;i<subrecommended.size();i++ ) {
            if( subrecommended.get(i).getCal()>needCal ) {
                subrecommended.remove(i);
                missIngredientsRec.remove(i);
                i--;
            }
        }
    }

    public ArrayList<Recipe> getRecommended() {
        return this.recommended;
    }
    public ArrayList<Recipe> getSubRecommended() {
        return this.subrecommended;
    }
    public ArrayList<ArrayList<String>> getMissIngredientsRec() {
        return this.missIngredientsRec;
    }
    public void setInput(ArrayList<Recipe> recipes, ArrayList<String> ingredients, double needCal){
        this.recipes = recipes;
        this.ingredients = ingredients;
        this.needCal = needCal;
    }
}
