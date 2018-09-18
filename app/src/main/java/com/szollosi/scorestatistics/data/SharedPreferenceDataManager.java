package com.szollosi.scorestatistics.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.util.Set;
import java.util.TreeSet;

public class SharedPreferenceDataManager implements DataManaging {
  private static final String KEY_CG = "_CG";

  private static final String KEY_PLAYED_MATCHES = "_PM";

  private static final String KEY_SG = "_SG";

  private static final String KEY_TEAMS = "teams";

  private static final String KEY_WINS = "_W";

  private static final String TAG = SharedPreferenceDataManager.class.getName();

  private final SharedPreferences sharedPreferences;

  public SharedPreferenceDataManager(@NonNull Context context) {
    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
  }

  @Nullable
  @Override
  public Score getScoreByTeam(@NonNull String teamName) {
    if (TextUtils.isEmpty(teamName)) {
      return null;
    }
    return new Score(sharedPreferences.getInt(teamName + KEY_PLAYED_MATCHES, 0),
        sharedPreferences.getInt(teamName + KEY_WINS, 0), sharedPreferences.getInt(teamName + KEY_SG, 0),
        sharedPreferences.getInt(teamName + KEY_CG, 0), teamName);
  }

  @Override
  public Set<String> getTeams() {
    return new TreeSet<>(sharedPreferences.getStringSet(KEY_TEAMS, new TreeSet<>()));
  }

  @Override
  public void reset() {
    sharedPreferences.edit().clear().apply();
  }

  @Override
  public void saveScore(@NonNull String teamA, @NonNull String teamB, int scoreA, int scoreB) {
    SharedPreferences.Editor editor = sharedPreferences.edit();
    increasePlayedMatches(editor, teamA, teamB);
    updateScores(editor, teamA, teamB, scoreA, scoreB);
    if (scoreA > scoreB) {
      updateWins(editor, teamA);
    } else if (scoreA < scoreB) {
      updateWins(editor, teamB);
    }
    editor.apply();
  }

  @Override
  public boolean saveTeamName(@NonNull String teamName) {
    Set<String> teams = getTeams();
    boolean result;
    try {
      result = teams.add(teamName);
    } catch (Exception e) {
      Log.e(TAG, "[saveTeamName] Cannot add element into the TreeSet. Exception: " + e);
      // TODO: 9/17/18 Extra error handling
      return false;
    }
    if (result) {
      sharedPreferences.edit().putStringSet(KEY_TEAMS, teams).apply();
    }
    return result;
  }

  private void increasePlayedMatches(SharedPreferences.Editor editor, String teamA, String teamB) {
    String keyA = teamA + KEY_PLAYED_MATCHES;
    int playedMatches = sharedPreferences.getInt(keyA, 0);
    editor.putInt(keyA, playedMatches + 1);

    String keyB = teamB + KEY_PLAYED_MATCHES;
    playedMatches = sharedPreferences.getInt(keyB, 0);
    editor.putInt(keyB, playedMatches + 1);
  }

  private void updateScores(SharedPreferences.Editor editor, String teamA, String teamB, int scoreGoalsTeamA,
      int scoreGoalsTeamB) {
    int scoredGoalsTeamA = sharedPreferences.getInt(teamA + KEY_SG, 0);
    editor.putInt(teamA + KEY_SG, scoredGoalsTeamA + scoreGoalsTeamA);

    int concededGoalsTeamA = sharedPreferences.getInt(teamA + KEY_CG, 0);
    editor.putInt(teamA + KEY_CG, concededGoalsTeamA + scoreGoalsTeamB);

    int scoredGoalsTeamB = sharedPreferences.getInt(teamB + KEY_SG, 0);
    editor.putInt(teamB + KEY_SG, scoredGoalsTeamB + scoreGoalsTeamB);

    int concededGoalsTeamB = sharedPreferences.getInt(teamB + KEY_CG, 0);
    editor.putInt(teamB + KEY_CG, concededGoalsTeamB + scoreGoalsTeamA);
  }

  private void updateWins(SharedPreferences.Editor editor, String winner) {
    int victories = sharedPreferences.getInt(winner + KEY_WINS, 0);
    editor.putInt(winner + KEY_WINS, victories + 1);
  }
}
