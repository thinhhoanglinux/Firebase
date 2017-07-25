package com.cwd.firebase.CustomDialog;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cwd.firebase.R;

import static com.cwd.firebase.BusinessLogic.SystemController.openCamera;
import static com.cwd.firebase.BusinessLogic.SystemController.openGallery;


/**
 * Created by eagle on 7/24/2017.
 */

public class PhotoDialog {

    public View view;
    public AlertDialog.Builder builder;
    public AlertDialog dialog;
    public Context context;
    public TextView txvTitle, txvClose;
    public ImageView imvCamera, imvGallery;

    public PhotoDialog(final Context context) {
        this.context = context;
        this.view = LayoutInflater.from(context).inflate(R.layout.dialog_photo, null);
        this.builder = new AlertDialog.Builder(context);
        this.txvTitle = (TextView) view.findViewById(R.id.dialog_photo_title);
        this.imvCamera = (ImageView) view.findViewById(R.id.dialog_photo_cam);
        this.imvGallery = (ImageView) view.findViewById(R.id.dialog_photo_gal);
        this.txvClose = (TextView) view.findViewById(R.id.dialog_photo_close);
        this.txvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        this.imvCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openCamera(v.getContext());
            }
        });
        this.imvGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openGallery(v.getContext());
            }
        });
    }

    public void setTitle(CharSequence title) {
        if (title != null)
            txvTitle.setText(title);
    }

    public void show() {
        builder.setView(view);
        dialog = builder.create();
        dialog.show();

    }

    public void dismiss() {
        dialog.dismiss();
    }
}
