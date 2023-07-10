package com.example.mini_project_02;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
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
import com.example.mini_project_02.db.FavoriteQuotesDbOpenHelper;
import com.example.mini_project_02.models.Quote;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class StartActivity extends AppCompatActivity {
    TextView tvStartActQuote, tvStartActAuthor;
    Button btnStartActPass;
    ToggleButton tbStartActPinUnpin;
    SharedPreferences sharedPreferences;
    ImageView ivStartActIsFavorite;
    boolean isFavorite = false;
    FavoriteQuotesDbOpenHelper db;
    TextView tvStartActId;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        tvStartActQuote = findViewById(R.id.tvStartActQuote);
        tvStartActAuthor = findViewById(R.id.tvStartActAuthor);
        btnStartActPass = findViewById(R.id.btnStartActPass);
        tbStartActPinUnpin = findViewById(R.id.tbStartActPinUnpin);
        ivStartActIsFavorite = findViewById(R.id.ivStartActIsFavorite);
        tvStartActId = findViewById(R.id.tvStartActId);

        db = new FavoriteQuotesDbOpenHelper(this);

        //region Pin | Unpin Quote

        sharedPreferences = getSharedPreferences("pinned-pinnedQuote", MODE_PRIVATE);

        String pinnedQuote = sharedPreferences.getString("pinnedQuote", null);

        if (pinnedQuote == null) {
            getRandomQuote();
        } else {
            String author = sharedPreferences.getString("author", null);
            String id = sharedPreferences.getString("id", null);

            tvStartActQuote.setText(pinnedQuote);
            tvStartActAuthor.setText(author);
            tvStartActId.setText("#"+id);

            ivStartActIsFavorite.setImageResource(R.drawable.like);

            tbStartActPinUnpin.setChecked(true);
        }

        tbStartActPinUnpin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String quote = null;
                String author = null;
                String id  = null;

                if (isChecked) {
                    quote = tvStartActQuote.getText().toString();
                    author = tvStartActAuthor.getText().toString();
                    id = tvStartActId.getText().toString().substring(1);

                    if(!db.isFavorite(Integer.parseInt(id))) {
                        db.add(new Quote(Integer.parseInt(id), quote, author));
                        ivStartActIsFavorite.performClick();
                    }

                } else {
                    getRandomQuote();
                    System.out.println("unpin");
                }

                editor.putString("pinnedQuote", quote);
                editor.putString("author", author);
                editor.putString("id", id);

                editor.commit();
            }
        });

        //endregion

        //region Like | Dislike Quote

        db = new FavoriteQuotesDbOpenHelper(this);

        ivStartActIsFavorite.setOnClickListener(v -> {
            int id = Integer.parseInt(tvStartActId.getText().toString().substring(1));

            if (isFavorite) {
                isFavorite=false;
                ivStartActIsFavorite.setImageResource(R.drawable.dislike);

                if (tbStartActPinUnpin.isChecked())
                    tbStartActPinUnpin.setChecked(false);

                db.delete(id);

            } else {
                isFavorite=true;

                ivStartActIsFavorite.setImageResource(R.drawable.like);

                String quote = tvStartActQuote.getText().toString();
                String author = tvStartActAuthor.getText().toString();

                if(!db.isFavorite(id))
                    db.add(new Quote(id, quote, author));
            }

            //region ToDelete

            ArrayList<Quote> quotes = db.getAll();
            for (Quote quote : quotes) {
                Log.e("SQLite", quote.toString());
            }

            //endregion
        });

        //endregion

        btnStartActPass.setOnClickListener(v -> {
            finish();
        });
    }

    private void getRandomQuote() {
        System.out.println("unpin");

        RequestQueue queue = Volley.newRequestQueue(this);
//        String url = "https://dummyjson.com/quotes/random";

        //region ToDo: Delete

        int randomNumber = ThreadLocalRandom.current().nextInt(1, 8 + 1);
        String url = String.format("https://dummyjson.com/quotes/%d", randomNumber);

        //endregion

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int id = response.getInt("id");
                            String quote = response.getString("quote");
                            String author = response.getString("author");

                            if (db.isFavorite(id)) {
                                ivStartActIsFavorite.setImageResource(R.drawable.like);
                                isFavorite = true;
                            }

                            else {
                                ivStartActIsFavorite.setImageResource(R.drawable.dislike);
                                isFavorite = false;
                            }

                            tvStartActId.setText(String.format("#%d", id));
                            tvStartActQuote.setText(quote);
                            tvStartActAuthor.setText(author);
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