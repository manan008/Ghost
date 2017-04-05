package com.google.engedu.ghost3;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.engedu.ghost.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class GhostActivity extends AppCompatActivity implements KeyListener{

    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private static  String Text1 = "";
    private static  int player_Score = 0;
    private static  int com_Score = 0;
    private GhostDictionary dictionary;
    private GhostDictionary dictionary2;
    private boolean userTurn = false;
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);

        try {
            AssetManager assetManager = getAssets();
            InputStream inputStream = assetManager.open("words.txt");
            //dictionary = new FastDictionary(inputStream);
            dictionary2 = new FastDictionary(inputStream);
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }
        onStart(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        TextView textView=(TextView)findViewById(R.id.ghostText);
        Text1 =textView.getText().toString();
        savedInstanceState.putBoolean("UserTurn",userTurn);
        savedInstanceState.putInt("ComScore",com_Score);
        savedInstanceState.putInt("PlayerScore",player_Score);
        savedInstanceState.putString("Text1", Text1);
        super.onSaveInstanceState(savedInstanceState);

    }
@Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
    com_Score = savedInstanceState.getInt("Com Score");
    player_Score = savedInstanceState.getInt("PlayerScore");
    userTurn=savedInstanceState.getBoolean("UserTurn");
    Text1 =savedInstanceState.getString("Text1");
    super.onRestoreInstanceState(savedInstanceState);

        // Restore state members from saved instance
    }
    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText(Text1);
        TextView comScoreView = (TextView) findViewById(R.id.textView);
        TextView playerScoreView = (TextView) findViewById(R.id.textView2);
        comScoreView.setText(""+com_Score);
        playerScoreView.setText(""+player_Score);
        TextView label = (TextView) findViewById(R.id.gameStatus);
        text.setKeyListener(this);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }

    public void onReStart(View view)
    {
        com_Score=player_Score=0;
        onStart(null);
    }
    private void computerTurn() {
        Text1 ="";
        Log.i("App1","Com Turn");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        TextView text= (TextView) findViewById(R.id.ghostText);
        TextView comScoreView = (TextView) findViewById(R.id.textView);
        TextView playerScoreView = (TextView) findViewById(R.id.textView2);
        String word;
        word=text.getText().toString();
        Log.i("App1",word);
        if(word.equals(""))
        {
            Log.i("App1","Starting goodwords function");
            text.setText(""+dictionary2.getGoodWordStartingWith("").charAt(0));
        }
        else
        {
        Log.i("App1","Test1");
        if(word.length()>=4&&dictionary2.isWord(word))
        {
            Log.i("App1","word lenth >4");
            label.setText("Computer Wins");
            comScoreView.setText(++com_Score+"");
            Toast.makeText(getApplicationContext(),"Computer Wins:"+word.toUpperCase()+" is a valid word",Toast.LENGTH_LONG).show();
            onStart(null);
        }
        else
        {
            Log.i("App1","Start good woords");
            String newword=dictionary2.getGoodWordStartingWith(word);
            Log.i("App1","New Word"+newword);
            if(newword==null)
            {
                label.setText("Computer Wins");
                comScoreView.setText(++com_Score+"");
                Toast.makeText(getApplicationContext(),"Computer Wins:No words starting with:"+word.toUpperCase(),Toast.LENGTH_LONG).show();
                onStart(null);
            }
            else
            {
                text.setText(newword.substring(0,word.length()+1));
            }
         }
        }
        // Do computer turn stuff then make it the user's turn again
        userTurn = true;
        label.setText(USER_TURN);
    }

    @Override
    public int getInputType() {
        return 0;
    }

    @Override
    public boolean onKeyDown(View view, Editable text, int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public boolean onKeyUp(View view, Editable text, int keyCode, KeyEvent event){
        char c=(char)event.getUnicodeChar();
        Log.i("App1","KeyListener Entered");
        Log.i("App1",""+c);
        TextView label=(TextView) findViewById(R.id.ghostText);
        TextView status=(TextView) findViewById(R.id.gameStatus);
        String word=label.getText().toString();
        if(!Character.isLetter(c))
        {
            Log.i("App1",""+c);
            Log.i("App1","Not Letter");
            return super.onKeyUp(keyCode,event);
        }
        else
            {
                Log.i("App1","Letter Entered");
                word+=""+c;
                label.setText(word);
                if(dictionary2.isWord(word))
                {
                    status.setText("Is  Word");
                }
                else status.setText("Not Word");
            }
        computerTurn();
        return false;
    }

    public void challenge(View view)
    {
        Text1 ="";
        TextView text=(TextView)findViewById(R.id.ghostText);
        TextView label=(TextView)findViewById(R.id.gameStatus);
        TextView comScoreView = (TextView) findViewById(R.id.textView);
        TextView playerScoreView = (TextView) findViewById(R.id.textView2);
        comScoreView.setText(com_Score+"");
        String wordfrag= text.getText().toString();
        String possword=dictionary2.getGoodWordStartingWith(wordfrag);
        if(wordfrag.length()>=4&&dictionary2.isWord(wordfrag))
        {
            label.setText("You WIN");
            playerScoreView.setText(++player_Score+"");
            Toast.makeText(getApplicationContext(),"User WINS",Toast.LENGTH_LONG).show();
            onStart(null);
        }
        else if(possword!=null)
        {
            label.setText("Computer WIN");
            comScoreView.setText(++com_Score+"");
            Toast.makeText(getApplicationContext(),"Computer WINS:Possible Word:"+possword,Toast.LENGTH_SHORT).show();
            onStart(null);
        }
    }
    @Override
    public boolean onKeyOther(View view, Editable text, KeyEvent event) {
        return false;
    }

    @Override
    public void clearMetaKeyState(View view, Editable content, int states) {

    }
}
