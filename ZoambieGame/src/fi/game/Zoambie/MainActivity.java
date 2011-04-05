package fi.game.Zoambie;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {
    @Override
    
    //Luodaan vain main.xml leiska
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
}