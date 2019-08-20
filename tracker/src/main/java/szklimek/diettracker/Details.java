package szklimek.diettracker;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class Details extends AppCompatActivity {

TextView details;
TextView news;
    /**
     * Initializes the activity, filling in the data from the Intent.
     * @param savedInstanceState Contains information about the saved state of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra(Child.TITLE_KEY));


        details = findViewById(R.id.subTitleDetail);
        news = findViewById(R.id.newsTitleDetail);

        details.setText(Html.fromHtml(getIntent().getStringExtra(Child.DETAIL_KEY)));
        news.setText(getIntent().getStringExtra(Child.NEWS_KEY));


        //Initialize the views
       // TextView sportsTitle = (TextView)findViewById(R.id.titleDetail);
        ImageView sportsImage = findViewById(R.id.backdrop);
        //Get the drawable
        Drawable drawable = ContextCompat.getDrawable
                (this,getIntent().getIntExtra(Child.IMAGE_KEY, 0));

        //Create a placeholder gray scrim while the image loads
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(Color.GRAY);

        //Make it the same size as the image
        if(drawable!=null) {
            gradientDrawable.setSize(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        }

        //Set the text from the Intent extra
    //    sportsTitle.setText(getIntent().getStringExtra(Child.TITLE_KEY));

        //Load the image using the glide library and the Intent extra
        Glide.with(this).load(getIntent().getIntExtra(Child.IMAGE_KEY,0))
                .placeholder(gradientDrawable).into(sportsImage);

    }

    public void playVideo(View view) {
      //  Intent intent = new Intent(Details.this,YoutubeVideo.class);
        //intent.putExtra("key",getIntent().getStringExtra(Child.TITLE_KEY));
        //startActivity(intent);
    }
}
