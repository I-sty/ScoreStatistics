package com.szollosi.scorestatistics.data;

import android.support.annotation.NonNull;

import java.util.Set;

public interface DataManaging {
  /**
   * Gets all teams from the Shared Preference.
   *
   * @return A list with the team names.
   */
  Set<String> getTeams();

  /**
   * Resets the Shared Preference, a.k.a remove any elements from it.
   */
  void reset();

  /**
   * Saves the newly entered match score into the Shared Preference.
   *
   * @param teamA The first team in the score board
   * @param teamB The second team in the score board
   * @param scoreA The number of scored goals of the first team
   * @param scoreB The number of scored goals of the second team
   */
  void saveScore(@NonNull String teamA, @NonNull String teamB, int scoreA, int scoreB);

  /**
   * Saves the newly entered team name into the Shared Preference.
   *
   * @param teamName The name to save
   *
   * @return true if the name is saved, false in case of any errors during the saving operation
   */
  boolean saveTeamName(@NonNull String teamName);
}
