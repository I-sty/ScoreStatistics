package com.szollosi.scorestatistics.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Set;

/**
 * Data managing interface.
 */
public interface DataManaging {
  /**
   * Gets the saved scored for a team.
   *
   * @param teamName The team to get saved score.
   *
   * @return The saved score of a team or null if the team does not exist in the Shared Preference
   */
  @Nullable
  Score getScoreByTeam(@NonNull String teamName);

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
