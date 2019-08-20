package szklimek.diettracker.reusablefragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import szklimek.diettracker.R;

import static android.content.DialogInterface.BUTTON_POSITIVE;

/**
 * Fragment which shows dialog with TimePicker
 */

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    /**
     * Called when the user is done setting a new time and the dialog has closed.
     * Contains position of adapter if the dialog was launched to set time on specific
     * item from adapter
     */
    public interface OnTimeSetFromAdapterListener {
        void onTimeSetFromAdapter(TimePicker timePicker, int hour, int minutes, int adapterPosition);
    }

    public static final String HOUR_KEY = "hour";
    public static final String MINUTES_KEY = "minutes";
    public static final String POSITION_KEY = "position";

    public static final String AFTER_HOUR = "after_hour";
    public static final String AFTER_MINUTES = "after_minutes";

    public static final String BEFORE_HOUR = "before_hour";
    public static final String BEFORE_MINUTES = "before_minutes";

    public static final int NOT_SET = -1;
    public static final int POSITION_NONE = -1;

    int mPosition; // position of item in adapter
    int mHour; // current hour of item
    int mMinutes; // current minutes of item

    int mAfterHour; // unable user to set time
    int mAfterMinutes; // earlier than this

    int mBeforeHour; // unable user to set time
    int mBeforeMinutes; // later than this

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Calendar currentTime = Calendar.getInstance();
        mHour = currentTime.get(Calendar.HOUR_OF_DAY);
        mMinutes = currentTime.get(Calendar.MINUTE);
        mPosition = POSITION_NONE;
        mAfterHour = NOT_SET;
        mAfterMinutes = NOT_SET;
        mBeforeHour = NOT_SET;
        mBeforeMinutes = NOT_SET;

        Bundle args = getArguments();
        if (args != null) {
            if (args.containsKey(POSITION_KEY)) mPosition = args.getInt(POSITION_KEY);

            if (args.containsKey(HOUR_KEY)) mHour = args.getInt(HOUR_KEY);
            if (args.containsKey(MINUTES_KEY)) mMinutes = args.getInt(MINUTES_KEY);

            if (args.containsKey(AFTER_HOUR)) mAfterHour = args.getInt(AFTER_HOUR);
            if (args.containsKey(AFTER_MINUTES)) mAfterMinutes = args.getInt(AFTER_MINUTES);

            if (args.containsKey(BEFORE_HOUR)) mBeforeHour = args.getInt(BEFORE_HOUR);
            if (args.containsKey(BEFORE_MINUTES)) mBeforeMinutes = args.getInt(BEFORE_MINUTES);
        }

        // Check if the system use 24h or 12h time format
        boolean is24HourFormat = android.text.format.DateFormat.is24HourFormat(this.getContext());

        return new TimePickerDialog(getContext(), this, mHour, mMinutes, is24HourFormat) {

            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minutes) {
                super.onTimeChanged(view, hourOfDay, minutes);
                Calendar pickedTime = Calendar.getInstance();
                pickedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                pickedTime.set(Calendar.MINUTE, minutes);

                if (isTimeAfterLimit(pickedTime)) getButton(BUTTON_POSITIVE).setEnabled(false);
                else getButton(BUTTON_POSITIVE).setEnabled(true);

                if (isTimeBeforeLimit(pickedTime)) getButton(BUTTON_POSITIVE).setEnabled(false);
                else getButton(BUTTON_POSITIVE).setEnabled(true);

            }
        };
    }

    private boolean isTimeBeforeLimit(Calendar newTime) {
        if (mAfterHour != NOT_SET
                && mAfterMinutes != NOT_SET) {
            Calendar afterTime = Calendar.getInstance();
            afterTime.set(Calendar.HOUR_OF_DAY, mAfterHour);
            afterTime.set(Calendar.MINUTE, mAfterMinutes);
            afterTime.add(Calendar.MINUTE, 15);

            if (newTime.before(afterTime)) return true;
        }
        return false;
    }

    private boolean isTimeAfterLimit(Calendar newTime) {
        if (mBeforeHour != NOT_SET
                && mBeforeMinutes != NOT_SET) {

            Calendar beforeTime = Calendar.getInstance();
            beforeTime.set(Calendar.HOUR_OF_DAY, mBeforeHour);
            beforeTime.set(Calendar.MINUTE, mBeforeMinutes);
            beforeTime.add(Calendar.MINUTE, -15);

            if (newTime.after(beforeTime)) return true;
        }
        return false;
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minutes) {

        Calendar pickedTime = Calendar.getInstance();
        pickedTime.set(Calendar.HOUR_OF_DAY, hour);
        pickedTime.set(Calendar.MINUTE, minutes);

        // Show error: time before previous meal
        if (isTimeAfterLimit(pickedTime)) {
            Toast.makeText(getContext(),
                    R.string.error_time_after, Toast.LENGTH_SHORT).show();
            return;
        }

        // Show error: time after next meal
        if (isTimeBeforeLimit(pickedTime)) {
            Toast.makeText(getContext(),
                    R.string.error_time_before, Toast.LENGTH_SHORT).show();
            return;
        }

        // If Dialog was launched with position, call method connected with adapter position
        if (mPosition != POSITION_NONE) {
            ((OnTimeSetFromAdapterListener) getActivity())
                    .onTimeSetFromAdapter(timePicker, hour, minutes, mPosition);
        } else {
            ((TimePickerDialog.OnTimeSetListener) getActivity())
                    .onTimeSet(timePicker, hour, minutes);
        }

    }
}
