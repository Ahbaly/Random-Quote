package com.example.mini_project_02;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.RecoverySystem;

import com.example.mini_project_02.adapters.FavoriteQuotesAdapter;
import com.example.mini_project_02.db.FavoriteQuotesDbOpenHelper;
import com.example.mini_project_02.models.Quote;

import java.util.ArrayList;

public class AllFavoriteQuotesActivity extends AppCompatActivity {
    RecyclerView rvAllFavQuotesActList;
    FavoriteQuotesDbOpenHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_favorite_quotes);

        rvAllFavQuotesActList = findViewById(R.id.rvAllFavQuotesActList);

        //region Persistence Objects

        db = new FavoriteQuotesDbOpenHelper(this);

        //endregion

        FavoriteQuotesAdapter adapter = new FavoriteQuotesAdapter(db.getAll());
        rvAllFavQuotesActList.setAdapter(adapter);
        rvAllFavQuotesActList.setLayoutManager(new LinearLayoutManager(this));
    }
}