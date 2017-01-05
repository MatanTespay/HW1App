package controller;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.matan.hw1app.R;

import model.GlobalVariables;
import utils.HelperClass;

public class userProfileActivity extends Activity {
	private static String NAME_KEY = "name_key";
	private static String AGE_KEY = "age_key";
	private static String COUNTRY_KEY = "country_key";
	private SharedPreferences prefs = null;
	private RadioGroup radioGroup;
	private RadioButton radioButton;
	private TextView txtName;
	Spinner countrySpin;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setTitle(R.string.ProfileTitle);
		prefs = getPreferences(MODE_PRIVATE);

		setContentView(R.layout.user);

        // populate all countries in the spinner
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, GlobalVariables.getInstance().getCountries());

		countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
		countrySpin = (Spinner) findViewById(R.id.countrySpin);
		countrySpin.setAdapter(countryAdapter);

		txtName = (TextView) findViewById(R.id.txtName);

		radioGroup = (RadioGroup) findViewById(R.id.ageGroup);

        // load user info
		loadInfo();

		Button saveInfoBtn = (Button) findViewById(R.id.btnSaveInfo);

		saveInfoBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				// Get and edit info
				SharedPreferences.Editor editor = prefs.edit();
				editor.putString(NAME_KEY, txtName.getText().toString());
				String country = (String)countrySpin.getSelectedItem();

				editor.putString(COUNTRY_KEY, country);

				int selectedAge = radioGroup.getCheckedRadioButtonId();
				// find the radiobutton by returned id
				radioButton = (RadioButton) findViewById(selectedAge);
				editor.putInt(AGE_KEY,selectedAge );
				editor.commit();
				GlobalVariables.getInstance().showToast(R.string.saveInfoMsg,null);
			}
		});
		
	}

    /**
     * get info from Preferences data and set it on the screen
     */
	private void loadInfo(){

		txtName.setText(String.valueOf(prefs.getString(NAME_KEY,"")));

		radioGroup.check(prefs.getInt(AGE_KEY,0));

        // set the saved country as select on the spinner
		int idx = GlobalVariables.getInstance().getCountryIdxByName(prefs.getString(COUNTRY_KEY,""));
		countrySpin.setSelection(idx != -1 ? idx : 0);
	}
}