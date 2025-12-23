package com.example.assignment_04_android.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.assignment_04_android.R;
import com.example.assignment_04_android.model.NewsArticle;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private Context context;
    private List<NewsArticle> articles;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(NewsArticle article);

        void onDeleteClick(NewsArticle article);

        void onShareClick(NewsArticle article);
    }

    public NewsAdapter(Context context, List<NewsArticle> articles, OnItemClickListener listener) {
        this.context = context;
        this.articles = articles;
        this.listener = listener;
    }

    public void updateData(List<NewsArticle> newArticles) {
        this.articles = newArticles;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NewsViewHolder holder, int position) {
        final NewsArticle article = articles.get(position);
        holder.tvTitle.setText(article.getTitle());
        holder.tvDesc.setText(article.getDescription());
        holder.tvDate.setText(article.getPublishedAt());

        // Click to open details
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(article);
            }
        });

        // Popup Menu (3 dots)
        holder.tvOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, holder.tvOptions);
                popup.inflate(R.menu.menu_item_popup);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.action_open) {
                            listener.onItemClick(article);
                            return true;
                        } else if (id == R.id.action_share) {
                            listener.onShareClick(article);
                            return true;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });

        // Context Menu is registered using OnCreateContextMenuListener in ViewHolder
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView tvTitle, tvDesc, tvDate, tvOptions;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDesc = itemView.findViewById(R.id.tvDescription);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvOptions = itemView.findViewById(R.id.tvOptions);

            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select Action");
            // The logic to handle selection is in Activity, handled via
            // 'onContextItemSelected'
            // We need to pass the ID or position to the activity.
            // Often we use a custom menu item ID or set the intent.
            // But standard way: Activity.registerForContextMenu(recyclerView) and use
            // RecyclerContextMenuInfo?
            // To keep it simple inside Adapter/ViewHolder logic:

            MenuItem deleteItem = menu.add(0, 101, 0, "Delete Article");
            deleteItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    listener.onDeleteClick(articles.get(getAdapterPosition()));
                    return true;
                }
            });
        }
    }
}
