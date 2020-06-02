package mobile.fhi360.covid_19selfscreeningtool.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import mobile.fhi360.covid_19selfscreeningtool.Api.APIService;
import mobile.fhi360.covid_19selfscreeningtool.Api.APIUrl;
import mobile.fhi360.covid_19selfscreeningtool.R;
import mobile.fhi360.covid_19selfscreeningtool.Result;
import mobile.fhi360.covid_19selfscreeningtool.model.UserHealthData;
import mobile.fhi360.covid_19selfscreeningtool.model.Users;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserHealthDataActivity extends AppCompatActivity {


    private ProgressDialog loadingBar;

    private static final String TAG = "MainActivity";
    EditText mFullname, mDate;
    //declaring the radio group;
    RadioGroup mCough, mFever, mDifficultyInBreathing, mSneezing, mChestPain, mDiarrhoea, mFlu, mSoreThroatSymptoms,
            mContactWithFever, mContactWithCough, mContactWithDifficultBreathing, mContactWithSneeze, mContactWithChestpain,
            mContactWithDiarrhoea, mContactWithOtherFlu, mContactWithSoreThroat, mUnderlyingConditions, mSpecifyKidney, mSpecifyPregnancy,
            mSpecifyTB, mSpecifyDiabetes, mSpecifyLiver, mSpecifyChronicLungDisease, mSpecifyCancer, mSpecifyHeartDisease,
            mContactWithSomeoneWithSymptoms,
            mSpecifyHIV, mExposedToFacilityWithConfirmedCase, mTreatment, mEnoughDrugsForThreeMonths,
            mCovid19CareFromSomeoneInHousehold, mSomeoneHelpingYouManageHIV, mLossOfSmellSymptoms;
    //declaring radio buttons
    RadioButton rbFever, rbCough, rbDifficultyInBreathing, rbSneezing, rbChestPain, rbDiarrhoea, rbFlu, rbSoreThroatSymptoms, rbSpecifyCancer,
            rbContactWithFever, rbContactWithCough, rbContactWithDifficultBreathing, rbContactWithSneeze, rbContactWithChestpain, rbSpecifyKidney, rbSpecifyTB,
            rbContactWithDiarrhoea, rbContactWithOtherFlu, rbContactWithSoreThroat, rbUnderlyingConditions, rbSpecifyPregnancy, rbSpecifyDiabetes, rbSpecifyLiver,
            rbSpecifyHeartDisease, rbSpecifyChronicLungDisease, rbContactWithSomeoneWithSymptoms, rbSpecifyHIV, rbExposedToFacilityWithConfirmedCase,
            rbTreatment, rbEnoughDrugsForThreeMonths, rbCovid19CareFromSomeoneInHousehold, rbSomeoneHelpingYouManageHIV, rbLossOfSmellSymptoms;
    Button mBtn_submit;

    private Spinner mVisitSpinner, mLanguageSpinner, mAgeSpinner;
    private String mGender, mVisit, mAge;
    private List<String> states;

    Locale myLocale;
    String currentLanguage = "en", currentLang;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_health_data);

        currentLanguage = getIntent().getStringExtra(currentLang);

        mLanguageSpinner = findViewById(R.id.language_spinner);
        List<String> list = new ArrayList<>();
        list.add("Select language");
        list.add("English");
        list.add("Hausa");
        list.add("Igbo");

        mAgeSpinner = findViewById(R.id.age_spinner);
//
        initializedFields();


        mBtn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUserHealthData();
            }
        });

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> visitAdapter = ArrayAdapter.createFromResource(this,
                R.array.visit_array, android.R.layout.simple_spinner_dropdown_item);
        // Specify the layout to use when the list of choices appears
        visitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mVisitSpinner.setAdapter(visitAdapter);


        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> languageAdapter = ArrayAdapter.createFromResource(this,
                R.array.language_array, android.R.layout.simple_spinner_dropdown_item);
        // Specify the layout to use when the list of choices appears
        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mLanguageSpinner.setAdapter(languageAdapter);

        mLanguageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        setLocale("en");
                        break;
                    case 2:
                        setLocale("ha");
                        break;
                    case 3:
                        setLocale("ig");
                        break;
                    case 4:
                        setLocale("yo");
                        break;


                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//            // TODO Auto-generated method stub
                new DatePickerDialog(UserHealthDataActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }

        });
    }


    final Calendar myCalendar = Calendar.getInstance();


    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    private void updateLabel() {
        String myFormat = "MM/dd/yy";   //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        mDate.setText(sdf.format(myCalendar.getTime()));

    }

    private void initializedFields() {
        mDate = findViewById(R.id.edit_date);
        mVisitSpinner = findViewById(R.id.visit_spinner);
        mFullname = findViewById(R.id.edit_text_fullname);
        mFever = findViewById(R.id.radio_fever);
        mCough = findViewById(R.id.radio_cough);
        mDifficultyInBreathing = findViewById(R.id.radio_difficultBreathing);
        mSneezing = findViewById(R.id.radio_sneezingSymptoms);
        mChestPain = findViewById(R.id.radio_chestpainSymptoms);
        mDiarrhoea = findViewById(R.id.radio_diarrhoeaSymptoms);
        mFlu = findViewById(R.id.radio_fluSymptoms);
        mSoreThroatSymptoms = findViewById(R.id.radio_soreSymptoms);
        mContactWithFever = findViewById(R.id.radio_contactWithFever);
        mContactWithCough = findViewById(R.id.radio_contactWithCough);
        mContactWithDifficultBreathing = findViewById(R.id.radio_contactWithDifficultBreathing);
        mContactWithSneeze = findViewById(R.id.radio_contactWithSneeze);
        mContactWithChestpain = findViewById(R.id.radio_contactWithChestpain);
        mContactWithDiarrhoea = findViewById(R.id.radio_contactWithDiarrhoea);
        mContactWithOtherFlu = findViewById(R.id.radio_contactWithOtherFLu);
        mContactWithSoreThroat = findViewById(R.id.radio_contactWithSoreThroat);
        mUnderlyingConditions = findViewById(R.id.radio_condition);
        mSpecifyTB = findViewById(R.id.specifyTB);
        mSpecifyDiabetes = findViewById(R.id.radio_specifyDiabetes);
        mSpecifyLiver = findViewById(R.id.radio_specifyLiverDisease);
        mSpecifyChronicLungDisease = findViewById(R.id.radio_chronicLungDisease);
        mSpecifyCancer = findViewById(R.id.radio_specifyCancer);
        mSpecifyHeartDisease = findViewById(R.id.radio_heartDisease);
        mContactWithSomeoneWithSymptoms = findViewById(R.id.radio_contactWithSomeoneWithSymptoms);
        mSpecifyKidney = findViewById(R.id.radio_specifyKidney);
        mSpecifyPregnancy = findViewById(R.id.radio_specifyPregnancy);
        mSpecifyHIV = findViewById(R.id.specifyHIV);
        mTreatment = findViewById(R.id.treatment);
        mEnoughDrugsForThreeMonths = findViewById(R.id.enoughDrugsForThreeMonths);
        mExposedToFacilityWithConfirmedCase = findViewById(R.id.exposedToFacilityWithConfirmedCase);
        mCovid19CareFromSomeoneInHousehold = findViewById(R.id.covid19CareFromSomeoneInHousehold);
        mSomeoneHelpingYouManageHIV = findViewById(R.id.someoneHelpingYouManageHIV);
        mLossOfSmellSymptoms = findViewById(R.id.lossOfSmellSymptoms);
        mBtn_submit = findViewById(R.id.btn_submit);
        loadingBar = new ProgressDialog(this);
    }

    public void setLocale(String localeName) {
        if (!localeName.equals(currentLanguage)) {
            myLocale = new Locale(localeName);
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
            Intent refresh = new Intent(this, MainActivity.class);
            refresh.putExtra(currentLang, localeName);
            startActivity(refresh);
        } else {
            Toast.makeText(UserHealthDataActivity.this, "Language already selected!", Toast.LENGTH_SHORT).show();
        }
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        int radioButtonId2 = mFever.getCheckedRadioButtonId();
        rbFever = findViewById(radioButtonId2);

        int radioButtonId3 = mCough.getCheckedRadioButtonId();
        rbCough = findViewById(radioButtonId3);

        int radioButtonId4 = mDifficultyInBreathing.getCheckedRadioButtonId();
        rbDifficultyInBreathing = findViewById(radioButtonId4);

        int radioButtonId5 = mSneezing.getCheckedRadioButtonId();
        rbSneezing = findViewById(radioButtonId5);

        int radioButtonId6 = mChestPain.getCheckedRadioButtonId();
        rbChestPain = findViewById(radioButtonId6);

        int radioButtonId7 = mDiarrhoea.getCheckedRadioButtonId();
        rbDiarrhoea = findViewById(radioButtonId7);

        int radioButtonId8 = mFlu.getCheckedRadioButtonId();
        rbFlu = findViewById(radioButtonId8);

        int radioButtonId9 = mSoreThroatSymptoms.getCheckedRadioButtonId();
        rbSoreThroatSymptoms = findViewById(radioButtonId9);

        int radioButtonId10 = mContactWithFever.getCheckedRadioButtonId();
        rbContactWithFever = findViewById(radioButtonId10);

        int radioButtonId11 = mContactWithCough.getCheckedRadioButtonId();
        rbContactWithCough = findViewById(radioButtonId11);

        int radioButtonId12 = mContactWithDifficultBreathing.getCheckedRadioButtonId();
        rbContactWithDifficultBreathing = findViewById(radioButtonId12);

        int radioButtonId13 = mContactWithSneeze.getCheckedRadioButtonId();
        rbContactWithSneeze = findViewById(radioButtonId13);

        int radioButtonId14 = mContactWithChestpain.getCheckedRadioButtonId();
        rbContactWithChestpain = findViewById(radioButtonId14);

        int radioButtonId15 = mContactWithDiarrhoea.getCheckedRadioButtonId();
        rbContactWithDiarrhoea = findViewById(radioButtonId15);

        int radioButtonId16 = mContactWithOtherFlu.getCheckedRadioButtonId();
        rbContactWithOtherFlu = findViewById(radioButtonId16);

        int radioButtonId17 = mContactWithSoreThroat.getCheckedRadioButtonId();
        rbContactWithSoreThroat = findViewById(radioButtonId17);

        int radioButtonId18 = mUnderlyingConditions.getCheckedRadioButtonId();
        rbUnderlyingConditions = findViewById(radioButtonId18);

        int radioButtonId19 = mSpecifyKidney.getCheckedRadioButtonId();
        rbSpecifyKidney = findViewById(radioButtonId19);

        int radioButtonId20 = mSpecifyPregnancy.getCheckedRadioButtonId();
        rbSpecifyPregnancy = findViewById(radioButtonId20);

        int radioButtonId21 = mSpecifyTB.getCheckedRadioButtonId();
        rbSpecifyTB = findViewById(radioButtonId21);

        int radioButtonId22 = mSpecifyDiabetes.getCheckedRadioButtonId();
        rbSpecifyDiabetes = findViewById(radioButtonId22);

        int radioButtonId23 = mSpecifyLiver.getCheckedRadioButtonId();
        rbSpecifyLiver = findViewById(radioButtonId23);
        int radioButtonId24 = mSpecifyChronicLungDisease.getCheckedRadioButtonId();
        rbSpecifyChronicLungDisease = findViewById(radioButtonId24);

        int radioButtonId25 = mSpecifyCancer.getCheckedRadioButtonId();
        rbSpecifyCancer = findViewById(radioButtonId25);

        int radioButtonId26 = mSpecifyHeartDisease.getCheckedRadioButtonId();
        rbSpecifyHeartDisease = findViewById(radioButtonId26);

        int radioButtonId27 = mContactWithSomeoneWithSymptoms.getCheckedRadioButtonId();
        rbContactWithSomeoneWithSymptoms = findViewById(radioButtonId27);

        int radioButtonId30 = mSpecifyHIV.getCheckedRadioButtonId();
        rbSpecifyHIV = findViewById(radioButtonId30);

        int radioButtonId31 = mTreatment.getCheckedRadioButtonId();
        rbTreatment = findViewById(radioButtonId31);

        int radioButtonId32 = mEnoughDrugsForThreeMonths.getCheckedRadioButtonId();
        rbEnoughDrugsForThreeMonths = findViewById(radioButtonId32);

        int radioButtonId33 = mExposedToFacilityWithConfirmedCase.getCheckedRadioButtonId();
        rbExposedToFacilityWithConfirmedCase = findViewById(radioButtonId33);

        int radioButtonId34 = mCovid19CareFromSomeoneInHousehold.getCheckedRadioButtonId();
        rbCovid19CareFromSomeoneInHousehold = findViewById(radioButtonId34);

        int radioButtonId35 = mSomeoneHelpingYouManageHIV.getCheckedRadioButtonId();
        rbSomeoneHelpingYouManageHIV = findViewById(radioButtonId35);

        int radioButtonId36 = mLossOfSmellSymptoms.getCheckedRadioButtonId();
        rbLossOfSmellSymptoms = findViewById(radioButtonId36);


        switch (view.getId()) {
            case R.id.radio_condition_no:
                if (checked)
                    findViewById(R.id.first_group).setVisibility(View.GONE);
                break;

            case R.id.radio_condition_yes:
                if (checked)
                    findViewById(R.id.first_group).setVisibility(View.VISIBLE);
                break;
        }

        switch (view.getId()) {
            case R.id.radio_specifyHIV_no:
                if (checked)
                    findViewById(R.id.second_group).setVisibility(View.GONE);
                break;

            case R.id.radio_specifyHIV_yes:
                if (checked)
                    findViewById(R.id.second_group).setVisibility(View.VISIBLE);
                break;
        }
    }

    private void createUserHealthData() {
        String fullname = mFullname.getText().toString().trim();
        String date = mDate.getText().toString().trim();
        final String stateVisited = String.valueOf(mVisitSpinner.getSelectedItem());
        final String feverSymptom = ((RadioButton) findViewById(mFever.getCheckedRadioButtonId())).getText().toString();
        final String coughSymptom = ((RadioButton) findViewById(mCough.getCheckedRadioButtonId())).getText().toString();
        final String difficultyInBreathingSymptom = ((RadioButton) findViewById(mDifficultyInBreathing.getCheckedRadioButtonId())).getText().toString();
        final String sneezingSymptoms = ((RadioButton) findViewById(mSneezing.getCheckedRadioButtonId())).getText().toString();
        String chestPainSymptoms = ((RadioButton) findViewById(mChestPain.getCheckedRadioButtonId())).getText().toString();
        final String diarrhoeaSymptoms = ((RadioButton) findViewById(mDiarrhoea.getCheckedRadioButtonId())).getText().toString();
        String fluSymptoms = ((RadioButton) findViewById(mFlu.getCheckedRadioButtonId())).getText().toString();
        String soreThroatSymptoms = ((RadioButton) findViewById(mSoreThroatSymptoms.getCheckedRadioButtonId())).getText().toString();
        final String contactWithFever = ((RadioButton) findViewById(mContactWithFever.getCheckedRadioButtonId())).getText().toString();
        final String contactWithCough = ((RadioButton) findViewById(mContactWithCough.getCheckedRadioButtonId())).getText().toString();
        final String contactWithDifficultBreathing = ((RadioButton) findViewById(mContactWithDifficultBreathing.getCheckedRadioButtonId())).getText().toString();
        final String contactWithSneeze = ((RadioButton) findViewById(mContactWithSneeze.getCheckedRadioButtonId())).getText().toString();
        final String contactWithChestpain = ((RadioButton) findViewById(mContactWithChestpain.getCheckedRadioButtonId())).getText().toString();
        final String contactWithDiarrhoea = ((RadioButton) findViewById(mContactWithDiarrhoea.getCheckedRadioButtonId())).getText().toString();
        final String contactWithOtherFLu = ((RadioButton) findViewById(mContactWithOtherFlu.getCheckedRadioButtonId())).getText().toString();
        final String contactWithSoreThroat = ((RadioButton) findViewById(mContactWithSoreThroat.getCheckedRadioButtonId())).getText().toString();
        final String underlyingConditions = ((RadioButton) findViewById(mUnderlyingConditions.getCheckedRadioButtonId())).getText().toString();
        final String specifyKidney = ((RadioButton) findViewById(mSpecifyKidney.getCheckedRadioButtonId())).getText().toString();
        final String specifyPregnancy = ((RadioButton) findViewById(mSpecifyPregnancy.getCheckedRadioButtonId())).getText().toString();
        final String specifyTB = ((RadioButton) findViewById(mSpecifyTB.getCheckedRadioButtonId())).getText().toString();
        final String specifyDiabetes = ((RadioButton) findViewById(mSpecifyDiabetes.getCheckedRadioButtonId())).getText().toString();
        final String specifyLiver = ((RadioButton) findViewById(mSpecifyLiver.getCheckedRadioButtonId())).getText().toString();
        final String specifyChronicLungDisease = ((RadioButton) findViewById(mSpecifyChronicLungDisease.getCheckedRadioButtonId())).getText().toString();
        final String specifyCancer = ((RadioButton) findViewById(mSpecifyCancer.getCheckedRadioButtonId())).getText().toString();
        final String specifyHeartDisease = ((RadioButton) findViewById(mSpecifyHeartDisease.getCheckedRadioButtonId())).getText().toString();
        final String contactWithSomeoneWithSymptoms = ((RadioButton) findViewById(mContactWithSomeoneWithSymptoms.getCheckedRadioButtonId())).getText().toString();
        final String specifyHIV = ((RadioButton) findViewById(mSpecifyHIV.getCheckedRadioButtonId())).getText().toString();
        final String treatment = ((RadioButton) findViewById(mTreatment.getCheckedRadioButtonId())).getText().toString();
        final String enoughDrugsForThreeMonths = ((RadioButton) findViewById(mEnoughDrugsForThreeMonths.getCheckedRadioButtonId())).getText().toString();
        final String exposedToFacilityWithConfirmedCase = ((RadioButton) findViewById(mExposedToFacilityWithConfirmedCase.getCheckedRadioButtonId())).getText().toString();
        final String covid19CareFromSomeoneInHousehold = ((RadioButton) findViewById(mCovid19CareFromSomeoneInHousehold.getCheckedRadioButtonId())).getText().toString();
        final String someoneHelpingYouManageHIV = ((RadioButton) findViewById(mSomeoneHelpingYouManageHIV.getCheckedRadioButtonId())).getText().toString();
        final String lossOfSmellSymptoms = ((RadioButton) findViewById(mLossOfSmellSymptoms.getCheckedRadioButtonId())).getText().toString();

        //validations of fields
        if (TextUtils.isEmpty(fullname)) {
            mFullname.setError("Please enter initials!");
            mFullname.requestFocus();
            return;
        }
        if (fullname.contains(".") || fullname.contains(",") || fullname.contains("&")
                || fullname.contains("*") || fullname.contains("-")) {
            mFullname.setError("special characters not allowed here!");
            mFullname.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(date)) {
            mDate.setError("Enter date!");
            mDate.requestFocus();
        }
        //making api calls

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing Up...");
        progressDialog.show();
        //building retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Defining retrofit api service
        APIService service = retrofit.create(APIService.class);

        //Defining the user object as we need to pass it with the call
        UserHealthData userHealthData = new UserHealthData(fullname,
                date,
                stateVisited,
                feverSymptom,
                coughSymptom,
                difficultyInBreathingSymptom,
                sneezingSymptoms,
                chestPainSymptoms,
                diarrhoeaSymptoms,
                fluSymptoms,
                soreThroatSymptoms,
                lossOfSmellSymptoms,
                contactWithSomeoneWithSymptoms,
                contactWithFever,
                contactWithCough,
                contactWithDifficultBreathing,
                contactWithSneeze,
                contactWithChestpain,
                contactWithDiarrhoea,
                contactWithOtherFLu,
                contactWithSoreThroat,
                underlyingConditions,
                specifyKidney,
                specifyPregnancy,
                specifyTB,
                specifyDiabetes,
                specifyLiver,
                specifyChronicLungDisease,
                specifyCancer,
                specifyHeartDisease,
                specifyHIV,
                treatment,
                enoughDrugsForThreeMonths,
                exposedToFacilityWithConfirmedCase,
                someoneHelpingYouManageHIV,
                covid19CareFromSomeoneInHousehold);

        //defining the call
        Call<Result> call = service.createUserHealthData(
                userHealthData.getFullname(),
                userHealthData.getDate(),
                userHealthData.getStateVisited(),
                userHealthData.getFeverSymptom(),
                userHealthData.getChestPainSymptoms(),
                userHealthData.getCoughSymptom(),
                userHealthData.getContactWithDifficultBreathing(),
                userHealthData.getSneezingSymptoms(),
                userHealthData.getChestPainSymptoms(),
                userHealthData.getDiarrhoeaSymptoms(),
                userHealthData.getFluSymptoms(),
                userHealthData.getSoreThroatSymptoms(),
                userHealthData.getContactWithSomeoneWithSymptoms(),
                userHealthData.getContactWithFever(),
                userHealthData.getContactWithCough(),
                userHealthData.getContactWithDifficultBreathing(),
                userHealthData.getContactWithSneeze(),
                userHealthData.getContactWithChestpain(),
                userHealthData.getContactWithDiarrhoea(),
                userHealthData.getContactWithOtherFLu(),
                userHealthData.getContactWithSoreThroat(),
                userHealthData.getUnderlyingConditions(),
                userHealthData.getSpecifyKidney(),
                userHealthData.getSpecifyPregnancy(),
                userHealthData.getSpecifyTB(),
                userHealthData.getSpecifyDiabetes(),
                userHealthData.getSpecifyLiver(),
                userHealthData.getSpecifyChronicLungDisease(),
                userHealthData.getSpecifyCancer(),
                userHealthData.getSpecifyHeartDisease(),
                userHealthData.getSpecifyHIV(),
                userHealthData.getTreatment(),
                userHealthData.getEnoughDrugsForThreeMonths(),
                userHealthData.getExposedToFacilityWithConfirmedCase(),
                userHealthData.getCovid19CareFromSomeoneInHousehold()

        );

//calling the api
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                //hiding progress dialog
                loadingBar.dismiss();

                //displaying the message from the response as toast
                Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                System.out.println(":::" + response);
                Log.i("Responsestring", response.body().toString());
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                loadingBar.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();

                System.out.println(">>>>>>>>" + t);
                Log.i("Responsestring", t.toString());
            }
        });

    }

    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Want to go back?")
                .setMessage("Are you sure you want to go back?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(UserHealthDataActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                }).create().show();
    }
}