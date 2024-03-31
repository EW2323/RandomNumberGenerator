package activities;

import static androidx.preference.PreferenceManager.getDefaultSharedPreferences;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.randomnumbergenerator.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.PersistableBundle;
import android.view.View;

import com.example.randomnumbergenerator.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import libs.Utils;
import model.RandomNumber;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private RandomNumber mRandomNumber;
    private ArrayList<Integer> mNumberHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setContentView(R.layout.activity_main);

        mRandomNumber = new RandomNumber();
        initializeHistoryList(savedInstanceState, "history_key");

        //setSupportActionBar(binding.toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFabClick();
            }
        });
    }
    private void initializeHistoryList (Bundle savedInstanceState, String key)
    {
        SharedPreferences defaultSharedPreferences = getDefaultSharedPreferences(this);

        if (savedInstanceState != null) {
            mNumberHistory = savedInstanceState.getIntegerArrayList (key);
        }
        else {
            String history = getDefaultSharedPreferences (this).getString (key, null);
            mNumberHistory = history == null ?
                    new ArrayList<> () : Utils.getNumberListFromJSONString (history);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState);
        outState.putIntegerArrayList("history_key", mNumberHistory);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.menu_show_history) {
            Utils.showInfoDialog(MainActivity.this, "History", mNumberHistory.toString());
            return true;
        } else if (itemId == R.id.menu_clear_history) {
            mNumberHistory.clear();
            return true;
        } else if (itemId == R.id.menu_about) {
            Snackbar.make(findViewById(android.R.id.content), getString(R.string.about_text), Snackbar.LENGTH_LONG).show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void onFabClick() {
            TextView fromNumberText = findViewById(R.id.from_number);
            TextView toNumberText = findViewById(R.id.to_number);
            TextView randomNumberText = findViewById(R.id.random_number);

            if (fromNumberText.getText().length() == 0 || toNumberText.getText().length() == 0) {
                // Show error message if from/to numbers are not entered
                Toast.makeText(this, "Please enter both numbers", Toast.LENGTH_SHORT).show();
                return;
            }
            int fromNumber = Integer.parseInt(fromNumberText.getText().toString());
            int toNumber = Integer.parseInt(toNumberText.getText().toString());

            mRandomNumber.setFromTo(fromNumber, toNumber);
            int randomNumber = mRandomNumber.getCurrentRandomNumber();
            mNumberHistory.add(randomNumber);
            randomNumberText.setText(String.valueOf(randomNumber));
        }
}