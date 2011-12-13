package com.examples.helloandroid;

import java.lang.Thread;
import java.lang.InterruptedException;

import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.VelocityTracker;
import android.util.Log;

import com.examples.helloandroid.LabelView;

public class HelloAndroid extends Activity
{
    public final float maxAcceptedSpeed = 2.5f;

    protected float frictionLevel = 0;
    protected int climaxLevel = 0;
	protected float speed = 0;
	
	protected float currTime = 0;
	protected float currY = 0;
	protected float histTime = 0;
	protected float histY = 0;
	
	protected VelocityTracker vTracker = null;
	
	protected float displayHeight = 450;
	protected final float maxVelocity = displayHeight * 3;

	protected ImageView image;
    protected LabelView climaxGauge;

    public float normalizedSpeed(float rawSpeed) {
        float resultingSpeed = rawSpeed * (100f / maxVelocity);
		if (resultingSpeed < 0) resultingSpeed *= -1;

        return resultingSpeed;
    }

    public int computeClimaxIncrement(float normalizedSpeed) {
        int resultingIncrement = 0;
        if (normalizedSpeed <= 5f || normalizedSpeed >= 95f) {
            resultingIncrement = -2;
        } else if (normalizedSpeed <= 10f || normalizedSpeed >= 90f) {
            resultingIncrement = -1;
        } else if (normalizedSpeed <= 15f || normalizedSpeed >= 85f) {
            resultingIncrement = 0;
        } else if (normalizedSpeed <= 35f || normalizedSpeed >= 65f) {
            resultingIncrement = 1;
        } else {
			resultingIncrement = 2;
		}
        // climaxGauge.setText(climaxGauge.getText() + Integer.toString(resultingIncrement));
		
        return resultingIncrement;
    }

    public void incrementClimax(int incrementValue) {
        climaxLevel += incrementValue;
        if (climaxLevel < 0) {
            climaxLevel = 0;
        } else if (climaxLevel > 100) {
            climaxLevel = 100;
        }
    }

    public void updateClimaxGauge() {
        //climaxGauge.setText("Climax = " + Integer.toString(climaxLevel));
		// String gaugeLevel = new String();
		// for (int i = 0; i < climaxLevel; ++i) {
			// gaugeLevel += "X";
		// }
		
		if (climaxLevel >= 3) {
			setContentView(R.layout.victory);
		} else {
			if (climaxGauge != null) {
				Log.i("HelloAndroid", "climaxGauge está sendo atualizado");
				climaxGauge.setLevel(climaxLevel);
			} else {
				Log.i("HelloAndroid", "climaxGauge está nulo");
			}
		}
    }

    public void processClimaxUpdate() {
		vTracker.computeCurrentVelocity(1000, 1350);
        // float normSpeed = normalizedSpeed(currY, histY, currTime, histTime);
		float normSpeed = normalizedSpeed(vTracker.getYVelocity());
        int climaxInc = computeClimaxIncrement(normSpeed);
        incrementClimax(climaxInc);
        updateClimaxGauge();
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

	    image = (ImageView) findViewById(R.id.image);
        climaxGauge = (LabelView) findViewById(R.id.climaxGauge);

		vTracker = VelocityTracker.obtain();

	    image.setOnTouchListener(new View.OnTouchListener() {
		    public boolean onTouch(View v, MotionEvent event) {
				Log.i("HelloAndroid", "onTouch disparado");
			
			    if (event.getAction() == MotionEvent.ACTION_DOWN) {
					if (vTracker == null) vTracker = VelocityTracker.obtain();
				} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
					vTracker.addMovement(event);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					vTracker.clear();
				}
				
			    return true;
		    }
	    });
		
		/*TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
			
			}
		};*/
		

        new Thread(
            new Runnable() {
                public void run() {
                    try {
                        while(true) {                        
                            Thread.sleep(2000);
                            image.post(new Runnable() {
                                public void run() {
                                    incrementClimax(-1);
                                    updateClimaxGauge();
                                }
                            });  
                        }
                    } 
                    catch (Exception ex) {

                    }  
                }
            }
        ).start();
		
		new Thread(
            new Runnable() {
                public void run() {
                    try {
                        while(true) {                        
                            Thread.sleep(500);
                            climaxGauge.post(new Runnable() {
                                public void run() {
                                    processClimaxUpdate();
                                }
                            });  
                        }
                    } 
                    catch (Exception ex) {

                    }  
                }
            }
        ).start();
    }
}
