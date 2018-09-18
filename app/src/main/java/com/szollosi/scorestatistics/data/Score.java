package com.szollosi.scorestatistics.data;

/**
 * Data class for the Score object. This class contains a results of a team.
 */
public class Score {
  private final int concededGoals;

  private final int playedMatches;

  private final int scoredGoals;

  private final String teamName;

  private final int wins;

  /**
   * Constructor
   *
   * @param playedMatches The number of played matches
   * @param wins The number of win
   * @param scoredGoals Scored goals
   * @param concededGoals Conceded goals
   * @param teamName The name of the team
   */
  public Score(int playedMatches, int wins, int scoredGoals, int concededGoals, String teamName) {
    this.playedMatches = playedMatches;
    this.wins = wins;
    this.scoredGoals = scoredGoals;
    this.concededGoals = concededGoals;
    this.teamName = teamName;
  }

  public int getConcededGoals() {
    return concededGoals;
  }

  public int getPlayedMatches() {
    return playedMatches;
  }

  public int getScoredGoals() {
    return scoredGoals;
  }

  public String getTeamName() {
    return teamName;
  }

  public int getWins() {
    return wins;
  }
}
