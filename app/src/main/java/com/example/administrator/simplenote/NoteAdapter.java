package com.example.administrator.simplenote;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2015/6/1.
 */
public class NoteAdapter extends BaseAdapter{

    private Context context;
    private Cursor cursor;
    private LayoutInflater mInflater;

    public NoteAdapter(Context context, Cursor cursor){
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public Object getItem(int position) {
        return cursor.getPosition();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        mInflater = LayoutInflater.from(context);
        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView =  mInflater.inflate(R.layout.list_item, null);
            viewHolder.titleTV = (TextView) convertView.findViewById(R.id.item_title);
            viewHolder.timeTV = (TextView) convertView.findViewById(R.id.item_time);
            viewHolder.contentTV = (TextView) convertView.findViewById(R.id.item_content);
            viewHolder.imgTV_1 = (ImageView) convertView.findViewById(R.id.item_photo_1);
            viewHolder.imgTV_2 = (ImageView) convertView.findViewById(R.id.item_photo_2);
            viewHolder.imgTV_3 = (ImageView) convertView.findViewById(R.id.item_photo_3);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        cursor.moveToPosition(position);
        String title = cursor.getString(cursor.getColumnIndex("title"));
        String time = cursor.getString(cursor.getColumnIndex("time"));
        String content = cursor.getString(cursor.getColumnIndex("content"));
        String urlimg = cursor.getString(cursor.getColumnIndex("img"));
        String urlphoto = cursor.getString(cursor.getColumnIndex("photo"));
        String urlvideo = cursor.getString(cursor.getColumnIndex("video"));
        viewHolder.titleTV.setText(title);
        viewHolder.timeTV.setText(time);
        viewHolder.contentTV.setText(content);
        viewHolder.imgTV_1.setImageBitmap(getImageThumbnail(urlimg, 75, 55));
        viewHolder.imgTV_2.setImageBitmap(getImageThumbnail(urlphoto, 75, 55));
        viewHolder.imgTV_2.setImageBitmap(getVideoThumbnail(urlvideo, 75, 55, MediaStore.Images.Thumbnails.MICRO_KIND));

        return convertView;
    }

    private Bitmap getImageThumbnail(String uri, int width, int height) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        bitmap = BitmapFactory.decodeFile(uri,options);
        options.inJustDecodeBounds = false;
        int beWidth = options.outWidth/width;
        int beHeight = options.outHeight/height;
        int be = 1;
        if (beWidth < beHeight){
            be = beWidth;
        }else {
            be = beHeight;
        }
        if (be <= 0){
            be = 1;
        }
        options.inSampleSize = be;
        bitmap = BitmapFactory.decodeFile(uri, options);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    private Bitmap getVideoThumbnail(String uri, int width, int height, int kind){
        Bitmap bitmap = null;
        bitmap = ThumbnailUtils.createVideoThumbnail(uri, kind);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);

        return bitmap;
    }

    class ViewHolder{
        public TextView titleTV;
        public TextView timeTV;
        public TextView contentTV;
        public ImageView imgTV_1;
        public ImageView imgTV_2;
        public ImageView imgTV_3;
    }
}

