package com.cwd.firebase.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cwd.firebase.CustomDialog.PhotoDialog;
import com.cwd.firebase.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theartofdev.edmodo.cropper.CropImage;

import static com.cwd.firebase.BusinessLogic.Constant.IMAGE_REQUEST_CODE;
import static com.cwd.firebase.BusinessLogic.FirebaseController.uploadAvatar;
import static com.cwd.firebase.BusinessLogic.SystemController.openCamera;
import static com.cwd.firebase.BusinessLogic.SystemController.openGallery;


public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String myID, name;
    private TextView txvName;
    private ImageView imvAvatar;
    private AlertDialog.Builder defaultDialog;
    private PhotoDialog customDialog;
    private Context context;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mapping();
        context = ProfileActivity.this;

        // kết nối lắng nghe xác thực tài khoản:
        mAuth = FirebaseAuth.getInstance();

        // Lấy id của user đang đăng nhập trên thiết bị:
        myID = mAuth.getCurrentUser().getUid();

        // kết nối database và lấy đường dẫn note gốc của database dạng cây JSON:
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Tạo sự kiện lắng nghe dữ liệu firebase: (đồng bộ thời gian thực - real time)
        // để lấy dự liệu từ đường dẫn "gốc/account/user ID/name":
        mDatabase.child("account").child(myID).child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                name = dataSnapshot.getValue().toString();
                txvName.setText(name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        imvAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // sử dụng dialog mặc định xài hàm này đơn giản (nhanh, gọn, lẹ, xấu)
                // openDefaultDialog();

                // sử dụng custom dialog xài hàm này (tốn thời gian tí, đẹp, so cool)
                openCustomDialog();
            }
        });

    }

    private void mapping() {

        txvName = (TextView) findViewById(R.id.profile_name);
        imvAvatar = (ImageView) findViewById(R.id.profile_avatar);
    }

    // Khỏi tạo và mở dialog mặc định:
    private void openDefaultDialog() {
        defaultDialog = new AlertDialog.Builder(ProfileActivity.this);
        // đặt tiêu đề và chữ
        defaultDialog.setTitle("....").setMessage("Chọn ");
        defaultDialog.setPositiveButton("Từ thư viện", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // mo thu vien anh:
                openGallery(context);
            }
        });
        defaultDialog.setNegativeButton("Chụp ảnh mới", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // mo camera:
                openCamera(context);
            }
        });
        defaultDialog.create().show(); // khởi tạo và show dialog:
    }

    // Khỏi tạo và mở dialog tùy chỉnh
    private void openCustomDialog() {
        customDialog = new PhotoDialog(ProfileActivity.this);
        customDialog.setTitle("Select action");
        customDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //          SỬ DỤNG THƯ VIỆN CROP IMAGE (CẮT ẢNH)
        //          compile 'com.theartofdev.edmodo:android-image-cropper:2.4.3'
        //          và thêm activity crop image vào manifest ():
        //--------------------------------------------------------------------------------------.
        //                                                                                      |
        //        <activity android:name="com.theartofdev.edmodo.cropper.CropImageActivity"     |
        //                  android:theme="@style/Base.Theme.AppCompat" />                      |
        //                                                                                      |
        //--------------------------------------------------------------------------------------
        //                                                thấy quả comment tỏ ra nguy hiểm ko :) ?

        super.onActivityResult(requestCode, resultCode, data);
        // Nếu nhận <ActivityResult> từ activity CHỌN ẢNH (không phân biệt từ thư viện hay chụp ảnh mới)
        // và kiểm tra có chọn ảnh hay không để xử lý hàm if này
        if (requestCode == IMAGE_REQUEST_CODE
                && resultCode == RESULT_OK
                && data != null) {
            imageUri = data.getData();
            // Lấy được tấm ảnh rồi thì mở activity cắt ảnh
            CropImage.activity(imageUri)// thảy cái uri vào intent crop image
                    .setAspectRatio(1, 1)// set tỉ lệ cắt (ở đây cắt avatar nên để tỉ lệ 1:1 cho nó thành hình vuông)
                    .setMaxCropResultSize(500,500) // kích thước cắt tối đa: 500x500 pixel
                    .setMinCropWindowSize(50, 50) // kích thước cắt tối thiểu: 50x50 pixel
                    .setBackgroundColor(R.color.colorWhite) // tự hiểu
                    .start(this); // tự hiểu
        }

        // Nếu nhận <ActivityResult> từ activity CẮT ẢNH
        // và nhận được 1 tấm ảnh đã cắt thì xử lý hàm if này:
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                // Lấy đường dẫn
                imageUri = result.getUri();
                // Đóng dialog mở lên lúc nãy
                customDialog.dismiss();
                // upload Avatar lên firebase
                uploadAvatar(context, myID, imageUri);// Xem bên class FirebaseController

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                // nếu xảy ra lỗi thì cũng đóng dialog luôn:
                customDialog.dismiss();
            }
        }
    }


}
