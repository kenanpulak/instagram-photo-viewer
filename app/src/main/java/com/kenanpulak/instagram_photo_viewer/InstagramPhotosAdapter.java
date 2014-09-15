package com.kenanpulak.instagram_photo_viewer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by kenanpulak on 9/14/14.
 */
public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {

    public InstagramPhotosAdapter(Context context, List<InstagramPhoto> photos){
        super(context, android.R.layout.simple_list_item_1, photos);
    }

    //Takes a data item at a position and converts it into a view in the listview
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        //Take the data source at position

        //Get the data item
        InstagramPhoto photo = getItem(position);

        //Check to see if we are using a recycled view
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo,parent,false);
        }
        //Lookup the subviews within the template
        TextView tvCaption = (TextView)convertView.findViewById(R.id.tvCaption);
        ImageView imgPhoto = (ImageView)convertView.findViewById(R.id.imgPhoto);
        TextView tvUsername = (TextView)convertView.findViewById(R.id.tvUsername);
        TextView tvPlaceName = (TextView)convertView.findViewById(R.id.tvPlaceName);
        ImageView ivUserImage = (ImageView)convertView.findViewById(R.id.ivUserImage);


        //Populate the subviews with the correct data
        tvCaption.setText(photo.caption);
        tvUsername.setText(photo.username);
        if (photo.placename != null){
            tvPlaceName.setText(photo.placename);
        }
        else{
            tvPlaceName.setText("No Location Available");
        }
        Picasso.with(getContext()).load(photo.userImageURL).into(ivUserImage);

        //set image height before loading
        imgPhoto.getLayoutParams().height = photo.imageHeight;
        // Reset the image from the recycled view
        imgPhoto.setImageResource(0);
        //Ask for the photo to be added to the imageView based on the imageURL
        Picasso.with(getContext()).load(photo.imageURL).into(imgPhoto);
        //Return the view for that data item
        return convertView;
    }

}
