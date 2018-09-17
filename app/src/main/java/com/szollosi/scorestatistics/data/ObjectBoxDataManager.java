package com.szollosi.scorestatistics.data;

import android.support.annotation.NonNull;

import java.util.Set;
import java.util.TreeSet;

public class ObjectBoxDataManager implements DataManaging {
  @Override
  public Set<String> getTeams() {
    return new TreeSet<>();
  }

  @Override
  public void reset() {

  }

  @Override
  public void saveScore(@NonNull String teamA, @NonNull String teamB, int scoreA, int scoreB) {

  }

  @Override
  public boolean saveTeamName(@NonNull String teamName) {
    return false;
  }
}
