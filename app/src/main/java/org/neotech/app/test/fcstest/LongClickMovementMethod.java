package org.neotech.app.test.fcstest;

import android.text.Layout;
import android.text.Spannable;
import android.text.method.MovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.TextView;

/**
 * A simple quick and dirty movement method that traverses LongClickableSpans in the text buffer and
 * scrolls if necessary. Supports clicking and long clicking on LongClickableSpans.
 */
public class LongClickMovementMethod extends ScrollingMovementMethod {

    private static final int LONG_PRESS_TIME_OUT = ViewConfiguration.getLongPressTimeout();
    private static int SCALED_TOUCH_SLOP = 0;

    private int firstX;
    private int firstY;

    private Runnable pendingLongPress;
    private boolean hasPreformedLongPress = false;
    private static LongClickMovementMethod sInstance;

    public static MovementMethod getInstance() {
        if (sInstance == null) {
            sInstance = new LongClickMovementMethod();
        }

        return sInstance;
    }

    @Override
    public boolean canSelectArbitrarily() {
        return false;
    }

    @Override
    public boolean onTouchEvent(final TextView widget, Spannable buffer, MotionEvent event) {
        int action = event.getAction();

        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            if(action == MotionEvent.ACTION_DOWN){
                firstX = x;
                firstY = y;
            }

            x -= widget.getTotalPaddingLeft();
            y -= widget.getTotalPaddingTop();

            x += widget.getScrollX();
            y += widget.getScrollY();

            Layout layout = widget.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            final LongClickableSpan[] link = buffer.getSpans(off, off, LongClickableSpan.class);

            if (link.length != 0) {
                if (action == MotionEvent.ACTION_UP) {
                    if(pendingLongPress != null){
                        widget.removeCallbacks(pendingLongPress);
                        pendingLongPress = null;
                    }
                    if(!hasPreformedLongPress){
                        link[0].onClick(widget);
                    }
                } else if (action == MotionEvent.ACTION_DOWN) {
                    if(pendingLongPress == null) {
                        pendingLongPress = new Runnable() {
                            public void run() {
                                hasPreformedLongPress = true;
                                link[0].onLongClick(widget);
                            }
                        };
                    }

                    widget.postDelayed(pendingLongPress, LONG_PRESS_TIME_OUT);
                    hasPreformedLongPress = false;
                }

                return true;
            }
        } else if(action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_MOVE) {
            int deltaX = (int) Math.abs(firstX-event.getX());
            int deltaY = (int) Math.abs(firstY-event.getY());
            if(deltaX >= SCALED_TOUCH_SLOP || deltaY >= SCALED_TOUCH_SLOP){
                if (pendingLongPress != null) {
                    widget.removeCallbacks(pendingLongPress);
                    pendingLongPress = null;
                }
            }
            super.onTouchEvent(widget, buffer, event);
            return true;
        }

        return super.onTouchEvent(widget, buffer, event);
    }

    @Override
    public void initialize(TextView widget, Spannable text) {
        SCALED_TOUCH_SLOP = ViewConfiguration.get(widget.getContext()).getScaledTouchSlop();
    }
}