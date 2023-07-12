package com.example.mini_project_02.adapters;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mini_project_02.R;
import com.example.mini_project_02.models.Quote;

import java.util.ArrayList;

public class FavoriteQuotesAdapter extends RecyclerView.Adapter<FavoriteQuotesAdapter.ViewHolder> {
    private ArrayList<Quote> quotes;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvQuote;
        private final TextView tvAuthor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvQuote = itemView.findViewById(R.id.tvQuote);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
        }
    }

    public FavoriteQuotesAdapter(ArrayList<Quote> quotes) {
        this.quotes = quotes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite_quote, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Quote quote = quotes.get(position);
        Spannable spannableQuote = new SpannableString(quote.getQuote());
        Spannable spannableAuthor = new SpannableString(quote.getAuthor());

//        QUOTE : SIZE
        spannableQuote.setSpan(new AbsoluteSizeSpan(16, true),
                0,
                spannableQuote.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableQuote.setSpan(new AbsoluteSizeSpan(20, true),
                0,
                1,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

//        QUOTE : TEXT COLOR
        spannableQuote.setSpan(new ForegroundColorSpan(Color.BLUE),
                0,
                1,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


//        AUTHOR : SIZE
        spannableAuthor.setSpan(new AbsoluteSizeSpan(13, true),
                0,
                spannableAuthor.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

//        AUTHOR : TEXT COLOR
        spannableAuthor.setSpan(new ForegroundColorSpan(Color.GRAY),
                0,
                spannableAuthor.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

//        AUTHOR : UNDERLINE
        spannableAuthor.setSpan(new UnderlineSpan(),
                0,
                spannableAuthor.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

//        AUTHOR : BOLD
        spannableAuthor.setSpan(new StyleSpan(Typeface.BOLD),
                0,
                spannableAuthor.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        holder.tvQuote.setText(spannableQuote);
        holder.tvAuthor.setText(spannableAuthor);
    }

    @Override
    public int getItemCount() {
        return quotes.size();
    }


}
