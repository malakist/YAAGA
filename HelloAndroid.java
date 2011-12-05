package com.examples.helloandroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.MotionEvent;
import android.widget.TextView;

public class HelloAndroid extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

	/*final TextView textoDestino = (TextView) findViewById(R.id.textoDestino);

	final TextView texto = (TextView) findViewById(R.id.texto);
	
	texto.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v) {
			textoDestino.setText("Clicou!!!");
		}
	});

	texto.setOnTouchListener(new View.OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {
			textoDestino.setText(Integer.toString(event.getAction()));
			return true;
		}
	});*/

	final TextView texto = (TextView) findViewById(R.id.texto);

	texto.setOnTouchListener(new View.OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {
			float currTime = System.currentTimeMillis();
			float currY = event.getY();
			float threshold = 2.5f;

			float speed = 0;
			final int historySize = event.getHistorySize();
			for (int h = 0; h < historySize; h++) {
				//texto.setText(
					//texto.getText() + "h=" + Integer.toString(h) + "/" +
					//Float.toString(event.getHistoricalEventTime(h))
					//"x=" + Float.toString(event.getHistoricalX(h)) + "/" +
					//"y=" + Float.toString(event.getHistoricalY(h)) + "; "
				//);
				speed = (currY - event.getHistoricalY(h)) / 
					(currY - event.getHistoricalEventTime(h));
				speed = speed * 1000000;
				if (speed < 0) speed *= -1;

				texto.setText(Float.toString(speed));

				if (speed > threshold) {
					texto.setBackgroundColor(0xffff0000);
				} else {
					texto.setBackgroundColor(0xff00ff00);
				}

				// for (int p = 0; p < 
			}

			return true;
		}
	});
    }
}
