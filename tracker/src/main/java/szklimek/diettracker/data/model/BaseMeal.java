package szklimek.diettracker.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Class representing meal, used to set basic properties of meal
 */

public class BaseMeal implements Parcelable {

    public static final int TYPE_BREAKFAST = 0;
    public static final int TYPE_ND_BREAKFAST = 1;
    public static final int TYPE_LUNCH = 2;
    public static final int TYPE_DINNER = 3;
    public static final int TYPE_SUPPER = 4;
    public static final int TYPE_SNACK = 5;
    public static final int TYPE_BRUNCH = 6;
    public static final int TYPE_CHEAT_MEAL = 7;
    public static final int TYPE_OTHER = 8;

    // BaseMeal number
    public static final int MEAL_1 = 1;
    public static final int MEAL_2 = 2;
    public static final int MEAL_3 = 3;
    public static final int MEAL_4 = 4;
    public static final int MEAL_5 = 5;
    public static final int MEAL_6 = 6;
    public static final int MEAL_7 = 7;
    public static final int MEAL_8 = 8;
    public static final int MEAL_9 = 9;
    public static final int MEAL_PLUS = 10;

    private int mNumber;
    private String mName;
    private int mNameId;
    private int mTimeMinutes;
    private int mTimeHour;
    private int mPercentDailyLimit;
    private int mCaloriesLimit;

    // Constructor
    public BaseMeal(int number, int nameId, int hour, int minutes, int percentDailyLimit) {
        mNumber = number;
        mNameId = nameId;
        mTimeMinutes = minutes;
        mTimeHour = hour;
        mPercentDailyLimit = percentDailyLimit;
    }

    public BaseMeal(int number, int nameId, String name,
                    int hour, int minutes,
                    int percentDailyLimit, int caloriesLimit) {
        mNumber = number;
        mNameId = nameId;
        mName = name;
        mTimeMinutes = minutes;
        mTimeHour = hour;
        mPercentDailyLimit = percentDailyLimit;
        mCaloriesLimit = caloriesLimit;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public int getNumber() {
        return mNumber;
    }

    public void setNumber(int number) {
        this.mNumber = number;
    }

    public void setNameId(int nameId) {
        this.mNameId = nameId;
    }

    public int getNameId() {
        return mNameId;
    }

    public void setTimeMinutes(int timeMinutes) {
        this.mTimeMinutes = timeMinutes;
    }

    public int getTimeMinutes() {
        return mTimeMinutes;
    }

    public void setTimeHour(int timeHour) {
        this.mTimeHour = timeHour;
    }

    public int getTimeHour() {
        return mTimeHour;
    }

    public void setPercentDailyLimit(int percentDailyLimit) {
        this.mPercentDailyLimit = percentDailyLimit;
    }

    public int getCaloriesLimit(){
        return mCaloriesLimit;
    }

    public void setCaloriesLimit(int totalCaloriesLimit) {
        mCaloriesLimit = totalCaloriesLimit * mPercentDailyLimit / 100;
    }

    public int getPercentDailyLimit() {
        return mPercentDailyLimit;
    }

    public int getMealCaloriesLimit(int caloriesLimit) {
        return mPercentDailyLimit * caloriesLimit / 100;
    }

    // Implementation of Parcelable interface
    private BaseMeal(Parcel in) {
        mNumber = in.readInt();
        mName = in.readString();
        mNameId = in.readInt();
        mTimeMinutes = in.readInt();
        mTimeHour = in.readInt();
        mPercentDailyLimit = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(mNumber);
        parcel.writeString(mName);
        parcel.writeInt(mNameId);
        parcel.writeInt(mTimeMinutes);
        parcel.writeInt(mTimeHour);
        parcel.writeInt(mPercentDailyLimit);
    }

    public static final Parcelable.Creator<BaseMeal> CREATOR = new Parcelable.Creator<BaseMeal>() {
        public BaseMeal createFromParcel(Parcel in) {
            return new BaseMeal(in);
        }

        public BaseMeal[] newArray(int size) {
            return new BaseMeal[size];
        }
    };

}
