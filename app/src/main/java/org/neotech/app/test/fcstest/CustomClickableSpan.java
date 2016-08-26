package org.neotech.app.test.fcstest;

import android.text.TextPaint;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.Toast;

/**
 * A clickable span with highlighting and select capabilities including custom foreground and
 * highlight colors.
 */
public final class CustomClickableSpan extends LongClickableSpan {

    private final int highlightColor;
    private final int textColor;

    private boolean shouldHighlightText = false;
    private boolean isSelected = false;
    private View view = null;

    private final Runnable unHighLight = new Runnable() {
        @Override
        public void run() {
            shouldHighlightText = false;
            view.invalidate();
        }
    };

    public CustomClickableSpan(int highlightColor, int textColor){
        this.highlightColor = highlightColor;
        this.textColor = textColor;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public void toggleSelected(){
        this.isSelected = !this.isSelected;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(textColor);
        if(shouldHighlightText || isSelected){
            ds.bgColor = highlightColor;
        }
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(view.getContext(), "onClick()", Toast.LENGTH_SHORT).show();
        if(isSelected()){
            toggleSelected();
        }
        if(!isSelected){
            shouldHighlightText = true;
            view.removeCallbacks(unHighLight);
            this.view = view;
            view.postDelayed(unHighLight, 200);
        }
        view.invalidate();
    }

    @Override
    public void onLongClick(View view) {
        Toast.makeText(view.getContext(), "onLongClick()", Toast.LENGTH_SHORT).show();
        view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
        toggleSelected();
        view.invalidate();
    }
}