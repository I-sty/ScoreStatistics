package com.szollosi.scorestatistics;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Base class for maintaining global application state.
 */
public class ScoreStatistics extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    if (LeakCanary.isInAnalyzerProcess(this)) {
      return;
    }
    LeakCanary.install(this);
  }
}
