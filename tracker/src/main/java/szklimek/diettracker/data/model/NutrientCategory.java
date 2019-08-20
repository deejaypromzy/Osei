package szklimek.diettracker.data.model;

/**
 * Class representing nutrient as a category
 */

public class NutrientCategory extends Nutrient {

    private double minValue;
    private double maxValue;
    private boolean isChecked;
    private boolean isMinimumValueChecked;
    private boolean isMaximumValueChecked;

    public NutrientCategory(Nutrient nutrient){
        super(nutrient.getName());
    }

    public double getMinValue() {
        return minValue;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isMinimumValueChecked() {
        return isMinimumValueChecked;
    }

    public void setMinimumValueChecked(boolean minimumValueChecked) {
        isMinimumValueChecked = minimumValueChecked;
    }

    public boolean isMaximumValueChecked() {
        return isMaximumValueChecked;
    }

    public void setMaximumValueChecked(boolean maximumValueChecked) {
        isMaximumValueChecked = maximumValueChecked;
    }
}
