package com.nor1.example;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by alexwilczewski on 8/27/13.
 */
public class TripListAdapter extends BaseAdapter {
    private List<Tour> items;
    private int resourceId;
    private Context context;

    public TripListAdapter(Context context, List<Tour> items) {
        this.context = context;
        this.items = items;
        this.resourceId = R.layout.list_item_trip;
    }

    public long getItemId(int position) {
        return position;
    }

    public Tour getItem(int position) {
        return items.get(position);
    }

    public int getCount() {
        return items.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(context);
            v = vi.inflate(resourceId, null);
        }

        Tour tour = items.get(position);

        if(tour != null) {
            ImageView thumbnail = (ImageView) v.findViewById(R.id.item_thumb);
            TextView title = (TextView) v.findViewById(R.id.item_title);
            TextView description = (TextView) v.findViewById(R.id.item_description);

            title.setText(tour.title);
            description.setText(tour.description);

            thumbnail.setTag(tour.thumbnailUrl);
            new DownloadImageTask().execute(thumbnail);
        }

        return v;
    }

    public class DownloadImageTask extends AsyncTask<ImageView, Void, Bitmap> {
        ImageView imageView = null;

        protected Bitmap doInBackground(ImageView... imageViews) {
            this.imageView = imageViews[0];
            return downloadImage((String)imageView.getTag());
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }

        private Bitmap downloadImage(String url) {
            try {
                URL newurl = new URL(url);
                return BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
            } catch(MalformedURLException e) {

            } catch(IOException e) {

            }
            return Bitmap.createBitmap(1, 1, Bitmap.Config.ALPHA_8);
        }
    }
}