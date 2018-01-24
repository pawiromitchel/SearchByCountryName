package sr.unasat.rest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import sr.unasat.rest.dto.CountryDto;

public class MainActivity extends AppCompatActivity {

    private TextView mTextView;
    private EditText CountryNameEditText;
    private TextView countryNameText;
    private TextView countryCapitalText;
    private TextView countryRegionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.text);
        CountryNameEditText = (EditText) findViewById(R.id.searchCountryName);

        // country details
        countryNameText = (TextView) findViewById(R.id.countryNameText);
        countryCapitalText = (TextView) findViewById(R.id.countryCapitalText);
        countryRegionText = (TextView) findViewById(R.id.countryRegionText);
    }

    private void getCountryData(String countryName) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        // final String ALL_COUNTRIES = "https://restcountries.eu/rest/v2/all";
        final String ONE_COUNTRY = "https://restcountries.eu/rest/v2/name/" + countryName;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ONE_COUNTRY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        List<CountryDto> countryDto = mapJsonToCountryObject(response);

                        // place the values in the TextViews
                        countryNameText.setText(countryDto.get(0).getName());
                        countryCapitalText.setText(countryDto.get(0).getCapital());
                        countryRegionText.setText(countryDto.get(0).getRegion());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mTextView.setText("something went wrong");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


    private List<CountryDto> mapJsonToCountryObject(String jsonArray) {
        ObjectMapper mapper = new ObjectMapper();
        List<CountryDto> countryList = new ArrayList<>();
        List<Map<String, ?>> countryArray = null;
        CountryDto country = null;

        try {
            countryArray = mapper.readValue(jsonArray, List.class);
            for (Map<String, ?> map : countryArray) {
                country = new CountryDto((String) map.get("name"), (String) map.get("capital"), (String) map.get("region"));
                countryList.add(country);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Er is wat fout gegaan bij het parsen van de json data");
        }
        return countryList;
    }

    public void searchCountry(View view) {
        getCountryData(CountryNameEditText.getText().toString());
    }
}
