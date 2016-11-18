package ca.ualberta.cs.lonelytwitter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
//deleted unused import java.util.Date

import android.app.Activity;
//deleted unused import android.content.Context
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class LonelyTwitterActivity extends Activity {

	private final Activity activity = this; //make final

	//deleted unused string file.sav
	private EditText bodyText;
	private ListView oldTweetsList;
	private ArrayList<NormalTweet> tweetList = new ArrayList<NormalTweet>();
	private ArrayAdapter<NormalTweet> adapter;

	public ListView getOldTweetsList(){
		return oldTweetsList;
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		bodyText = (EditText) findViewById(R.id.body);
		Button saveButton = (Button) findViewById(R.id.save);
		Button searchButton = (Button) findViewById(R.id.search);
		oldTweetsList = (ListView) findViewById(R.id.oldTweetsList);

		saveButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				setResult(RESULT_OK);
				String text = bodyText.getText().toString();
				NormalTweet newTweet = new NormalTweet(text);
				tweetList.add(newTweet);
				adapter.notifyDataSetChanged();
				// saveInFile(); // TODO replace this with elastic search
				ElasticsearchTweetController.AddTweetsTask addTweetsTask = new ElasticsearchTweetController.AddTweetsTask();
				addTweetsTask.execute(newTweet);
			}
		});

		searchButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				setResult(RESULT_OK); //what does this mean?
				//tweetList.clear();
				//deleteFile(FILENAME);  // TODO deprecate this button
				//adapter.notifyDataSetChanged();


			}
		});

		oldTweetsList.setOnItemClickListener(new
				AdapterView.OnItemClickListener(){
					public void onItemClick(AdapterView<?> parent, View view,
											int position ,long id){
						Intent intent = new Intent(activity, EditTweetActivity.class);
						startActivity(intent);
					}

				});


	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		// loadFromFile(); // TODO replace this with elastic search
		ElasticsearchTweetController.GetTweetsTask getTweetsTask = new ElasticsearchTweetController.GetTweetsTask();
		getTweetsTask.execute("");
		try {
			tweetList = getTweetsTask.get();
		}
		catch (Exception e) {
			Log.i("Error", "Failed to get the tweets out of the async object.");
		}
		adapter = new ArrayAdapter<NormalTweet>(this,
				R.layout.list_item, tweetList);
		oldTweetsList.setAdapter(adapter);
	}


	//delted loadFromFile() and saveInFile()
}