package model;

import java.util.ArrayList;
/**
 * Created by Maxmi on 16/11/2558.
 */
public class Recipe {
    private String name;
    private ArrayList<String> ingredients;
    private double cal;
    private String img;

    public Recipe(String name, String[] ing, double cal, String img){
        this.name = name;
        toArrayList(ing);
        this.cal = cal;
        this.img = img;
    }

    public void toArrayList(String[] ing){
        ingredients = new ArrayList<String>();
        for( int i=0;i<ing.length;i++ ) {
            ingredients.add(ing[i]);
        }
    }
    public int length(){
        return this.ingredients.size();
    }

    public double getCal(){
        return this.cal;
    }
    public String getName(){
        return this.name;
    }
    public String getImage() {
        return this.img;
    }
    public ArrayList<String> getIngredients(){
        return this.ingredients;
    }
    public ArrayList<String> getMissIngredient(ArrayList<String> inputIng){
        ArrayList<String> missIngredients = new ArrayList<String>();
        for( int i=0;i<ingredients.size();i++ ) {
            String check = ingredients.get(i);
            if( !inputIng.contains( check ) ) {
                missIngredients.add( check );
            }
        }
        return missIngredients;
    }


}
