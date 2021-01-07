package com.example.group2sqlitedbapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    SQLiteDatabase db;

    private TextInputLayout etStdntID;
    private TextInputLayout etStdntName;
    private TextInputLayout etStdntProg;
    private Button btAdd;
    private Button btDelete;
    private Button btSearch;
    private Button btView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = openOrCreateDatabase("StudentDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS student(stdnt_id VARCHAR, stdnt_name VARCHAR, stdnt_prog VARCHAR);");

        etStdntID = findViewById(R.id.etStdntID);
        etStdntName = findViewById(R.id.etStdntName);
        etStdntProg = findViewById(R.id.etStdntProg);

        btAdd = findViewById(R.id.btAdd);
        btDelete = findViewById(R.id.btDelete);
        btSearch = findViewById(R.id.btSearch);
        btView = findViewById(R.id.btView);

        btAdd.setOnClickListener(this);
        btDelete.setOnClickListener(this);
        btSearch.setOnClickListener(this);
        btView.setOnClickListener(this);

        etStdntID.requestFocus();
        clearText();
    }

    @Override
    public void onClick(View view) {
        if (view == btAdd) {
            db.execSQL("INSERT INTO student VALUES('" + Objects.requireNonNull(etStdntID.getEditText()).getText().toString().trim() + "', '" + Objects.requireNonNull(etStdntName.getEditText()).getText().toString().trim() + "', '" + etStdntName.getEditText().getText().toString().trim() + "');");
            showMessage("Success", "Record added.");
            clearText();
        }
        else if (view == btDelete) {
            String selectRecord = "SELECT * FROM student WHERE stdnt_id = '" + Objects.requireNonNull(etStdntID.getEditText()).getText().toString().trim() + "';";
            Cursor c = db.rawQuery(selectRecord, null);
            if (c.moveToFirst()) {
                String deleteRecord = "DELETE FROM student WHERE stdnt_id = '" + etStdntID.getEditText().getText().toString().trim() + "';";
                db.execSQL(deleteRecord);
                showMessage("Success", "Record deleted.");
            }
            c.close();
            clearText();
        }
        else if (view == btSearch) {
            String searchRecord = "SELECT * FROM student WHERE stdnt_id = '" + Objects.requireNonNull(etStdntID.getEditText()).getText().toString().trim() + "';";
            Cursor c = db.rawQuery(searchRecord, null);
            StringBuilder buffer = new StringBuilder();
            if (c.moveToFirst()) {
                buffer.append("Name: ").append(c.getString(1)).append("\n");
                buffer.append("Program: ").append(c.getString(2)).append("\n\n");
            }
            c.close();
            showMessage("Student Details", buffer.toString());
        }
        else if (view == btView) {
            String viewRecord = "SELECT * FROM student";
            Cursor c = db.rawQuery(viewRecord, null);
            if (c.getCount() == 0) {
                showMessage("Error", "No record founds");
                return;
            }
            StringBuilder buffer = new StringBuilder();
            while (c.moveToNext()) {
                buffer.append("Id: ").append(c.getString(0)).append("\n");
                buffer.append("Name: ").append(c.getString(1)).append("\n");
                buffer.append("Program: ").append(c.getString(2)).append("\n\n");
            }
            showMessage("Student Details", buffer.toString());
            c.close();
        }
        else {
            throw new IllegalStateException("Unexpected value: " + view.getId());
        }
    }

    private void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    private void clearText() {
        Objects.requireNonNull(etStdntID.getEditText()).setText("");
        Objects.requireNonNull(etStdntName.getEditText()).setText("");
        Objects.requireNonNull(etStdntProg.getEditText()).setText("");
    }
}