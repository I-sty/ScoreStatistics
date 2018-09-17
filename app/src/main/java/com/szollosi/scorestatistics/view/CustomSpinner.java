package com.szollosi.scorestatistics.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Spinner;

public class CustomSpinner extends android.support.v7.widget.AppCompatSpinner {
  /**
   * An interface which a client of this Spinner could use to receive
   * open/closed events for this Spinner.
   */
  public interface OnSpinnerEventsListener {

    /**
     * Callback triggered when the spinner was closed.
     */
    void onSpinnerClosed(Spinner spinner);

    /**
     * Callback triggered when the spinner was opened.
     */
    void onSpinnerOpened(Spinner spinner);

  }

  private OnSpinnerEventsListener mListener;

  private boolean mOpenInitiated = false;

  /**
   * Construct a new spinner with the given context's theme and the supplied attribute set.
   *
   * @param context The Context the view is running in, through which it can
   * access the current theme, resources, etc.
   * @param attrs The attributes of the XML tag that is inflating the view.
   */
  public CustomSpinner(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  /**
   * Constructs a new spinner with the given context's theme.
   *
   * @param context The Context the view is running in, through which it can
   * access the current theme, resources, etc.
   */
  public CustomSpinner(Context context) {
    super(context);
  }

  // implement the Spinner constructors that you need

  /**
   * A boolean flag indicating that the Spinner triggered an open event.
   *
   * @return true for opened Spinner
   */
  public boolean hasBeenOpened() {
    return mOpenInitiated;
  }

  public void onWindowFocusChanged(boolean hasFocus) {
    if (hasBeenOpened() && hasFocus) {
      performClosedEvent();
    }
  }

  @Override
  public boolean performClick() {
    // register that the Spinner was opened so we have a status
    // indicator for when the container holding this Spinner may lose focus
    mOpenInitiated = true;
    if (mListener != null) {
      mListener.onSpinnerOpened(this);
    }
    return super.performClick();
  }

  /**
   * Propagate the closed Spinner event to the listener from outside if needed.
   */
  public void performClosedEvent() {
    mOpenInitiated = false;
    if (mListener != null) {
      mListener.onSpinnerClosed(this);
    }
  }

  /**
   * Register the listener which will listen for events.
   */
  public void setSpinnerEventsListener(OnSpinnerEventsListener onSpinnerEventsListener) {
    mListener = onSpinnerEventsListener;
  }

}
