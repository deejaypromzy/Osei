package szklimek.diettracker.data.local;

import szklimek.diettracker.data.model.Diet;

/**
 * Contract for use of data in SharedPreferences files
 */

public interface SharedPreferencesSource {

    boolean isFirstLaunch();

    int getMaxFoodWeight();

    void saveDiet(Diet diet);

    void getDiet(GetDietCallback callback);

    interface GetDietCallback {

        void onDietLoaded(Diet diet);

        void onDietNotSet();

    }
}
