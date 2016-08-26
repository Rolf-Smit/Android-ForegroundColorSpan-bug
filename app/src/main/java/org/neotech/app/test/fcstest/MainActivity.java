package org.neotech.app.test.fcstest;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TEXT = "Once upon a time, in a faraway land, a young prince lived in a shining castle. Although he had everything his heart desired, the prince was spoiled, selfish, and unkind. But then, one winter's night, an old beggar woman came to the castle and offered him a single rose in return for shelter from the bitter cold. Repulsed by her haggard appearance, the prince sneered at the gift and turned the old woman away. But she warned him not to be deceived by appearances, for beauty is found within. And when he dismissed her again, the old woman's ugliness melted away to reveal a beautiful enchantress. The prince tried to apologize, but it was too late, for she had seen that there was no love in his heart. And as punishment, she transformed him into a hideous beast and placed a powerful spell on the castle and all who lived there. Ashamed of his monstrous form, the beast concealed himself inside his castle, with a magic mirror as his only window to the outside world. The rose she had offered was truly an enchanted rose, which would bloom until his 21st year. If he could learn to love another, and earn her love in return by the time the last petal fell, then the spell would be broken. If not, he would be doomed to remain a beast for all time. As the years passed, he fell into despair and lost all hope. For who could ever learn to love a beast?";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView message = (TextView) findViewById(android.R.id.message);
        message.setMovementMethod(LongClickMovementMethod.getInstance());

        message.setText(getSpannable(TEXT), TextView.BufferType.SPANNABLE);
    }

    private Spannable getSpannable(String rawText){
        final int highlightColor = ContextCompat.getColor(this, R.color.colorHighlight);
        final int textColorSpecial = ContextCompat.getColor(this, R.color.colorAccent);
        final int textColor = ContextCompat.getColor(this, R.color.colorText);

        final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(rawText);

        //Simple method to detect sentences.
        int lastIndexOfDot = 0;
        int indexOfDot;
        while((indexOfDot = rawText.indexOf('.', lastIndexOfDot + 1)) != -1 ){
            spannableStringBuilder.setSpan(new CustomClickableSpan(highlightColor, textColor), lastIndexOfDot, indexOfDot, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            lastIndexOfDot = indexOfDot;
        }
        /**
         * Not so effective method to highlight specific words, imagine doing this including the
         * word "old woman" (hint: extra unnecessary spannable's).
         */
        String[] hitList = new String[]{"prince", "woman", "beast"};
        for(String word: hitList){
            int lastWordIndex = 0;
            int indexOfWord;
            while((indexOfWord = rawText.indexOf(word, lastWordIndex + 1)) != -1){
                lastWordIndex = indexOfWord + word.length();
                spannableStringBuilder.setSpan(new ForegroundColorSpan(textColorSpecial), indexOfWord, lastWordIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), indexOfWord, lastWordIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return spannableStringBuilder;
    }
}
