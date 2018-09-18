package com.szollosi.scorestatistics.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Spinner;

/**
 * Open-Close Spinner (OCSpinner)
 */
public class OCSpinner extends android.support.v7.widget.AppCompatSpinner {
  /**
   * An interface to receive open/closed events for this Spinner.
   */
  public interface OnSpinnerEventsListener {

    /**
     * Callback triggered when the spinner was closed.
     *
     * @param spinner The spinner that was triggered
     */
    void onSpinnerClosed(Spinner spinner);

    /**
     * Callback triggered when the spinner was opened.
     *
     * @param spinner The spinner that was triggered
     */
    void onSpinnerOpened(Spinner spinner);

  }

  /** Spinner event listener */
  private OnSpinnerEventsListener mListener;

  private boolean opened = false;

  /**
   * Construct a new spinner with the given context's theme and the supplied attribute set.
   *
   * @param context The Context the view is running in, through which it can
   * access the current theme, resources, etc.
   * @param attrs The attributes of the XML tag that is inflating the view.
   */
  public OCSpinner(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  /**
   * Constructs a new spinner with the given context's theme.
   *
   * @param context The Context the view is running in, through which it can
   * access the current theme, resources, etc.
   */
  public OCSpinner(Context context) {
    super(context);
  }

  /**
   * A boolean flag indicating that the Spinner triggered an open event.
   *
   * @return true for opened Spinner
   */
  public boolean hasBeenOpened() {
    return opened;
  }

  public void onWindowFocusChanged(boolean hasFocus) {
    if (hasBeenOpened() && hasFocus) {
      performClosedEvent();
    }
  }

  @Override
  public boolean performClick() {
    opened = true;
    if (mListener != null) {
      mListener.onSpinnerOpened(this);
    }
    return super.performClick();
  }

  /**
   * Propagate the closed Spinner event to the listener from outside if needed.
   */
  public void performClosedEvent() {
    opened = false;
    if (mListener != null) {
      mListener.onSpinnerClosed(this);
    }
  }

  /**
   * Register the listener which will listen for events.
   *
   * @param onSpinnerEventsListener The event listener
   */
  public void setSpinnerEventsListener(OnSpinnerEventsListener onSpinnerEventsListener) {
    mListener = onSpinnerEventsListener;
  }

}
