package com.example.currencyexchange;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    EditText amountInput, searchBar;
    Spinner fromCurrency, toCurrency;
    TextView resultAmount, rateText;
    Button convertButton;
    RecyclerView ratesRecyclerView;
    RateAdapter rateAdapter;

    List<RateItem> allRates = new ArrayList<>();
    List<RateItem> filteredRates = new ArrayList<>();
    Map<String, Double> ratesMap = new HashMap<>();

    OkHttpClient client = new OkHttpClient();
    Handler mainHandler = new Handler(Looper.getMainLooper());

    String[] currencies = {"USD", "EUR", "GBP", "JPY", "CAD", "AUD", "CHF", "CNY", "INR", "MXN", "SGD", "HKD"};
    String[] flags = {"🇺🇸", "🇪🇺", "🇬🇧", "🇯🇵", "🇨🇦", "🇦🇺", "🇨🇭", "🇨🇳", "🇮🇳", "🇲🇽", "🇸🇬", "🇭🇰"};
    String[] names = {"US Dollar", "Euro", "British Pound", "Japanese Yen", "Canadian Dollar", "Australian Dollar", "Swiss Franc", "Chinese Yuan", "Indian Rupee", "Mexican Peso", "Singapore Dollar", "Hong Kong Dollar"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        amountInput = findViewById(R.id.amountInput);
        searchBar = findViewById(R.id.searchBar);
        fromCurrency = findViewById(R.id.fromCurrency);
        toCurrency = findViewById(R.id.toCurrency);
        resultAmount = findViewById(R.id.resultAmount);
        rateText = findViewById(R.id.rateText);
        convertButton = findViewById(R.id.convertButton);
        ratesRecyclerView = findViewById(R.id.ratesRecyclerView);

        // Setup spinners
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, currencies);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromCurrency.setAdapter(adapter);
        toCurrency.setAdapter(adapter);
        toCurrency.setSelection(1); // EUR default

        // Setup RecyclerView
        rateAdapter = new RateAdapter(this, filteredRates);
        ratesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ratesRecyclerView.setAdapter(rateAdapter);

        // Fetch rates
        fetchRates();

        // Convert button
        convertButton.setOnClickListener(v -> convertCurrency());

        // Swap button
        findViewById(R.id.swapButton).setOnClickListener(v -> {
            int fromPos = fromCurrency.getSelectedItemPosition();
            int toPos = toCurrency.getSelectedItemPosition();
            fromCurrency.setSelection(toPos);
            toCurrency.setSelection(fromPos);
            convertCurrency();
        });

        // Search bar
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterRates(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void fetchRates() {
        String url = "https://api.frankfurter.app/latest?base=USD";
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mainHandler.post(() ->
                        Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String json = response.body().string();
                        JSONObject obj = new JSONObject(json);
                        JSONObject rates = obj.getJSONObject("rates");

                        ratesMap.clear();
                        ratesMap.put("USD", 1.0);
                        Iterator<String> keys = rates.keys();
                        while (keys.hasNext()) {
                            String key = keys.next();
                            ratesMap.put(key, rates.getDouble(key));
                        }

                        mainHandler.post(() -> {
                            buildRatesList();
                            convertCurrency();
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void buildRatesList() {
        allRates.clear();
        for (int i = 0; i < currencies.length; i++) {
            String code = currencies[i];
            double rate = ratesMap.containsKey(code) ? ratesMap.get(code) : 0.0;
            double change = Math.random() * 0.6 - 0.3;
            allRates.add(new RateItem(flags[i], code, names[i], rate, change));
        }
        filteredRates.clear();
        filteredRates.addAll(allRates);
        rateAdapter.updateList(filteredRates);
    }

    private void filterRates(String query) {
        filteredRates.clear();
        for (RateItem item : allRates) {
            if (item.getCode().toLowerCase().contains(query.toLowerCase()) ||
                    item.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredRates.add(item);
            }
        }
        rateAdapter.updateList(filteredRates);
    }

    private void convertCurrency() {
        String fromCode = currencies[fromCurrency.getSelectedItemPosition()];
        String toCode = currencies[toCurrency.getSelectedItemPosition()];

        String amountStr = amountInput.getText().toString();
        if (amountStr.isEmpty()) {
            resultAmount.setText("0.00");
            return;
        }

        double amount = Double.parseDouble(amountStr);

        if (!ratesMap.containsKey(fromCode) || !ratesMap.containsKey(toCode)) {
            Toast.makeText(this, "Rates not loaded yet", Toast.LENGTH_SHORT).show();
            return;
        }

        double fromRate = ratesMap.get(fromCode);
        double toRate = ratesMap.get(toCode);
        double result = amount * (toRate / fromRate);
        double rate = toRate / fromRate;

        resultAmount.setText(String.format("%,.2f", result));
        rateText.setText(String.format("1 %s = %.4f %s", fromCode, rate, toCode));
    }
}