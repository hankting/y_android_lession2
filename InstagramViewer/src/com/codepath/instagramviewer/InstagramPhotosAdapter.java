package com.codepath.instagramviewer;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {

    public InstagramPhotosAdapter(Context context, List<InstagramPhoto> photos) {
        super(context, R.layout.item_photo, photos);
        // TODO Auto-generated constructor stub
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        // return super.getView(position, convertView, parent);
        InstagramPhoto photo = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent,
                    false);
        }
        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        tvCaption.setText(photo.caption);
        TextView tvLikesCount = (TextView) convertView.findViewById(R.id.tvLikesCount);
        tvLikesCount.setText("Likes Count: " + String.valueOf(photo.likesCount));
        TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
        tvUserName.setText("User Name: " + photo.username);

        TextView tvCreateTime = (TextView) convertView.findViewById(R.id.tvCreateTime);
//        Date date = new Date(photo.createTime * 1000);
//        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
//        cal.setTime(date);
//        String dateString = cal.get(Calendar.YEAR) + "/" + cal.get(Calendar.MONTH) + "/"
//                + cal.get(Calendar.DATE) + " " + cal.get(Calendar.HOUR) + ":"
//                + cal.get(Calendar.MINUTE);
//        tvCreateTime.setText("Create Time: " + String.valueOf(photo.createTime));
        int relativeTs = (int) (((System.currentTimeMillis() - photo.createTime * 1000)/1000)%86400);
        int relativeHH = (int) relativeTs / 3600;
        int relativeMM = (int) (relativeTs % 3600) / 60;
        tvCreateTime.setText("Relative Time: - " + String.valueOf(relativeHH) + " Hours "
            + String.valueOf(relativeMM) + " Minutes");

        ImageView imgPhoto = (ImageView) convertView.findViewById(R.id.imgPhoto);
        imgPhoto.getLayoutParams().height = photo.imageHeight;
        imgPhoto.setImageResource(0);
        Picasso.with(getContext()).load(photo.imageUrl).into(imgPhoto);

        ImageView profilePhoto = (ImageView) convertView.findViewById(R.id.profilePhoto);
        profilePhoto.setImageResource(0);
        Picasso.with(getContext()).load(photo.profilePicture).into(profilePhoto);

        return convertView;
    }

}
