package com.szollosi.scorestatistics;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.szollosi.scorestatistics.data.DataManaging;
import com.szollosi.scorestatistics.data.SharedPreferenceDataManager;
import com.szollosi.scorestatistics.view.OCSpinner;

import java.util.ArrayList;

/**
 * The entry point of the application
 */
public class LandingActivity extends AppCompatActivity {

  private static final String EMPTY_LIST_ITEM = "-";

  /** Log tag */
  private static final String TAG = LandingActivity.class.getName();

  /** Data manager */
  private DataManaging dataManaging;

  /** The first EditText (for the score of the team) from the 'add-score-dialog' */
  private EditText editTextTeamA;

  /** The second EditText (for the score of the team) from the 'add-score-dialog' */
  private EditText editTextTeamB;

  /** The first dropdown from the 'add-score-dialog' */
  private OCSpinner spinnerTeamA;

  /** The second dropdown (for the name of the team) from the 'add-score-dialog' */
  private OCSpinner spinnerTeamB;

  /**
   * Opens a new dialog to add a new score
   *
   * @param view The clicked view
   */
  public void addScore(View view) {
    AlertDialog.Builder alertDialog = new AlertDialog.Builder(LandingActivity.this);
    alertDialog.setView(R.layout.alert_add_score);
    alertDialog.setTitle(R.string.label_add_score);
    alertDialog.setPositiveButton(R.string.label_save, (dialog, which) -> {
      int scoreA = Integer.parseInt(editTextTeamA.getText().toString());
      int scoreB = Integer.parseInt(editTextTeamB.getText().toString());
      dataManaging
          .saveScore(String.valueOf(spinnerTeamA.getSelectedItem()), String.valueOf(spinnerTeamB.getSelectedItem()),
              scoreA, scoreB);
      dialog.dismiss();
    });
    alertDialog.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel());
    AlertDialog dialog = alertDialog.show();

    //gets views
    spinnerTeamA = dialog.findViewById(R.id.spinnerTeamA);
    spinnerTeamB = dialog.findViewById(R.id.spinnerTeamB);
    editTextTeamA = dialog.findViewById(R.id.editTextTeamA);
    editTextTeamB = dialog.findViewById(R.id.editTextTeamB);

    ArrayList<String> teamsSet = new ArrayList<>(dataManaging.getTeams());
    teamsSet.add(0, EMPTY_LIST_ITEM);
    String[] teams = teamsSet.toArray(new String[teamsSet.size()]);
    if (teams.length == 0) {
      Toast.makeText(LandingActivity.this, R.string.warning_empty_list, Toast.LENGTH_LONG).show();
      return;
    }
    ArrayAdapter<String> spinnerTeamAdaptor = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, teams);
    spinnerTeamA.setAdapter(spinnerTeamAdaptor);
    spinnerTeamA.setSpinnerEventsListener(new OCSpinner.OnSpinnerEventsListener() {
      @Override
      public void onSpinnerClosed(Spinner spinner) {

      }

      @Override
      public void onSpinnerOpened(Spinner spinner) {
        ArrayList<String> set = new ArrayList<>(teamsSet);
        String selectedItem = String.valueOf(spinnerTeamB.getSelectedItem());
        if (!selectedItem.equals(EMPTY_LIST_ITEM)) {
          set.remove(selectedItem);
        }
        String[] teams = set.toArray(new String[set.size()]);
        spinner.setAdapter(new ArrayAdapter<>(LandingActivity.this, android.R.layout.simple_spinner_dropdown_item, teams));
      }
    });
    spinnerTeamB.setSpinnerEventsListener(new OCSpinner.OnSpinnerEventsListener() {
      @Override
      public void onSpinnerClosed(Spinner spinner) {
      }

      @Override
      public void onSpinnerOpened(Spinner spinner) {
        ArrayList<String> set = new ArrayList<>(teamsSet);
        String selectedItem = String.valueOf(spinnerTeamA.getSelectedItem());
        if (!selectedItem.equals(EMPTY_LIST_ITEM)) {
          set.remove(selectedItem);
        }
        String[] teams = set.toArray(new String[set.size()]);
        spinner.setAdapter(new ArrayAdapter<>(LandingActivity.this, android.R.layout.simple_spinner_dropdown_item, teams));
      }
    });
  }

  /**
   * Opens a dialog to add a new team
   *
   * @param view The clicked view
   */
  public void addTeam(View view) {
    AlertDialog.Builder alertDialog = new AlertDialog.Builder(LandingActivity.this);
    final EditText input = new EditText(LandingActivity.this);
    input.setSingleLine();
    input.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
    input.setKeyListener(DigitsKeyListener.getInstance("qwertzuiopasdfghjklyxcvbnmQWERTZUIOPASDFGHJKLYXCVBNM "));
    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
    input.setLayoutParams(lp);
    alertDialog.setView(input);
    alertDialog.setTitle(R.string.label_name_team);
    alertDialog.setPositiveButton(R.string.label_save, (dialog, which) -> {
      final String teamName = input.getText().toString();
      if (isValidTeamName(teamName)) {
        Log.i(TAG, "addTeam: " + dataManaging.saveTeamName(teamName));
        dialog.dismiss();
      } else {
        Toast.makeText(LandingActivity.this, R.string.warning_invalid_team_name, Toast.LENGTH_LONG).show();
      }
    });
    alertDialog.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel());
    alertDialog.show();
  }

  /**
   * Resets the data structure
   *
   * @param view The clicked view
   */
  public void resetStat(View view) {
    dataManaging.reset();
  }

  /**
   * Starts the {@link StatisticsActivity}
   *
   * @param view The clicked view
   */
  public void showStat(View view) {
    startActivity(new Intent(LandingActivity.this, StatisticsActivity.class));
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_landing);
    dataManaging = new SharedPreferenceDataManager(this);
  }

  /**
   * Checks if the given team name is a valid one and this is a new name in the SP.
   *
   * @param teamName The given name to check
   *
   * @return true if the name should be save, otherwise false
   */
  private boolean isValidTeamName(@NonNull String teamName) {
    if (teamName.isEmpty()) {
      return false;
    }
    if (dataManaging.getTeams().contains(teamName)) {
      return false;
    }
    return true;
  }
}
