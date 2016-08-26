package org.neotech.app.test.fcstest;

import android.text.style.ClickableSpan;
import android.view.View;

public abstract class LongClickableSpan extends ClickableSpan {

    public abstract void onLongClick(View widget);

}