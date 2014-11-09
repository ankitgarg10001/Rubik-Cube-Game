package com.jiit.rubikcube;

import java.util.Random;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Point;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainActivity extends Activity implements
		KubeRenderer.AnimationCallback {

	private float ax, ay, oldX, oldY;
	private static int stopani = 0;
	static int newcube = 0;
	static int vcurrentface = 1;

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public int getDeviceWidth() {
		int deviceWidth = 0;

		Point size = new Point();
		WindowManager windowManager = getWindowManager();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			windowManager.getDefaultDisplay().getSize(size);
			deviceWidth = size.x;
		} else {
			Display display = windowManager.getDefaultDisplay();
			deviceWidth = display.getWidth();
		}
		return deviceWidth;
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public int getDeviceHeight() {
		int deviceHeight = 0;

		Point size = new Point();
		WindowManager windowManager = getWindowManager();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			windowManager.getDefaultDisplay().getSize(size);
			deviceHeight = size.y;
		} else {
			Display display = windowManager.getDefaultDisplay();
			deviceHeight = display.getHeight();
		}
		return deviceHeight;
	}

	private GLWorld makeGLWorld() {
		GLWorld world = new GLWorld();

		int one = 0x10000;// 65536 to hex--16^4
		int half = 0x08000;// 2^15
		GLColor red = new GLColor(one, 0, 0);
		GLColor green = new GLColor(0, one, 0);
		GLColor blue = new GLColor(0, 0, one);
		GLColor yellow = new GLColor(one, one, 0);
		GLColor orange = new GLColor(one, half, 0);
		GLColor white = new GLColor(one, one, one);
		GLColor black = new GLColor(0, 0, 0);

		float c0 = -1.0f;
		float c1 = -0.38f;
		float c2 = -0.32f;
		float c3 = 0.32f;
		float c4 = 0.38f;
		float c5 = 1.0f;
		// make cubes with vertices coordinates
		// top back, left to right
		mCubes[0] = new Cube(world, c0, c4, c0, c1, c5, c1);
		// GLWorld world, float left0, float bottom4, float back0, float right1,
		// float top5, float front1
		mCubes[1] = new Cube(world, c2, c4, c0, c3, c5, c1);
		mCubes[2] = new Cube(world, c4, c4, c0, c5, c5, c1);
		// top middle, left to right
		mCubes[3] = new Cube(world, c0, c4, c2, c1, c5, c3);
		mCubes[4] = new Cube(world, c2, c4, c2, c3, c5, c3);
		mCubes[5] = new Cube(world, c4, c4, c2, c5, c5, c3);
		// top front, left to right
		mCubes[6] = new Cube(world, c0, c4, c4, c1, c5, c5);
		mCubes[7] = new Cube(world, c2, c4, c4, c3, c5, c5);
		mCubes[8] = new Cube(world, c4, c4, c4, c5, c5, c5);
		// middle back, left to right
		mCubes[9] = new Cube(world, c0, c2, c0, c1, c3, c1);
		mCubes[10] = new Cube(world, c2, c2, c0, c3, c3, c1);
		mCubes[11] = new Cube(world, c4, c2, c0, c5, c3, c1);
		// middle middle, left to right
		mCubes[12] = new Cube(world, c0, c2, c2, c1, c3, c3);
		mCubes[13] = null;
		mCubes[14] = new Cube(world, c4, c2, c2, c5, c3, c3);
		// middle front, left to right
		mCubes[15] = new Cube(world, c0, c2, c4, c1, c3, c5);
		mCubes[16] = new Cube(world, c2, c2, c4, c3, c3, c5);
		mCubes[17] = new Cube(world, c4, c2, c4, c5, c3, c5);
		// bottom back, left to right
		mCubes[18] = new Cube(world, c0, c0, c0, c1, c1, c1);
		mCubes[19] = new Cube(world, c2, c0, c0, c3, c1, c1);
		mCubes[20] = new Cube(world, c4, c0, c0, c5, c1, c1);
		// bottom middle, left to right
		mCubes[21] = new Cube(world, c0, c0, c2, c1, c1, c3);
		mCubes[22] = new Cube(world, c2, c0, c2, c3, c1, c3);
		mCubes[23] = new Cube(world, c4, c0, c2, c5, c1, c3);
		// bottom front, left to right
		mCubes[24] = new Cube(world, c0, c0, c4, c1, c1, c5);
		mCubes[25] = new Cube(world, c2, c0, c4, c3, c1, c5);
		mCubes[26] = new Cube(world, c4, c0, c4, c5, c1, c5);

		// paint the sides
		int i, j;
		// set all faces black by default
		for (i = 0; i < 27; i++) {
			Cube cube = mCubes[i];
			if (cube != null) {
				for (j = 0; j < 6; j++)
					cube.setFaceColor(j, black);
			}
		}

		// paint all sides with their respictive colours,cubes are still seprate
		// entities
		// paint top
		for (i = 0; i < 9; i++)
			mCubes[i].setFaceColor(Cube.kTop, orange);
		// paint bottom
		for (i = 18; i < 27; i++)
			mCubes[i].setFaceColor(Cube.kBottom, red);
		// paint left
		for (i = 0; i < 27; i += 3)
			mCubes[i].setFaceColor(Cube.kLeft, yellow);
		// paint right
		for (i = 2; i < 27; i += 3)
			mCubes[i].setFaceColor(Cube.kRight, green);
		// paint back
		for (i = 0; i < 27; i += 9)
			for (j = 0; j < 3; j++)
				mCubes[i + j].setFaceColor(Cube.kBack, blue);
		// paint front
		for (i = 6; i < 27; i += 9)
			for (j = 0; j < 3; j++)
				mCubes[i + j].setFaceColor(Cube.kFront, white);

		for (i = 0; i < 27; i++)
			if (mCubes[i] != null)
				world.addShape(mCubes[i]);

		// initialize our permutation to solved position

		if (newcube == 0) {
			mPermutation = new int[27];
			for (i = 0; i < mPermutation.length; i++)
				mPermutation[i] = i;
		}

		// create layers into one combined object
		createLayers();
		// save above created setpate cubes(with colours fixed by now) into
		// mLayers, one sincle cube object
		updateLayers();

		world.generate();

		return world;
	}

	int[][] fchange = { { 0, 1, 2, 3, 4, 5, 6, 7, 8 },
			{ 0, 1, 4, 5, 3, 2, 8, 7, 8 }, { 0, 1, 3, 2, 4, 5, 6, 7, 8 },
			{ 0, 1, 5, 4, 3, 2, 8, 7, 6 }, { 5, 4, 2, 3, 0, 1, 6, 8, 7 },
			{ 4, 5, 2, 3, 0, 1, 6, 8, 7 } };

	int[] finishGame = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
			16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26 };

	void finishFunc() {
		int check = 0;
		for (int il = 0; il < 27; il++) {
			if (mPermutation[il] != finishGame[il]) {
				check = 1;
				break;
			}
		}

		if (check == 0) {
			Log.d("Finish", "Game Over!");
			double time = (System.currentTimeMillis() - time_start) / 1000d;
			String gameTime = "(No Time Recorded)";
			if (time > 60 && time < 3600) {
				// Log.d("Continue", "Continue : "+(int)time/60 + " minutes" +
				// (int)time%60 + " secounds");
				if ((int) time / 60 == 1 && (int) time % 60 == 1) {
					gameTime = (int) time / 60 + " minute " + (int) time % 60
							+ " secound";
				} else if ((int) time / 60 == 1) {
					gameTime = (int) time / 60 + " minute " + (int) time % 60
							+ " secounds";
				} else if ((int) time % 60 == 1) {
					gameTime = (int) time / 60 + " minutes " + (int) time % 60
							+ " secound";
				} else {
					gameTime = (int) time / 60 + " minutes " + (int) time % 60
							+ " secounds";
				}

			}
			if (time > 3600) {
				// Log.d("Continue", "Continue : "+(int)time/3600 + " hours" +
				// (int)time%3600 +" minutes");
				if ((int) time / 3600 == 1 && (int) time % 3600 == 1) {
					gameTime = (int) time / 3600 + " hour " + (int) time % 3600
							+ " minute";
				} else if ((int) time / 3600 == 1) {
					gameTime = (int) time / 3600 + " hour " + (int) time % 3600
							+ " minutes";
				} else if ((int) time % 3600 == 1) {
					gameTime = (int) time / 3600 + " hours " + (int) time
							% 3600 + " minute";
				} else {
					gameTime = (int) time / 3600 + " hours " + (int) time
							% 3600 + " minutes";
				}

			}
			if (time < 60) {
				// Log.d("Continue", "Continue : "+(int)time + " secounds");
				if ((int) time == 1) {
					gameTime = (int) time + " secound";
				} else {
					gameTime = (int) time + " secounds";
				}

			}
			final String finalGameTime = gameTime;
			MainActivity.this.runOnUiThread(new Runnable() {
				public void run() {
					// process incoming messages here

					new AlertDialog.Builder(MainActivity.this)
							.setTitle("Congratulations!!")
							.setMessage(
									"You have finished the Game in : "
											+ finalGameTime
											+ ". Do you want to start a new game?")
							.setNegativeButton("NO", null).setCancelable(true)
							.setPositiveButton("YES", new OnClickListener() {

								public void onClick(DialogInterface arg0,
										int arg1) {

									stopani = 0;
									ijk = 0;
									Toast toast = Toast.makeText(
											getApplicationContext(),
											"  Please Wait...  ",
											Toast.LENGTH_SHORT);

									toast.show();

									finish();
									startActivity(getIntent());
								}
							}).create().show();

				}
			});
		}

		else {

			double time = (System.currentTimeMillis() - time_start) / 1000d;
			if (time > 60 && time < 3600) {
				Log.d("Continue", "Continue : " + (int) time / 60 + " minutes"
						+ (int) time % 60 + " secounds");
			}
			if (time > 3600) {
				Log.d("Continue", "Continue : " + (int) time / 3600 + " hours"
						+ (int) time % 3600 + " minutes");
			}
			if (time < 60) {
				Log.d("Continue", "Continue : " + (int) time + " secounds");
			}

		}

	}

	private void createLayers() {
		mLayers[kUp] = new Layer(Layer.kAxisY);
		mLayers[kDown] = new Layer(Layer.kAxisY);
		mLayers[kLeft] = new Layer(Layer.kAxisX);
		mLayers[kRight] = new Layer(Layer.kAxisX);
		mLayers[kFront] = new Layer(Layer.kAxisZ);
		mLayers[kBack] = new Layer(Layer.kAxisZ);
		mLayers[kMiddle] = new Layer(Layer.kAxisX);
		mLayers[kEquator] = new Layer(Layer.kAxisY);
		mLayers[kSide] = new Layer(Layer.kAxisZ);
	}

	private void updateLayers() {
		Layer layer;
		GLShape[] shapes;
		int i, j, k;

		// up layer
		layer = mLayers[kUp];
		shapes = layer.mShapes;
		for (i = 0; i < 9; i++)
			shapes[i] = mCubes[mPermutation[i]];

		// down layer
		layer = mLayers[kDown];
		shapes = layer.mShapes;
		for (i = 18, k = 0; i < 27; i++)
			shapes[k++] = mCubes[mPermutation[i]];

		// left layer
		layer = mLayers[kLeft];
		shapes = layer.mShapes;
		for (i = 0, k = 0; i < 27; i += 9)
			for (j = 0; j < 9; j += 3)
				shapes[k++] = mCubes[mPermutation[i + j]];

		// right layer
		layer = mLayers[kRight];
		shapes = layer.mShapes;
		for (i = 2, k = 0; i < 27; i += 9)
			for (j = 0; j < 9; j += 3)
				shapes[k++] = mCubes[mPermutation[i + j]];

		// front layer
		layer = mLayers[kFront];
		shapes = layer.mShapes;
		for (i = 6, k = 0; i < 27; i += 9)
			for (j = 0; j < 3; j++)
				shapes[k++] = mCubes[mPermutation[i + j]];

		// back layer
		layer = mLayers[kBack];
		shapes = layer.mShapes;
		for (i = 0, k = 0; i < 27; i += 9)
			for (j = 0; j < 3; j++)
				shapes[k++] = mCubes[mPermutation[i + j]];

		// middle layer, white orange blue red, vertical front to back
		layer = mLayers[kMiddle];
		shapes = layer.mShapes;
		for (i = 1, k = 0; i < 27; i += 9)
			for (j = 0; j < 9; j += 3)
				shapes[k++] = mCubes[mPermutation[i + j]];

		// equator layer, white yellow blue green , horizontal front to back
		layer = mLayers[kEquator];
		shapes = layer.mShapes;
		for (i = 9, k = 0; i < 18; i++)
			shapes[k++] = mCubes[mPermutation[i]];

		// side layer, side facing midle layer, orange green red yellow
		layer = mLayers[kSide];
		shapes = layer.mShapes;
		for (i = 3, k = 0; i < 27; i += 9)
			for (j = 0; j < 3; j++)
				shapes[k++] = mCubes[mPermutation[i + j]];
	}

	public static long time_start;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// We don't need a title either.
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		cando = false;

		mView = new GLSurfaceView(getApplication());
		mRenderer = new KubeRenderer(makeGLWorld(), this);
		mView.setRenderer(mRenderer);
		setContentView(mView);

		final Button b = new Button(this);
		b.setText("");
		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Log.d("touch", "top left side");
				if (cando) {
					flag = true;
					if (vcurrentface == 2 || vcurrentface == 3) {
						direction = true;
					} else {
						direction = false;
					}

					touch = fchange[vcurrentface - 1][2];
					layerID = touch;
					mCurrentLayer = null;
				}
			}
		});
		final Button bb = new Button(this);
		bb.setText("");
		bb.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (cando) {
					Log.d("touch", "center top left side");
					flag = true;
					if (vcurrentface == 6) {
						direction = false;
					} else {
						direction = true;
					}
					touch = fchange[vcurrentface - 1][0];
					layerID = touch;
					mCurrentLayer = null;
				}
			}
		});
		final Button bb10 = new Button(this);
		// bb10.setText("Choose Face");
		if (vcurrentface == 1) {
			bb10.setText("1");
			resetfunc();
			ax = 16;
			ay = 16;
		}
		final Button bb101 = new Button(this);
		bb101.setText("1");
		final Button bb102 = new Button(this);
		bb102.setText("2");
		final Button bb103 = new Button(this);
		bb103.setText("3");
		final Button bb104 = new Button(this);
		bb104.setText("4");
		final Button bb105 = new Button(this);
		bb105.setText("5");
		final Button bb106 = new Button(this);
		bb106.setText("6");

		LinearLayout l1 = new LinearLayout(this);

		l1.setOrientation(LinearLayout.VERTICAL);
		l1.addView(b);
		l1.addView(bb);
		l1.addView(bb10);
		l1.addView(bb101);
		l1.addView(bb102);
		l1.addView(bb103);
		l1.addView(bb104);
		l1.addView(bb105);
		l1.addView(bb106);
		bb101.setVisibility(View.GONE);
		bb102.setVisibility(View.GONE);
		bb103.setVisibility(View.GONE);
		bb104.setVisibility(View.GONE);
		bb105.setVisibility(View.GONE);
		bb106.setVisibility(View.GONE);
		l1.setGravity(Gravity.LEFT | Gravity.TOP);
		ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		ViewGroup.LayoutParams lpm = new ViewGroup.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		this.addContentView(l1, lp);

		final Button b1 = new Button(this);
		b1.setText("");
		b1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (cando) {
					flag = true;
					Log.d("touch", "top middle side");
					if (vcurrentface == 2 || vcurrentface == 3) {
						direction = true;
					} else {
						direction = false;
					}
					touch = fchange[vcurrentface - 1][6];
					mCurrentLayer = null;
					layerID = touch;
				}
			}
		});
		LinearLayout l2 = new LinearLayout(this);
		l2.addView(b1);
		l2.setGravity(Gravity.CENTER | Gravity.TOP);
		this.addContentView(l2, lpm);

		final Button b2 = new Button(this);
		b2.setText("");
		b2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (cando) {
					flag = true;
					Log.d("touch", "top right side");
					if (vcurrentface == 2 || vcurrentface == 3) {
						direction = true;
					} else {
						direction = false;
					}
					touch = fchange[vcurrentface - 1][3];
					mCurrentLayer = null;
					layerID = touch;
				}
			}
		});
		final Button bb2 = new Button(this);
		bb2.setText("");
		bb2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (cando) {
					flag = true;
					Log.d("touch", "center top right side");
					if (vcurrentface == 6) {
						direction = true;
					} else {
						direction = false;
					}

					touch = fchange[vcurrentface - 1][0];
					mCurrentLayer = null;
					layerID = touch;
				}
			}
		});
		LinearLayout l3 = new LinearLayout(this);
		l3.addView(b2);
		l3.addView(bb2);
		l3.setOrientation(LinearLayout.VERTICAL);
		l3.setGravity(Gravity.RIGHT | Gravity.TOP);
		this.addContentView(l3, lpm);

		final Button b3 = new Button(this);
		b3.setText("");
		b3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (cando) {
					flag = true;
					Log.d("touch", "center bottom right side");
					if (vcurrentface == 6) {
						direction = true;
					} else {
						direction = false;
					}
					touch = fchange[vcurrentface - 1][1];
					mCurrentLayer = null;
					layerID = touch;
				}
			}
		});
		final Button bb3 = new Button(this);
		bb3.setText("");
		bb3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (cando) {
					flag = true;
					Log.d("touch", "bottom right side");
					if (vcurrentface == 2 || vcurrentface == 3) {
						direction = false;
					} else {
						direction = true;
					}
					touch = fchange[vcurrentface - 1][3];
					mCurrentLayer = null;
					layerID = touch;
				}
			}
		});
		b3.setLayoutParams(lp);
		bb3.setLayoutParams(lp);
		LinearLayout l4 = new LinearLayout(this);
		l4.setOrientation(LinearLayout.VERTICAL);
		l4.addView(b3);
		l4.addView(bb3);
		l4.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
		// ViewGroup.LayoutParams lp = new
		// ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT,
		// LayoutParams.MATCH_PARENT);
		this.addContentView(l4, lpm);

		final Button b4 = new Button(this);
		b4.setText("");
		b4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (cando) {
					flag = true;
					Log.d("touch", "bottom middle side");
					if (vcurrentface == 2 || vcurrentface == 3) {
						direction = false;
					} else {
						direction = true;
					}
					touch = fchange[vcurrentface - 1][6];
					mCurrentLayer = null;
					layerID = touch;
				}
			}
		});
		LinearLayout l5 = new LinearLayout(this);
		l5.addView(b4);
		l5.setGravity(Gravity.CENTER | Gravity.BOTTOM);
		// ViewGroup.LayoutParams lp = new
		// ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT,
		// LayoutParams.MATCH_PARENT);
		this.addContentView(l5, lpm);

		final Button b5 = new Button(this);
		b5.setText("");
		b5.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (cando) {
					flag = true;
					Log.d("touch", "center bottom left side");
					if (vcurrentface == 6) {
						direction = false;
					} else {
						direction = true;
					}
					touch = fchange[vcurrentface - 1][1];
					mCurrentLayer = null;
					layerID = touch;
				}
			}
		});
		final Button bb5 = new Button(this);
		bb5.setText("");
		bb5.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (cando) {
					flag = true;
					Log.d("touch", "bottom left side");
					if (vcurrentface == 2 || vcurrentface == 3) {
						direction = false;
					} else {
						direction = true;
					}
					touch = fchange[vcurrentface - 1][2];
					mCurrentLayer = null;
					layerID = touch;
				}
			}
		});
		b5.setLayoutParams(lp);
		bb5.setLayoutParams(lp);
		LinearLayout l6 = new LinearLayout(this);
		l6.setOrientation(LinearLayout.VERTICAL);
		l6.addView(b5);
		l6.addView(bb5);
		l6.setGravity(Gravity.LEFT | Gravity.BOTTOM);
		// ViewGroup.LayoutParams lp = new
		// ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT,
		// LayoutParams.MATCH_PARENT);
		this.addContentView(l6, lpm);

		final Button b6 = new Button(this);
		b6.setText("");
		b6.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (cando) {
					flag = true;
					Log.d("touch", "middle left side");
					if (vcurrentface == 6) {
						direction = false;
					} else {
						direction = true;
					}
					touch = fchange[vcurrentface - 1][7];
					mCurrentLayer = null;
					layerID = touch;
				}
			}
		});
		LinearLayout l7 = new LinearLayout(this);
		l7.addView(b6);
		l7.setGravity(Gravity.LEFT | Gravity.CENTER);
		// ViewGroup.LayoutParams lp = new
		// ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT,
		// LayoutParams.MATCH_PARENT);
		this.addContentView(l7, lpm);

		final Button b7 = new Button(this);
		b7.setText("");
		b7.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (cando) {
					flag = true;
					Log.d("touch", "middle right side");
					if (vcurrentface == 6) {
						direction = true;
					} else {
						direction = false;
					}
					touch = fchange[vcurrentface - 1][7];
					mCurrentLayer = null;
					layerID = touch;
				}
			}
		});
		LinearLayout l8 = new LinearLayout(this);
		l8.addView(b7);
		l8.setGravity(Gravity.RIGHT | Gravity.CENTER);
		// ViewGroup.LayoutParams lp = new
		// ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT,
		// LayoutParams.MATCH_PARENT);
		this.addContentView(l8, lpm);

		final Button bc = new Button(this);
		bc.setText("Start Game");
		bc.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {

				ijk = 100;
				bc.setVisibility(View.GONE);
				bb10.setVisibility(View.VISIBLE);
				b.setVisibility(View.VISIBLE);
				bb.setVisibility(View.VISIBLE);
				time_start = System.currentTimeMillis();

				b1.setVisibility(View.VISIBLE);

				b2.setVisibility(View.VISIBLE);
				bb2.setVisibility(View.VISIBLE);

				b3.setVisibility(View.VISIBLE);
				bb3.setVisibility(View.VISIBLE);

				b4.setVisibility(View.VISIBLE);
				b5.setVisibility(View.VISIBLE);
				bb5.setVisibility(View.VISIBLE);
				b6.setVisibility(View.VISIBLE);
				b7.setVisibility(View.VISIBLE);
			}
		});
		LinearLayout l9 = new LinearLayout(this);
		l9.addView(bc);
		l9.setGravity(Gravity.CENTER | Gravity.CENTER);
		// ViewGroup.LayoutParams lp = new
		// ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT,
		// LayoutParams.MATCH_PARENT);
		this.addContentView(l9, lpm);
		b.setVisibility(View.GONE);
		bb.setVisibility(View.GONE);
		b1.setVisibility(View.GONE);
		b2.setVisibility(View.GONE);
		bb2.setVisibility(View.GONE);
		b3.setVisibility(View.GONE);
		bb3.setVisibility(View.GONE);
		b4.setVisibility(View.GONE);
		b5.setVisibility(View.GONE);
		bb5.setVisibility(View.GONE);
		b6.setVisibility(View.GONE);
		b7.setVisibility(View.GONE);
		bb10.setVisibility(View.GONE);

		bb10.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (cando) {

					bb10.setText("Choose Face");
					bb101.setVisibility(View.VISIBLE);
					bb102.setVisibility(View.VISIBLE);
					bb103.setVisibility(View.VISIBLE);
					bb104.setVisibility(View.VISIBLE);
					bb105.setVisibility(View.VISIBLE);
					bb106.setVisibility(View.VISIBLE);
					b.setVisibility(View.GONE);
					bb.setVisibility(View.GONE);
					b1.setVisibility(View.GONE);
					b2.setVisibility(View.GONE);
					bb2.setVisibility(View.GONE);
					b3.setVisibility(View.GONE);
					bb3.setVisibility(View.GONE);
					b4.setVisibility(View.GONE);
					b5.setVisibility(View.GONE);
					bb5.setVisibility(View.GONE);
					b6.setVisibility(View.GONE);
					b7.setVisibility(View.GONE);

				}
			}
		});
		bb101.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (cando) {
					bb10.setText("1");
					resetfunc();
					ax = 16;
					ay = 16;
					vcurrentface = 1;
					b.setVisibility(View.VISIBLE);
					bb.setVisibility(View.VISIBLE);
					b1.setVisibility(View.VISIBLE);
					b2.setVisibility(View.VISIBLE);
					bb2.setVisibility(View.VISIBLE);
					b3.setVisibility(View.VISIBLE);
					bb3.setVisibility(View.VISIBLE);
					b4.setVisibility(View.VISIBLE);
					b5.setVisibility(View.VISIBLE);
					bb5.setVisibility(View.VISIBLE);
					b6.setVisibility(View.VISIBLE);
					b7.setVisibility(View.VISIBLE);
					bb101.setVisibility(View.GONE);
					bb102.setVisibility(View.GONE);
					bb103.setVisibility(View.GONE);
					bb104.setVisibility(View.GONE);
					bb105.setVisibility(View.GONE);
					bb106.setVisibility(View.GONE);
				}
			}
		});
		bb102.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (cando) {
					bb10.setText("2");
					resetfunc();
					ay = -106;
					ax = 16;
					vcurrentface = 2;
					b.setVisibility(View.VISIBLE);
					bb.setVisibility(View.VISIBLE);
					b1.setVisibility(View.VISIBLE);
					b2.setVisibility(View.VISIBLE);
					bb2.setVisibility(View.VISIBLE);
					b3.setVisibility(View.VISIBLE);
					bb3.setVisibility(View.VISIBLE);
					b4.setVisibility(View.VISIBLE);
					b5.setVisibility(View.VISIBLE);
					bb5.setVisibility(View.VISIBLE);
					b6.setVisibility(View.VISIBLE);
					b7.setVisibility(View.VISIBLE);
					bb101.setVisibility(View.GONE);
					bb102.setVisibility(View.GONE);
					bb103.setVisibility(View.GONE);
					bb104.setVisibility(View.GONE);
					bb105.setVisibility(View.GONE);
					bb106.setVisibility(View.GONE);
				}
			}
		});
		bb103.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (cando) {
					bb10.setText("3");
					resetfunc();
					ay = 196;
					ax = 16;
					vcurrentface = 3;
					b.setVisibility(View.VISIBLE);
					bb.setVisibility(View.VISIBLE);
					b1.setVisibility(View.VISIBLE);
					b2.setVisibility(View.VISIBLE);
					bb2.setVisibility(View.VISIBLE);
					b3.setVisibility(View.VISIBLE);
					bb3.setVisibility(View.VISIBLE);
					b4.setVisibility(View.VISIBLE);
					b5.setVisibility(View.VISIBLE);
					bb5.setVisibility(View.VISIBLE);
					b6.setVisibility(View.VISIBLE);
					b7.setVisibility(View.VISIBLE);
					bb101.setVisibility(View.GONE);
					bb102.setVisibility(View.GONE);
					bb103.setVisibility(View.GONE);
					bb104.setVisibility(View.GONE);
					bb105.setVisibility(View.GONE);
					bb106.setVisibility(View.GONE);
				}
			}
		});
		bb104.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (cando) {
					bb10.setText("4");
					resetfunc();
					ay = 106;
					ax = 16;
					vcurrentface = 4;
					b.setVisibility(View.VISIBLE);
					bb.setVisibility(View.VISIBLE);
					b1.setVisibility(View.VISIBLE);
					b2.setVisibility(View.VISIBLE);
					bb2.setVisibility(View.VISIBLE);
					b3.setVisibility(View.VISIBLE);
					bb3.setVisibility(View.VISIBLE);
					b4.setVisibility(View.VISIBLE);
					b5.setVisibility(View.VISIBLE);
					bb5.setVisibility(View.VISIBLE);
					b6.setVisibility(View.VISIBLE);
					b7.setVisibility(View.VISIBLE);
					bb101.setVisibility(View.GONE);
					bb102.setVisibility(View.GONE);
					bb103.setVisibility(View.GONE);
					bb104.setVisibility(View.GONE);
					bb105.setVisibility(View.GONE);
					bb106.setVisibility(View.GONE);
				}
			}
		});
		bb105.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (cando) {

					bb10.setText("5");
					resetfunc();
					ax = 106;
					ay = (float) 0.6;
					vcurrentface = 5;
					b.setVisibility(View.VISIBLE);
					bb.setVisibility(View.VISIBLE);
					b1.setVisibility(View.VISIBLE);
					b2.setVisibility(View.VISIBLE);
					bb2.setVisibility(View.VISIBLE);
					b3.setVisibility(View.VISIBLE);
					bb3.setVisibility(View.VISIBLE);
					b4.setVisibility(View.VISIBLE);
					b5.setVisibility(View.VISIBLE);
					bb5.setVisibility(View.VISIBLE);
					b6.setVisibility(View.VISIBLE);
					b7.setVisibility(View.VISIBLE);
					bb101.setVisibility(View.GONE);
					bb102.setVisibility(View.GONE);
					bb103.setVisibility(View.GONE);
					bb104.setVisibility(View.GONE);
					bb105.setVisibility(View.GONE);
					bb106.setVisibility(View.GONE);
				}
			}
		});
		bb106.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (cando) {
					bb10.setText("6");
					resetfunc();
					ax = -106;
					ay = (float) -0.6;
					vcurrentface = 6;
					b.setVisibility(View.VISIBLE);
					bb.setVisibility(View.VISIBLE);
					b1.setVisibility(View.VISIBLE);
					b2.setVisibility(View.VISIBLE);
					bb2.setVisibility(View.VISIBLE);
					b3.setVisibility(View.VISIBLE);
					bb3.setVisibility(View.VISIBLE);
					b4.setVisibility(View.VISIBLE);
					b5.setVisibility(View.VISIBLE);
					bb5.setVisibility(View.VISIBLE);
					b6.setVisibility(View.VISIBLE);
					b7.setVisibility(View.VISIBLE);
					bb101.setVisibility(View.GONE);
					bb102.setVisibility(View.GONE);
					bb103.setVisibility(View.GONE);
					bb104.setVisibility(View.GONE);
					bb105.setVisibility(View.GONE);
					bb106.setVisibility(View.GONE);
				}
			}
		});
	}

	void resetfunc() {
		ax = 0;
		ay = 0;
	}

	@Override
	protected void onResume() {
		super.onResume();
		mView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mView.onPause();
	}

	int touch = 0;

	float dwnx, dwny;
	boolean flag = false;
	boolean cando = true;

	public boolean onTouchEvent(MotionEvent event) {
		if (cando) {
			float x = event.getX();
			float y = event.getY();

			flag = false;
			switch (event.getAction()) {
			case MotionEvent.ACTION_MOVE:
				// Calculamos el cambio

				float dx1 = x - oldX;
				float dy1 = y - oldY;
				ax += dy1;
				ay += dx1;
				touch = 0;
				break;
			case MotionEvent.ACTION_DOWN:
				dwnx = x;
				dwny = y;
			case MotionEvent.ACTION_UP:
				float dx = dwnx - oldX;
				float dy = dwny - oldY;
				// Log.d("dwnx dwny",dwnx+"  "+dwny);
				// Log.d("dx dy oldx oldy",dx+"  "+dy+"  "+oldX+"  "+oldY);

			}

			// Log.d("Angle",""+ mRenderer.getAngle(oldX,oldY,x,y));

			// float degrees = (int)(Math.toDegrees(Math.atan2(y, x)));
			// myAngle = degrees;

			// Log.d("Angle",""+ myAngle);
			oldX = x;
			oldY = y;

			// mRenderer.onTouchEvent(event);
			// return super.onTouchEvent(event);
		}
		return true;
	}

	private float myAngle;
	boolean direction;
	int layerID = 0;
	static int ijk = 0;

	public void animate() {
		// change our angle of view
		if (getStopani() == 0) {
			mRenderer.setAngle(mRenderer.getAngle() + 3f);

			if (cando == false) {
				if (mCurrentLayer == null) {
					int layerID = mRandom.nextInt(9);
					mCurrentLayer = mLayers[layerID];
					mCurrentLayerPermutation = mLayerPermutations[layerID];
					mCurrentLayer.startAnimation();
					boolean direction = mRandom.nextBoolean();
					int count = mRandom.nextInt(3) + 1;

					count = 1;
					direction = false;
					mCurrentAngle = 0;
					if (direction) {
						mAngleIncrement = (float) Math.PI / 50;
						mEndAngle = mCurrentAngle + ((float) Math.PI * count)
								/ 2f;
					} else {
						mAngleIncrement = -(float) Math.PI / 50;
						mEndAngle = mCurrentAngle - ((float) Math.PI * count)
								/ 2f;
					}
				}

				mCurrentAngle += mAngleIncrement;

				if ((mAngleIncrement > 0f && mCurrentAngle >= mEndAngle)
						|| (mAngleIncrement < 0f && mCurrentAngle <= mEndAngle)) {
					mCurrentLayer.setAngle(mEndAngle);
					mCurrentLayer.endAnimation();
					mCurrentLayer = null;
					Log.d("Here there", "over" + mCurrentAngle);
					if (ijk == 100) {
						cando = true;
						setStopani(1);
						myAngle = mEndAngle;
					} else {
						cando = false;
						// ijk++;
					}

					// adjust mPermutation based on the completed layer rotation
					int[] newPermutation = new int[27];
					for (int i = 0; i < 27; i++) {
						newPermutation[i] = mPermutation[mCurrentLayerPermutation[i]];
					}
					mPermutation = newPermutation;
					updateLayers();

				} else {
					Log.d("Here", "over" + mCurrentAngle);
					mCurrentLayer.setAngle(mCurrentAngle);
					// cando=true;
				}

			}

		} else if (getStopani() == 1) {
			if (vcurrentface == 6) {
				mRenderer
						.setAngle(-ax, ay, getDeviceHeight(), getDeviceWidth());

			} else {
				mRenderer
						.setAngle(-ax, ay, getDeviceHeight(), getDeviceWidth());
			}

			float xx = (-ax) % 360;
			float yy = ay % 360;
			int checkt = 0;
			// Log.d("Anglej", ""+ax +" " +yy);
			if ((xx <= -75 && xx >= -165) || (xx >= 255 && xx <= 345)) {
				Log.d("TOP", "top is here");

			}
			if ((xx >= 75 && xx <= 165) || (xx <= -255 && xx >= -345)) {

			}
			if ((yy <= -75 && xx >= -165) || (yy >= 255 && xx <= 345)) {
				Log.d("Right", "right is here");

			}
			if ((xx <= -75 && xx >= -165) || (xx >= 255 && xx <= 345)) {
				Log.d("TOP", "top is here");

			}
			if ((xx <= -75 && xx >= -165) || (xx >= 255 && xx <= 345)) {
				Log.d("TOP", "top is here");

			}
			if ((xx <= -75 && xx >= -165) || (xx >= 255 && xx <= 345)) {
				Log.d("TOP", "top is here");

			}

			// mRenderer.setAngle(mRenderer.getAngle()+myAngle);

			// newcube=1;
			// mRenderer = new KubeRenderer(makeGLWorld(), this);

			/*
			 * if (touch > 0) {
			 * 
			 * if (touch == 1) { layerID = 0; } else if (touch == 2) { layerID =
			 * 2; } else if (touch == 3) { layerID = 4; } else if (touch == 4) {
			 * layerID = 5; } }
			 */
			if (flag) {

				cando = false;
				if (mCurrentLayer == null) {
					mCurrentLayer = mLayers[layerID];
					mCurrentLayerPermutation = mLayerPermutations[layerID];
					mCurrentLayer.startAnimation();
					// direction = false;
					int count = mRandom.nextInt(3) + 1;

					count = 1;
					// direction = false;
					mCurrentAngle = 0;
					if (direction) {
						mAngleIncrement = (float) Math.PI / 50;
						mEndAngle = mCurrentAngle + ((float) Math.PI * count)
								/ 2f;
					} else {
						mAngleIncrement = -(float) Math.PI / 50;
						mEndAngle = mCurrentAngle - ((float) Math.PI * count)
								/ 2f;
					}
				}

				mCurrentAngle += mAngleIncrement;

				if ((mAngleIncrement > 0f && mCurrentAngle >= mEndAngle)
						|| (mAngleIncrement < 0f && mCurrentAngle <= mEndAngle)) {
					mCurrentLayer.setAngle(mEndAngle);
					mCurrentLayer.endAnimation();
					mCurrentLayer = null;
					flag = false;

					// adjust mPermutation based on the completed layer rotation
					if (mAngleIncrement < 0) { // checks the turning direction
						int[] newPermutation = new int[27];
						for (int i = 0; i < 27; i++) {
							newPermutation[i] = mPermutation[mCurrentLayerPermutation[i]];

						}
						mPermutation = newPermutation;
						updateLayers();
						finishFunc();
					} else {
						int[] newPermutation = new int[27];
						for (int i = 0; i < 27; i++) {
							newPermutation[i] = mPermutation[mCurrentLayerPermutation[i]];
						}
						mPermutation = newPermutation;

						newPermutation = new int[27];
						for (int i = 0; i < 27; i++) {
							newPermutation[i] = mPermutation[mCurrentLayerPermutation[i]];
						}
						mPermutation = newPermutation;

						newPermutation = new int[27];
						for (int i = 0; i < 27; i++) {
							newPermutation[i] = mPermutation[mCurrentLayerPermutation[i]];
						}
						mPermutation = newPermutation;
						updateLayers();
						finishFunc();

					}

					cando = true;
				} else {
					mCurrentLayer.setAngle(mCurrentAngle);
				}
			}
		}

	}

	public static int getStopani() {
		return stopani;
	}

	public static void setStopani(int stopani) {
		MainActivity.stopani = stopani;
	}

	GLSurfaceView mView;
	KubeRenderer mRenderer;
	RotationGestureDetector mRotationDetector;
	Cube[] mCubes = new Cube[27];
	// a Layer for each possible move
	Layer[] mLayers = new Layer[9];
	// permutations corresponding to a pi/2 rotation of each layer about its
	// axis
	static int[][] mLayerPermutations = {
			// permutation for UP layer
			{ 2, 5, 8, 1, 4, 7, 0, 3, 6, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18,
					19, 20, 21, 22, 23, 24, 25, 26 },
			// permutation for DOWN layer
			{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 20,
					23, 26, 19, 22, 25, 18, 21, 24 },
			// permutation for LEFT layer
			{ 6, 1, 2, 15, 4, 5, 24, 7, 8, 3, 10, 11, 12, 13, 14, 21, 16, 17,
					0, 19, 20, 9, 22, 23, 18, 25, 26 },
			// permutation for RIGHT layer
			{ 0, 1, 8, 3, 4, 17, 6, 7, 26, 9, 10, 5, 12, 13, 14, 15, 16, 23,
					18, 19, 2, 21, 22, 11, 24, 25, 20 },
			// permutation for FRONT layer
			{ 0, 1, 2, 3, 4, 5, 24, 15, 6, 9, 10, 11, 12, 13, 14, 25, 16, 7,
					18, 19, 20, 21, 22, 23, 26, 17, 8 },
			// permutation for BACK layer
			{ 18, 9, 0, 3, 4, 5, 6, 7, 8, 19, 10, 1, 12, 13, 14, 15, 16, 17,
					20, 11, 2, 21, 22, 23, 24, 25, 26 },
			// permutation for MIDDLE layer
			{ 0, 7, 2, 3, 16, 5, 6, 25, 8, 9, 4, 11, 12, 13, 14, 15, 22, 17,
					18, 1, 20, 21, 10, 23, 24, 19, 26 },
			// permutation for EQUATOR layer
			{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 11, 14, 17, 10, 13, 16, 9, 12, 15, 18,
					19, 20, 21, 22, 23, 24, 25, 26 },
			// permutation for SIDE layer
			{ 0, 1, 2, 21, 12, 3, 6, 7, 8, 9, 10, 11, 22, 13, 4, 15, 16, 17,
					18, 19, 20, 23, 14, 5, 24, 25, 26 } };

	// current permutation of starting position
	int[] mPermutation;

	// for random cube movements
	Random mRandom = new Random(System.currentTimeMillis());
	// currently turning layer
	Layer mCurrentLayer = null;
	// current and final angle for current Layer animation
	float mCurrentAngle, mEndAngle;
	// amount to increment angle
	float mAngleIncrement;
	int[] mCurrentLayerPermutation;

	static final int kUp = 0;
	static final int kDown = 1;
	static final int kLeft = 2;
	static final int kRight = 3;
	static final int kFront = 4;
	static final int kBack = 5;
	static final int kMiddle = 6;
	static final int kEquator = 7;
	static final int kSide = 8;

}