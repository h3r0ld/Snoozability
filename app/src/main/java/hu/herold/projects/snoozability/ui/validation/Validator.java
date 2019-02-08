package hu.herold.projects.snoozability.ui.validation;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;

import javax.inject.Inject;

import hu.herold.projects.snoozability.R;
import hu.herold.projects.snoozability.SnoozabilityApplication;

public class Validator {

    @Inject
    Context context;

    public Validator() {
        SnoozabilityApplication.injector.inject(this);
    }

    public boolean validateEditText(TextInputLayout textInputLayout, TextInputEditText textInputEditText) {
        String editTextValue = textInputEditText.getText() == null ? "" : textInputEditText.getText().toString();

        if (editTextValue.isEmpty()) {
            textInputLayout.setError(context.getResources().getText(R.string.required_field));
            return false;
        } else {
            textInputLayout.setError(null);
            return true;
        }
    }

    public boolean validateEditTextMaxValue(TextInputLayout textInputLayout, TextInputEditText textInputEditText, int maxValue) {
        String editTextValue = textInputEditText.getText() == null ? "" : textInputEditText.getText().toString();

        int number = Integer.parseInt(editTextValue);

        if (number > maxValue) {
            textInputLayout.setError(String.format(context.getString(R.string.max_value_validation_error), maxValue));
            return false;
        } else {
            textInputLayout.setError(null);
            return true;
        }
    }
}
