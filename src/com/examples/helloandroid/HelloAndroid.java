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
import android.view.VelocityTracker;

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

	protected TextView texto;
    protected TextView climaxGauge;

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
        climaxGauge.setText(climaxGauge.getText() + Integer.toString(resultingIncrement));
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
        climaxGauge.setText("Climax = " + Integer.toString(climaxLevel));
    }

    public void processClimaxUpdate() {
		vTracker.computeCurrentVelocity(1000, 1350);
        // float normSpeed = normalizedSpeed(currY, histY, currTime, histTime);
		float normSpeed = normalizedSpeed(vTracker.getYVelocity());		
		texto.setText("normSpeed = " + Float.toString(normSpeed));
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

	    texto = (TextView) findViewById(R.id.texto);
        climaxGauge = (TextView) findViewById(R.id.climaxGauge);

		vTracker = VelocityTracker.obtain();

	    texto.setOnTouchListener(new View.OnTouchListener() {
		    public boolean onTouch(View v, MotionEvent event) {
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

        new Thread(
            new Runnable() {
                public void run() {
                    try {
                        while(true) {                        
                            Thread.sleep(2000);
                            texto.post(new Runnable() {
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
