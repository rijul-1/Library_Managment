package com.example.library_managment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> implements Filterable {

    private List<book> booklist;
    private List<book> allbooklist;
    public Adapter(List<book> BookList){
        this.booklist =BookList;
        this.allbooklist = new ArrayList<>(BookList);
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_details,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
            String url  = booklist.get(position).getImage_url();
            String author = booklist.get(position).getAuthor();
            String book = booklist.get(position).getTitle();

            holder.setdata(url,author,book);
    }

    @Override
    public int getItemCount() {
        return booklist.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<book> filteredbook = new ArrayList<>();
            if(constraint.toString().isEmpty()){
                filteredbook.addAll(allbooklist);
            }else{
                for(book Book: allbooklist){
                    if(Book.getAuthor().toLowerCase().contains(constraint.toString().toLowerCase())){
                        filteredbook.add(Book);
                    }
                    else if(Book.getTitle().toLowerCase().contains(constraint.toString().toLowerCase())){
                        filteredbook.add(Book);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredbook;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            booklist.clear();
            booklist.addAll((Collection<? extends book>) results.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView book_image;
        TextView author_name;
        TextView book_name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            book_image = itemView.findViewById(R.id.book_image);
            author_name = itemView.findViewById(R.id.book_author);
            book_name= itemView.findViewById(R.id.book_name);
        }

        public void setdata(String url, String author, String book) {
//            Glide.with(itemView.getContext()).load(url).into(book_image);
            book_name.setText(book);
            author_name.setText(author.substring(1,author.length()-1));

        }
    }
}
