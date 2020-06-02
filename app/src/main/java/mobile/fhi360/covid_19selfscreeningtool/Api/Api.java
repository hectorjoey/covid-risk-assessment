package mobile.fhi360.covid_19selfscreeningtool.Api;


import mobile.fhi360.covid_19selfscreeningtool.Result;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {
    @FormUrlEncoded
    @POST("users")
    Call<ResponseBody> createUser(
            @Field("email") String email,
            @Field("password") String password,
            @Field("firstname") String firstname,
            @Field("lastname") String lastname,
            @Field("phone") String phone,
            @Field("age") String age,
            @Field("userType") String userType,
            @Field("gender") String gender,
            @Field("state") String state,
            @Field("lga") String lga,
            @Field("facility") String facility
    );

    //the signin call
    @FormUrlEncoded
    @POST("login")
    Call<Result> login(
            @Field("email") String email,
            @Field("password") String password
    );

}
