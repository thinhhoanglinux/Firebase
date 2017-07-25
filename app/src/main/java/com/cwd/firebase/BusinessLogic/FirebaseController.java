package com.cwd.firebase.BusinessLogic;

import android.content.Context;
import android.net.Uri;

/**
 * Created by eagle on 7/25/2017.
 * Class chứa các hàm firebase
 */

public class FirebaseController {

    // Trước khi làm cái này nhớ nhúng thư viện:
    // compile 'com.google.firebase:firebase-storage:11.0.2'
    public static void uploadAvatar(Context context, String userID, Uri imageUri) {

        // upload hình Avatar lên storage và lấy đường dẫn (lúc này đã là url chứ không phải uri ):

        //... chưa làm cái mẹ gì hết

        // upload storage xong thì upload đường dẫn vào database:

        //... chưa làm cái mẹ gì hết

    }
}
