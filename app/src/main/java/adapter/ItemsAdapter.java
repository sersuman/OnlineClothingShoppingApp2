package adapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.onlineclothingshoppingapp.ItemDescriptionActivity;
import com.example.onlineclothingshoppingapp.R;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import model.Items;
import url.Url;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemsViewHolder> {
    Context context;
    List<Items> itemsList;

    public ItemsAdapter(Context context, List<Items> itemsList) {
        this.context = context;
        this.itemsList = itemsList;
    }

    @NonNull
    @Override
    public ItemsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.items_layout,viewGroup,false);
        return new ItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsAdapter.ItemsViewHolder itemsViewHolder, int i) {
        final Items items = itemsList.get(i);
        String imgPath = Url.BASE_URL + "uploads/" + items.getItemImageName();
        StrictMode();

        try {
            URL url = new URL(imgPath);
            itemsViewHolder.imgPhoto.setImageBitmap(BitmapFactory.decodeStream((InputStream)url.getContent()));

        } catch (Exception e) {
            e.printStackTrace();
        }

        itemsViewHolder.tvItemName.setText(items.getItemName());
        itemsViewHolder.tvItemDescription.setText(items.getItemDescription());
        itemsViewHolder.tvItemPrice.setText(items.getItemPrice());

        itemsViewHolder.imgPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ItemDescriptionActivity.class);
                intent.putExtra("image",items.getItemImageName());
                intent.putExtra("name",items.getItemName());
                intent.putExtra("price",items.getItemPrice());
                intent.putExtra("description",items.getItemDescription());
                context.startActivity(intent);
            }
        });
    }


    private void StrictMode() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public class ItemsViewHolder extends RecyclerView.ViewHolder{
        ImageView imgPhoto;
        TextView tvItemName, tvItemPrice, tvItemDescription;

        public ItemsViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.imgPhoto);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvItemPrice = itemView.findViewById(R.id.tvItemPrice);
            tvItemDescription = itemView.findViewById(R.id.tvItemDescription);
        }
    }
}
