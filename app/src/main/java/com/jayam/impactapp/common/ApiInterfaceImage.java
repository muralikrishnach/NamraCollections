package com.jayam.impactapp.common;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by administrator_pc on 13-06-2018.
 */

public interface ApiInterfaceImage {
    @Multipart
    @POST("DocumentUpload/POST")
    Call<ResponseBody> uploadImage(@Part MultipartBody.Part file);
}
