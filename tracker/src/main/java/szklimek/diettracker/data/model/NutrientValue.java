package szklimek.diettracker.data.model;

/**
 * Class representing nutrient in product
 */

public class NutrientValue {

    private double valuePer100g;

    NutrientValue(double valuePer100g){
        this.valuePer100g = valuePer100g;
    }

    public void setValuePer100g(double valuePer100g) {
        this.valuePer100g = valuePer100g;
    }

    public double getValuePer100g() {
        return valuePer100g;
    }
}
