package com.example.fthangoutv03;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomInput extends LinearLayout {

    private TextView label;
    private EditText editText;

    public CustomInput(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.custom_input, this, true);
        label = findViewById(R.id.label);
        editText = findViewById(R.id.TextInput);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CustomInput);
        String labelText = attributes.getString(R.styleable.CustomInput_labelText);
        if (labelText == null) {
            labelText = "Label";
        }
        label.setText(labelText);

        String hintText = attributes.getString(R.styleable.CustomInput_hintText);
        if (hintText == null) {
            hintText = "";
        }
        editText.setHint(hintText);

        attributes.recycle();
    }

    public String getInput() {
        return editText.getText().toString();
    }
    public void setInput(String text) {
        editText.setText(text);
    }
}
