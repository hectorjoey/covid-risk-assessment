package mobile.fhi360.covid_19selfscreeningtool.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ikhiloyaimokhai.nigeriastatesandlgas.Nigeria;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import mobile.fhi360.covid_19selfscreeningtool.Api.APIService;
import mobile.fhi360.covid_19selfscreeningtool.Api.APIUrl;
import mobile.fhi360.covid_19selfscreeningtool.R;
import mobile.fhi360.covid_19selfscreeningtool.Result;
import mobile.fhi360.covid_19selfscreeningtool.model.Users;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RegisterActivity extends AppCompatActivity {
    EditText mFirstname, mLastname, mPhone, mEmail, mPassword;
    Button mButtonRegister;
    TextView mLoginLink;
    Spinner mUserTypeSpinner, mStateSpinner,
            mLgaSpinner, mFacilitySpinner,
            mGender, mAgeSpinner;

    private ProgressDialog loadingBar;


    private String mState, mLga, mAge;
    private List<String> states;
    private static final int SPINNER_HEIGHT = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mStateSpinner = findViewById(R.id.stateSpinner);
        mLgaSpinner = findViewById(R.id.lgaSpinner);
        initializedFields();

        resizeSpinner(mStateSpinner, SPINNER_HEIGHT);
        resizeSpinner(mLgaSpinner, SPINNER_HEIGHT);

        states = Nigeria.getStates();

        //call to method that'll set up state and lga spinner
        setupSpinners();


        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });


        mLoginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                senUserToLoginActivity();
            }

        });

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> userTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.userType_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        userTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        mUserTypeSpinner.setAdapter(userTypeAdapter);


        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        mGender.setAdapter(genderAdapter);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> facilityAdapter = ArrayAdapter.createFromResource(this,
                R.array.facility_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        facilityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        mFacilitySpinner.setAdapter(facilityAdapter);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> ageAdapter = ArrayAdapter.createFromResource(this,
                R.array.age_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        ageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        mAgeSpinner.setAdapter(ageAdapter);
    }

    /**
     * Method to set up the spinners
     */
    public void setupSpinners() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        //populates the quantity spinner ArrayList

        ArrayAdapter<String> statesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, states);

        // Specify dropdown layout style - simple list view with 1 item per line
        statesAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        // Apply the adapter to the spinner
        statesAdapter.notifyDataSetChanged();
        mStateSpinner.setAdapter(statesAdapter);

        // Set the integer mSelected to the constant values
        mStateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mState = (String) parent.getItemAtPosition(position);
                setUpStatesSpinner(position);
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Unknown
            }
        });
    }

    /**
     * method to set up the state spinner
     *
     * @param position current position of the spinner
     */
    private void setUpStatesSpinner(int position) {
        List<String> list = new ArrayList<>(Nigeria.getLgasByState(states.get(position)));
        setUpLgaSpinner(list);
    }

    /**
     * Method to set up the local government areas corresponding to selected states
     *
     * @param lgas represents the local government areas of the selected state
     */
    private void setUpLgaSpinner(List<String> lgas) {

        ArrayAdapter lgaAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lgas);
        lgaAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        lgaAdapter.notifyDataSetChanged();
        mLgaSpinner.setAdapter(lgaAdapter);

        mLgaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                mLga = (String) parent.getItemAtPosition(position);
//                    Toast.makeText(RegisterActivity.this, "state: " + mState + " lga: " + mLga, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void resizeSpinner(Spinner spinner, int height) {
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            //Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spinner);

            //set popupWindow height to height
            popupWindow.setHeight(height);

        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }

    private void registerUser() {


        final String email = mEmail.getText().toString().trim();
        final String password = mPassword.getText().toString().trim();
        final String firstname = mFirstname.getText().toString().trim();
        final String lastname = mLastname.getText().toString().trim();
        final String age = String.valueOf(mAgeSpinner.getSelectedItem());
        final String phone = mPhone.getText().toString().trim();
        final String userType = String.valueOf(mUserTypeSpinner.getSelectedItem());
        final String gender = String.valueOf(mGender.getSelectedItem());
        final String state = String.valueOf(mStateSpinner.getSelectedItem());
        final String lga = String.valueOf(mLgaSpinner.getSelectedItem());
        final String facility = String.valueOf(mFacilitySpinner.getSelectedItem());

        if (TextUtils.isEmpty(firstname)) {
            mFirstname.setError("first name is required");
            mFirstname.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(lastname)) {
            mLastname.setError("last name is required");
            mLastname.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            mPhone.setError("Phone number is required!");
            mPhone.requestFocus();
        }

        if (phone.length() != 11 && (!phone.startsWith("070") || !phone.startsWith("080") || !phone.startsWith("090") || !phone.startsWith("081"))) {
            mPhone.setError("enter a valid phone number!");
            mPhone.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            mEmail.setError("Email is required!");
            mEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmail.setError("Enter a valid email");
            mEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            mPassword.setError("Password is required");
            mPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            mPassword.setError("Password length is too short");
            mPassword.requestFocus();
        }
        //do registration API call

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
        Users users = new Users(email,password,firstname, lastname,phone,
               age,userType, gender,state, lga,facility);

        //defining the call
        Call<Result> call = service.createUser(
                users.getEmail(),
                users.getPassword(),
                users.getFirstname(),
                users.getLastname(),
                users.getPhone(),
                users.getAge(),
                users.getUserType(),
                users.getGender(),
                users.getState(),
                users.getLga(),users.getFacility());

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

        private void senUserToLoginActivity () {
            Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }

    private void initializedFields() {
        mFirstname = findViewById(R.id.firstname);
        mLastname = findViewById(R.id.lastname);
        mPhone = findViewById(R.id.phone);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mUserTypeSpinner = findViewById(R.id.userType_spinner);
        mStateSpinner = findViewById(R.id.stateSpinner);
        mLgaSpinner = findViewById(R.id.lgaSpinner);
        mFacilitySpinner = findViewById(R.id.facilitySpinner);
        mButtonRegister = findViewById(R.id.btnRegister);
        mLoginLink = findViewById(R.id.loginLink);
        mGender = findViewById(R.id.genderSpinner);
        mAgeSpinner = findViewById(R.id.age_spinner);
        loadingBar = new ProgressDialog(this);

    }
}