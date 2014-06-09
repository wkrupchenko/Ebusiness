package de.mytasks.activities;

import java.util.List;

import de.mytasks.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class ParticipantListAdapter extends ArrayAdapter<String> {
	  private final Context context;
	  private final List<String> emails;
	  private final List<String> names;
	  private final List<Float> ratings;

	  public ParticipantListAdapter(Context context, List<String> emails, List<String> names, List<Float> ratings) {
	    super(context, R.layout.activity_participants, names);
	    this.context = context;
	    this.emails = emails;
	    this.names = names;
	    this.ratings = ratings;
	  }

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) { 
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(R.layout.activity_participants, parent, false);
	    TextView textView1 = (TextView) rowView.findViewById(R.id.userMail);
	    TextView textView2 = (TextView) rowView.findViewById(R.id.userName);
	    RatingBar ratingBar = (RatingBar) rowView.findViewById(R.id.participantsRatingBar);
	    ratingBar.setNumStars(5);
	    textView1.setText(names.get(position));
	    textView2.setText(emails.get(position));
	    ratingBar.setRating(ratings.get(position));
	    
	    TextView textView = new TextView(context);
	    textView.setText("Hello. I'm a header view");
	    
	    return rowView;
	  }
	} 