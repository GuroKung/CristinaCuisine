package rules;

import org.easyrules.annotation.Action;
import org.easyrules.annotation.Condition;
import org.easyrules.annotation.Rule;

@Rule(name = "TDEE rule", description = "calculate TDEE")
public class TDEERule {

    /**
     * The user input which represents the data that the rule will operate on.
     * metric standard.
     */
    private double weight; //kg
    private double height; // cm
    private String gender;
    private int age;
    private double activity_factor;
    private double TDEE;
    private double BMR;
    private String result;
    private double TDEEperMeal;
    private double needCal;

    /*
     	� 1.1 - 1.2 = Sedentary (desk job, and little formal exercise, this will be most of you students)
		� 1.3 - 1.4 = Lightly Active (light daily activity and light exercise 1-3 days a week)
		� 1.5 - 1.6 = Moderately Active (moderately daily Activity & moderate exercise 3-5 days a week)
		� 1.7 - 1.8 = Very Active (physically demanding lifestyle & hard exercise 6-7 days a week)
		� 1.9 - 2.2 = Extremely Active (athletes in endurance training or very hard physical job)
     */

    @Condition
    public boolean checkInput() {
        //The rule should be applied only if the user's response is yes (duke friend)
        return (gender.equalsIgnoreCase("male") | gender.equalsIgnoreCase("female")) & weight > 0 & height > 0 & activity_factor > 0 & age > 0;
    }

    @Action(order = 1)
    public void checkMale() throws Exception {
        if (gender.equalsIgnoreCase("male")){
            BMR = 66 + (13.7 * weight)+ (5 + height) - (6.8*age);
            //BMR = 10*weight + 6.25*height - 5*age + 5;
            TDEE = BMR * activity_factor;
        }
    }

    @Action(order = 2)
    public void checkFemale() throws Exception {
        if (gender.equalsIgnoreCase("female")){
            BMR = 665 + (9.6*weight)+(1.8*height)-(4.7*age);
            //BMR = 10*weight + 6.25*height - 5* age - 161;
            TDEE = BMR * activity_factor;
        }
    }

    @Action(order = 3)
    public void solveTDEEperMeal() throws Exception {
        TDEEperMeal = TDEE/3;
        result = String.format("Your TDEE = %.2f and your need %.2f Kcal per meal",TDEE,TDEEperMeal);
    }

    public void setInput(double weight, double height, String gender, int age, double activity_factor, double needCal) {
        this.weight = weight;
        this.height = height;
        this.gender = gender;
        this.age = age;
        this.activity_factor = activity_factor;
        this.needCal = needCal;
    }
    public String getResult() {
        return result;
    }
    public double getTDEE() { return TDEE; }
    public double getBMR() { return BMR; }
    public double getTDEEperMeal() { return TDEEperMeal; }
}
