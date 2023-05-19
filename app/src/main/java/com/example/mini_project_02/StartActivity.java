package com.example.mini_project_02;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class StartActivity extends AppCompatActivity {
    TextView tvStartActQuote, tvStartActAuthor;
    Button btnStartActPass;
    ToggleButton tbStartActPinUnpin;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        tvStartActQuote = findViewById(R.id.tvStartActQuote);
        tvStartActAuthor = findViewById(R.id.tvStartActAuthor);
        btnStartActPass = findViewById(R.id.btnStartActPass);
        tbStartActPinUnpin = findViewById(R.id.tbStartActPinUnpin);

        sharedPreferences = getSharedPreferences("pinned-quote", MODE_PRIVATE);

        String quote = sharedPreferences.getString("quote", null);

        if (quote == null) {
            getRandomQuote();
        } else {
            String author = sharedPreferences.getString("author", null);

            tvStartActQuote.setText(quote);
            tvStartActAuthor.setText(author);

            tbStartActPinUnpin.setChecked(true);
        }

        tbStartActPinUnpin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String quote = null;
                String author = null;

                if (isChecked) {
                    quote = tvStartActQuote.getText().toString();
                    author = tvStartActAuthor.getText().toString();
                } else {
                    getRandomQuote();
                }

                editor.putString("quote", quote);
                editor.putString("author", author);

                editor.commit();
            }
        });

        btnStartActPass.setOnClickListener(v -> {
            finish();
        });
    }

    private void getRandomQuote() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://dummyjson.com/quotes/random";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            tvStartActQuote.setText(response.getString("quote"));
                            tvStartActAuthor.setText(response.getString("author"));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        queue.add(jsonObjectRequest);
    }
}