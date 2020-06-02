package mobile.fhi360.covid_19selfscreeningtool.Api;

import mobile.fhi360.covid_19selfscreeningtool.Result;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIService {
    @FormUrlEncoded
    @POST("users")
    Call<Result> createUser(
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
            @Field("facility") String facility);


    @FormUrlEncoded
    @POST("userHealthData")
    Call<Result> createUserHealthData(
            @Field("date") String date,
            @Field("feverSymptom") String feverSymptom,
            @Field("coughSymptom") String coughSymptom,
            @Field("difficultyInBreathingSymptom") String difficultyInBreathingSymptom,
            @Field("sneezingSymptoms") String sneezingSymptoms,
            @Field("chestPainSymptoms") String chestPainSymptoms,
            @Field("diarrhoeaSymptoms") String diarrhoeaSymptoms,
            @Field("fluSymptoms") String fluSymptoms,
            @Field("soreThroatSymptoms") String soreThroatSymptoms,
            @Field("lossOfSmellSymptoms") String lossOfSmellSymptoms,
            @Field("contactWithSomeoneWithSymptoms") String contactWithSomeoneWithSymptoms,
            @Field("stateVisited") String stateVisited,
            @Field("specifyKidney") String exposedToFacilityWithConfirmedCase,
            @Field("contactWithFever") String contactWithFever,
            @Field("contactWithCough") String contactWithCough,
            @Field("contactWithDifficultBreathing") String contactWithDifficultBreathing,
            @Field("contactWithSneeze") String contactWithSneeze,
            @Field("contactWithChestpain") String contactWithChestpain,
            @Field("contactWithDiarrhoea") String contactWithDiarrhoea,
            @Field("contactWithOtherFLu") String contactWithOtherFLu,
            @Field("contactWithSoreThroat") String contactWithSoreThroat,
            @Field("underlyingConditions") String underlyingConditions,
            @Field("specifyKidney") String specifyKidney,
            @Field("specifyPregnancy") String specifyPregnancy,
            @Field("specifyTB") String specifyTB,
            @Field("specifyDiabetes") String specifyDiabetes,
            @Field("specifyLiver") String specifyLiver,
            @Field("specifyChronicLungDisease") String specifyChronicLungDisease,
            @Field("specifyCancer") String specifyCancer,
            @Field("specifyHeartDisease") String specifyHeartDisease,
            @Field("specifyHIV") String specifyHIV,
            @Field("treatment") String treatment,
            @Field("enoughDrugsForThreeMonths") String enoughDrugsForThreeMonths,
            @Field("someoneHelpingYouManageHIV") String ssomeoneHelpingYouManageHIVpecifyPregnancy,
            @Field("covid19CareFromSomeoneInHousehold") String covid19CareFromSomeoneInHousehold
    );
    //the signin call
    @FormUrlEncoded
    @POST("login")
    Call<Result> userLogin(
            @Field("email") String email,
            @Field("password") String password
    );
}
