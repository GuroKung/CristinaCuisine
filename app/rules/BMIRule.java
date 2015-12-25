package rules;

import org.easyrules.annotation.Action;
import org.easyrules.annotation.Condition;
import org.easyrules.annotation.Rule;

@Rule(name = "BMI rule", description = "check BMI")
public class BMIRule {

    /**
     * The user input which represents the data that the rule will operate on.
     */
    private double height;
    private double weight;
    private double bmi;
    private double need;
    private String result;

    @Condition
    public boolean checkInput() {
        //The rule should be applied only if the user's response is yes (duke friend)
        return (height > 0) & (weight > 0);
    }

    @Action(order = 1)
    public void checkBMI() throws Exception {
        this.height /= 100.0;
        bmi = weight / (this.height * this.height);
    }

    @Action(order = 2)
    public void checkUnderweight() throws Exception {
        //if (Double.compare(bmi, 18.5) < 0)
        if (18.5 > bmi) {
            result = String.format("BMI =  %.2f (Underweight)", bmi);
            need = 200;
        }
        //result = "Your BMI = "+bmi+" and you have an underweight";
    }

    @Action(order = 3)
    public void checkHealthyweight() throws Exception {
        if( 18.5 < bmi & bmi < 25 ) {
            result = String.format("BMI =  %.2f (Healthy weight)", bmi);
        }
        //result = "Your BMI = "+bmi+" and you have a normal weight";
    }

    @Action(order = 4)
    public void checkOverweight() throws Exception {
        if ( 25 < bmi & bmi < 29.99 ) {
            result = String.format("BMI =  %.2f (Overweight)", bmi);
            need = -100;
        }
        //result = "Your BMI = "+bmi+" and you have an overweight";
    }

    @Action(order = 5)
    public void checkObese() throws Exception {
        if ( 30 < bmi & bmi < 34.99 ) {
            result = String.format("BMI =  %.2f (Obese)", bmi);
            need = -200;
        }//result = "Your BMI = "+bmi+" and you have an overweight";
    }

    @Action(order = 6)
    public void checkSeverelyObese() throws Exception {
        if ( 35 < bmi & bmi < 39.99 ) {
            result = String.format("BMI =  %.2f (Severely obese)", bmi);
            need = -300;
        }//result = "Your BMI = "+bmi+" and you have an overweight";
    }

    @Action(order = 7)
    public void checkMorbidlyObese() throws Exception {
        if ( 40 < bmi ) {
            result = String.format("BMI =  %.2f (Morbidly obese)", bmi);
            need = -400;
        }//result = "Your BMI = "+bmi+" and you have an overweight";
    }
    public void setInput(double height, double weight) {
        this.height = height;
        this.weight = weight;
    }
    public String getResult() {
        return result;
    }
    public double getNeed() { return this.need; }
    public double getBMI() { return bmi; }
}
