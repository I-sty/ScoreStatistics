package com.szollosi.scorestatistics;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.szollosi.scorestatistics.data.DataManaging;
import com.szollosi.scorestatistics.data.SharedPreferenceDataManager;
import com.szollosi.scorestatistics.view.CustomSpinner;

import java.util.Set;
import java.util.TreeSet;

public class LandingActivity extends AppCompatActivity {

  private DataManaging dataManaging;

  private EditText editTextTeamA;

  private EditText editTextTeamB;

  private CustomSpinner spinnerTeamA;

  private CustomSpinner spinnerTeamB;

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

    Set<String> teamsSet = dataManaging.getTeams();
    String[] teams = teamsSet.toArray(new String[teamsSet.size()]);
    if (teams.length == 0) {
      Toast.makeText(LandingActivity.this, R.string.warning_empty_list, Toast.LENGTH_LONG).show();
      return;
    }
    ArrayAdapter<String> spinnerTeamAdaptor =
        new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, teams);
    spinnerTeamA.setAdapter(spinnerTeamAdaptor);
    spinnerTeamB.setSpinnerEventsListener(new CustomSpinner.OnSpinnerEventsListener() {
      @Override
      public void onSpinnerClosed(Spinner spinner) {
        Toast.makeText(LandingActivity.this, "closed", Toast.LENGTH_SHORT).show();
      }

      @Override
      public void onSpinnerOpened(Spinner spinner) {
        Toast.makeText(LandingActivity.this, "opened", Toast.LENGTH_SHORT).show();
        Set<String> set = new TreeSet<>(teamsSet);
        set.remove(spinnerTeamA.getSelectedItem());
        String[] teams = set.toArray(new String[set.size()]);
        spinnerTeamB
            .setAdapter(new ArrayAdapter<>(LandingActivity.this, android.R.layout.simple_spinner_dropdown_item, teams));
      }
    });
  }

  public void addTeam(View view) {
    AlertDialog.Builder alertDialog = new AlertDialog.Builder(LandingActivity.this);
    final EditText input = new EditText(LandingActivity.this);
    input.setSingleLine();
    input.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
    input.setKeyListener(DigitsKeyListener.getInstance("qwertzuiopasdfghjklyxcvbnmQWERTZUIOPASDFGHJKLYXCVBNM "));
    LinearLayout.LayoutParams lp =
        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
    input.setLayoutParams(lp);
    alertDialog.setView(input);
    alertDialog.setTitle(R.string.label_name_team);
    alertDialog.setPositiveButton(R.string.label_save, (dialog, which) -> {
      final String teamName = input.getText().toString();
      if (isValidTeamName(teamName)) {
        dataManaging.saveTeamName(teamName);
        dialog.dismiss();
      } else {
        Toast.makeText(LandingActivity.this, R.string.warning_invalid_team_name, Toast.LENGTH_LONG).show();
      }
    });
    alertDialog.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel());
    alertDialog.show();
  }

  public void resetStat(View view) {
    dataManaging.reset();
  }

  public void showStat(View view) {
    startActivity(new Intent(LandingActivity.this, StatisticsActivity.class));
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_landing);
    dataManaging = new SharedPreferenceDataManager(getApplicationContext());
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
