package ch.epfl.sweng.SDP;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        view = this.getWindow().getDecorView();
        view.setBackgroundResource(R.color.colorGrey);

        TextView drawText = findViewById(R.id.drawButton);
        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/Muroslant.otf");
        drawText.setTypeface(type);
        drawText.setPadding(0, -14, 0, 0);

        TextView trophiesText = findViewById(R.id.trophiesButton);
        trophiesText.setTypeface(type);
        trophiesText.setPadding(60, -5, 0, 0);

        TextView starsText = findViewById(R.id.starsButton);
        starsText.setTypeface(type);
        starsText.setPadding(60, -5, 0, 0);
    }

    public void didTapButton(View view) {
        Button button = (Button)findViewById(R.id.drawButton);
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);

        // Use bounce interpolator with amplitude 0.2 and frequency 20
        BounceInterpolator interpolator = new BounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);

        button.startAnimation(myAnim);
    }
}