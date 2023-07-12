package com.example.mini_project_02;

import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mini_project_02.db.FavoriteQuotesDbOpenHelper;
import com.example.mini_project_02.models.Quote;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class StartActivity extends AppCompatActivity {
    private final static int INVALIDE_ID = -1;

    TextView tvStartActQuote, tvStartActAuthor;
    Button btnStartActShowAllFavQuotes;
    ToggleButton tbStartActPinUnpin;
    SharedPreferences sharedPreferences;
    ImageView ivStartActIsFavorite;
    ImageView ivTheme;
    FavoriteQuotesDbOpenHelper db;
    TextView tvStartActId;
    ConstraintLayout cl;
    int disabledItemIndex = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        String[] colorsName = getResources().getStringArray(R.array.colors_names);
        String[] colorsCode = getResources().getStringArray(R.array.colors_codes);

        tvStartActQuote = findViewById(R.id.tvStartActQuote);
        tvStartActAuthor = findViewById(R.id.tvStartActAuthor);
        btnStartActShowAllFavQuotes = findViewById(R.id.btnStartActShowAllFavQuotes);
        tbStartActPinUnpin = findViewById(R.id.tbStartActPinUnpin);
        ivStartActIsFavorite = findViewById(R.id.ivStartActIsFavorite);
        ivTheme = findViewById(R.id.ivTheme);
        tvStartActId = findViewById(R.id.tvStartActId);
        cl = findViewById(R.id.cl);

        registerForContextMenu(ivTheme);

        ivTheme.setOnClickListener(v -> {
            androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setItems(colorsName, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    cl.setBackgroundColor(Color.parseColor(colorsCode[which]));

                    disabledItemIndex=which;
                }
            });

            AlertDialog dialog = builder.create();
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    ListView listView = ((AlertDialog) dialog).getListView();
                        listView.getChildAt(disabledItemIndex).setEnabled(false);
                }
            });

            dialog.show();
        });


        //region Persistence Objects

        db = new FavoriteQuotesDbOpenHelper(this);
        sharedPreferences = getSharedPreferences("pinned-quote", MODE_PRIVATE);

        //endregion

        //region Pin | Unpin Quote

        int pinnedQuoteId = sharedPreferences.getInt("id", INVALIDE_ID);

        if (pinnedQuoteId == INVALIDE_ID) {
            getRandomQuote();
        } else {
            String quote = sharedPreferences.getString("quote", null);
            String author = sharedPreferences.getString("author", null);

            tvStartActId.setText(String.format("#%d", pinnedQuoteId));
            tvStartActQuote.setText(quote);
            tvStartActAuthor.setText(author);

            ivStartActIsFavorite.setImageResource(db.isFavorite(pinnedQuoteId) ? R.drawable.like : R.drawable.dislike);

            tbStartActPinUnpin.setChecked(true);
        }

        tbStartActPinUnpin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                int pinnedQuoteId = INVALIDE_ID;
                String quote = null;
                String author = null;

                if (isChecked) {
                    pinnedQuoteId = Integer.parseInt(tvStartActId.getText().toString().substring(1));
                    quote = tvStartActQuote.getText().toString();
                    author = tvStartActAuthor.getText().toString();

                    if (!db.isFavorite(pinnedQuoteId)) {
                        ivStartActIsFavorite.setImageResource(R.drawable.like);

                        db.add(new Quote(pinnedQuoteId, quote, author));
                    }
                } else {
//                    getRandomQuote();
                }

                editor.putInt("id", pinnedQuoteId);
                editor.putString("quote", quote);
                editor.putString("author", author);

                editor.commit();
            }
        });

        //endregion

        //region Like | Dislike Quote

        ivStartActIsFavorite.setOnClickListener(v -> {
            int id = Integer.parseInt(tvStartActId.getText().toString().substring(1));
            boolean isFavorite = db.isFavorite(id);

            if (isFavorite) {
                tbStartActPinUnpin.setChecked(false);

                ivStartActIsFavorite.setImageResource(R.drawable.dislike);

                db.delete(id);
            } else {
                ivStartActIsFavorite.setImageResource(R.drawable.like);

                String quote = tvStartActQuote.getText().toString();
                String author = tvStartActAuthor.getText().toString();

                db.add(new Quote(id, quote, author));
            }
        });

        //endregion

        btnStartActShowAllFavQuotes.setOnClickListener(v -> {
            Intent intent = new Intent(this, AllFavoriteQuotesActivity.class);
            startActivity(intent);
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
                            int id = response.getInt("id");
                            String quote = response.getString("quote");
                            String author = response.getString("author");

                            if (db.isFavorite(id))
                                ivStartActIsFavorite.setImageResource(R.drawable.like);
                            else
                                ivStartActIsFavorite.setImageResource(R.drawable.dislike);

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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        String[] colorsName = getResources().getStringArray(R.array.colors_names);

        for (int i = 0; i < colorsName.length; i++) {
            if (i == disabledItemIndex)
                menu.add(0, v.getId(), 0, colorsName[i]).setEnabled(false);
            else
                menu.add(0, v.getId(), 0, colorsName[i]);

        };

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        String[] colorsName = getResources().getStringArray(R.array.colors_names);
        String[] colorsCode = getResources().getStringArray(R.array.colors_codes);
        for (int i = 0; i < colorsName.length; i++) {
            if (Objects.equals(item.getTitle(),colorsName[i])) {
                cl.setBackgroundColor(Color.parseColor(colorsCode[i]));

                disabledItemIndex = i;
            }
        }

        item.setEnabled(false);

        return true;
    }


}