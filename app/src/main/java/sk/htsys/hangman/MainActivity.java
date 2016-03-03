package sk.htsys.hangman;

import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String BUNDLE_HANGMAN = "Hangman";

    private EditText pismenoEditText;
    private TextView slovoTextView;
    private ImageView sibenicaImageView;

    private Hangman hangman;

    private boolean gameEnded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pismenoEditText = (EditText) findViewById(R.id.pismenoEditText);
        slovoTextView = (TextView) findViewById(R.id.slovoTextView);
        sibenicaImageView = (ImageView) findViewById(R.id.sibenicaImageView);
        if (savedInstanceState == null) {
            startGame();
        } else {
            hangman = (Hangman) savedInstanceState.getSerializable(BUNDLE_HANGMAN);
            redraw();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(BUNDLE_HANGMAN, hangman);
        super.onSaveInstanceState(outState);
    }

    private void redraw() {
        pismenoEditText.setText("");
        CharSequence guessedCharacters = hangman.getGuessedCharacters();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < guessedCharacters.length(); i++) {
            sb.append(guessedCharacters.charAt(i));
            sb.append(' ');
        }
        slovoTextView.setText(sb.toString());
        if (hangman.isWon()) {
            Toast.makeText(this, "Vyhral si", Toast.LENGTH_LONG).show();
            gameEnded = true;
        } else {
            int[] obrazky = new int[]{
                    R.drawable.gallows6,
                    R.drawable.gallows5,
                    R.drawable.gallows4,
                    R.drawable.gallows3,
                    R.drawable.gallows2,
                    R.drawable.gallows1,
                    R.drawable.gallows0
            };
            int attemptsLeft = hangman.getAttemptsLeft();
            sibenicaImageView.setImageResource(obrazky[attemptsLeft]);
            if (attemptsLeft == 0) {
                Toast.makeText(this, "Prehral si :(", Toast.LENGTH_LONG).show();
                gameEnded = true;
            }
        }
    }

    public void startGame() {
        hangman = new Hangman();
        gameEnded = false;
        redraw();
    }

    public void onSibenicaClick(View view) {
        if (gameEnded) {
            startGame();
        } else {
            String s = pismenoEditText.getText().toString();
            if (s.length() < 1) {
                Toast.makeText(this, "Zadajte písmeno!", Toast.LENGTH_SHORT).show();
            } else {
                hangman.guess(s.charAt(0));
            }
            redraw();
        }
    }
}
