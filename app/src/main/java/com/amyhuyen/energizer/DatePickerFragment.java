package com.amyhuyen.energizer;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceStaet) {
        // use the curent date as teh default date in the picker
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // create a new instance of DatePi.kerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.US);
        String strDate = simpleDateFormat.format(calendar.getTime());

        if (getTag().equals("Start Date Picker")){
            EditText etStartDate = getActivity().findViewById(R.id.etStartDate);
            String startDateString = getContext().getString(R.string.start_date_colon, strDate);
            etStartDate.setText(startDateString); //
        } else if (getTag().equals("End Date Picker")){
            EditText etEndDate = getActivity().findViewById(R.id.etEndDate);
            String endDateString = getContext().getString(R.string.end_date_colon, strDate);
            etEndDate.setText(endDateString);
        }
    }
}
