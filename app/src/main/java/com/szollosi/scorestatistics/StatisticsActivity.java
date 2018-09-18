package com.szollosi.scorestatistics;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.szollosi.scorestatistics.data.Score;
import com.szollosi.scorestatistics.data.SharedPreferenceDataManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class StatisticsActivity extends AppCompatActivity {

  private static final TableRow.LayoutParams LAYOUT_ROW =
      new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

  private static final TableLayout.LayoutParams LAYOUT_TABLE =
      new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);

  private Drawable border;

  private int[][] scoredMatrix;

  private ArrayList<Score> scores;

  private String[] statHeaders;

  private TableLayout statTable;

  private String[] teams;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_statistics);
    TableLayout scoreTable = findViewById(R.id.tableLayoutScore);
    statTable = findViewById(R.id.tableLayoutStatistics);
    SharedPreferenceDataManager dataManaging = new SharedPreferenceDataManager(this);
    border = ContextCompat.getDrawable(this, R.drawable.border);
    Set<String> set = dataManaging.getTeams();
    this.teams = set.toArray(new String[set.size()]);
    scores = new ArrayList<>();
    for (String team : teams) {
      scores.add(dataManaging.getScoreByTeam(team));
    }

    statHeaders = getResources().getStringArray(R.array.array_statistics);

    generateScoreTable(scoreTable);
    generateStatisticTable(statTable);
    Spinner spinnerOrder = findViewById(R.id.spinnerOrder);
    spinnerOrder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int index = parent.getSelectedItemPosition();
        Order order = Order.values()[index];
        generateStatisticTableContent(order);
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });
  }

  private void clearStatisticTable() {
    while (statTable.getChildCount() > 1) {
      statTable.removeViewAt(1);
    }
  }

  private void generateScoreTable(TableLayout scoreTable) {
    TableRow headerRow = new TableRow(this);
    TextView child = new TextView(this);
    child.setText(R.string.label_name_team);
    child.setPadding(30, 5, 30, 5);
    headerRow.addView(child, LAYOUT_ROW);

    // make the header row
    for (String team : teams) {
      TextView textView = new TextView(this);
      textView.setText(team);
      textView.setPadding(30, 5, 30, 5);
      headerRow.addView(textView, LAYOUT_ROW);
    }
    scoreTable.addView(headerRow, LAYOUT_TABLE);

    generateScoredMatrix(teams.length);

    // make the content of the table
    for (int i = 0; i < teams.length; ++i) {
      String team = teams[i];
      TableRow row = new TableRow(this);
      TextView textView = new TextView(this);
      textView.setText(team);
      textView.setPadding(30, 5, 30, 5);
      row.addView(textView, LAYOUT_ROW);
      for (int j = 0; j < teams.length; ++j) {
        child = new TextView(this);
        if (i == j) {
          child.setText(getString(R.string.not_available));
          child.setTypeface(null, Typeface.BOLD);
        } else {
          child.setText(getScoreValue(i, j));
        }
        child.setGravity(Gravity.CENTER);
        child.setBackground(border);
        child.setPadding(30, 5, 30, 5);
        row.addView(child, LAYOUT_ROW);
      }
      scoreTable.addView(row, LAYOUT_TABLE);
    }
  }

  private void generateScoredMatrix(final int length) {
    scoredMatrix = new int[length][length];
    for (int i = 0; i < length; ++i) {
      for (int j = 1 + i; j < length; ++j) {
        int value = new Random().nextInt(100);
        scoredMatrix[i][j] = scoredMatrix[j][i] = value;
      }

    }
  }

  private void generateStatRow(TableRow row, Object content) {
    TextView child = new TextView(this);
    child.setText(String.valueOf(content));
    child.setGravity(Gravity.CENTER);
    child.setBackground(border);
    child.setPadding(30, 5, 30, 5);
    row.addView(child, LAYOUT_ROW);
  }

  private void generateStatisticTable(TableLayout statTable) {
    TableRow headerRow = new TableRow(this);
    headerRow.addView(new TextView(this), LAYOUT_ROW);

    for (String header : statHeaders) {
      TextView textView = new TextView(this);
      textView.setText(header);
      textView.setPadding(30, 5, 30, 5);
      headerRow.addView(textView, LAYOUT_ROW);
    }
    statTable.addView(headerRow, LAYOUT_TABLE);
  }

  private void generateStatisticTableContent(Order order) {
    clearStatisticTable();
    for (Score score : getScoresByOrder(order)) {
      TableRow row = new TableRow(this);
      TextView textView = new TextView(this);
      textView.setText(score.getTeamName());
      textView.setPadding(30, 5, 30, 5);
      row.addView(textView, LAYOUT_ROW);
      generateStatRow(row, score.getPlayedMatches());
      generateStatRow(row, score.getWins());
      generateStatRow(row, score.getScoredGoals());
      generateStatRow(row, score.getConcededGoals());
      statTable.addView(row, LAYOUT_TABLE);
    }
  }

  /**
   * Returns a value from the scored matrix
   *
   * @param i The row
   * @param j The column
   *
   * @return The returned item
   */
  private String getScoreValue(int i, int j) {
    return String.valueOf(scoredMatrix[i][j]);
  }

  /**
   * Sorts the scores list according to the given order.
   * If two teams has the same result the order between them will be alphabetic.
   *
   * @param order The criteria of the order
   *
   * @return The ordered list
   */
  private List<Score> getScoresByOrder(Order order) {
    switch (order) {
      case PLAYED_MATCHES:
        Collections.sort(scores, (o1, o2) -> {
          int result = o2.getPlayedMatches() - o1.getPlayedMatches();
          if (result == 0) {
            return o1.getTeamName().compareTo(o2.getTeamName());
          }
          return result;
        });
        break;
      case WINS:
        Collections.sort(scores, (o1, o2) -> {
          int result = o2.getWins() - o1.getWins();
          if (result == 0) {
            return o1.getTeamName().compareTo(o2.getTeamName());
          }
          return result;
        });
        break;
      case SCORED_GOALS:
        Collections.sort(scores, (o1, o2) -> {
          int result = o2.getScoredGoals() - o1.getScoredGoals();
          if (result == 0) {
            return o1.getTeamName().compareTo(o2.getTeamName());
          }
          return result;
        });
        break;
      case CONCEDED_GOALS:
        Collections.sort(scores, (o1, o2) -> {
          int result = o2.getConcededGoals() - o1.getConcededGoals();
          if (result == 0) {
            return o1.getTeamName().compareTo(o2.getTeamName());
          }
          return result;
        });
        break;
    }
    return scores;
  }
}
