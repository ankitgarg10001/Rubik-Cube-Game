package com.jiit.rubikcube;

/*import android.opengl.GLSurfaceView;
 import javax.microedition.khronos.egl.EGLConfig;
 import javax.microedition.khronos.opengles.GL10;

 class KubeRenderer implements GLSurfaceView.Renderer {
 public interface AnimationCallback {
 void animate();
 }

 public KubeRenderer(GLWorld world, AnimationCallback callback) {
 mWorld = world;
 mCallback = callback;
 }

 public void onDrawFrame(GL10 gl) {
 if (mCallback != null) {
 mCallback.animate();
 }
 gl.glClearColor(0.5f,0.5f,0.5f,1);
 gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
 gl.glMatrixMode(GL10.GL_MODELVIEW);
 gl.glLoadIdentity();
 gl.glTranslatef(0, 0, -3.0f);
 gl.glScalef(0.5f, 0.5f, 0.5f);
 gl.glRotatef(mAngle,        0, 1, 0);
 gl.glRotatef(mAngle*0.25f,  1, 0, 0);

 gl.glColor4f(0.7f, 0.7f, 0.7f, 1.0f);
 gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
 gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
 gl.glEnable(GL10.GL_CULL_FACE);
 gl.glShadeModel(GL10.GL_SMOOTH);
 gl.glEnable(GL10.GL_DEPTH_TEST);

 mWorld.draw(gl);
 }

 public void onSurfaceChanged(GL10 gl, int width, int height) {
 gl.glViewport(0, 0, width, height);
 float ratio = (float)width / height;
 gl.glMatrixMode(GL10.GL_PROJECTION);
 gl.glLoadIdentity();
 gl.glFrustumf(-ratio, ratio, -1, 1, 2, 12);
 gl.glDisable(GL10.GL_DITHER);
 gl.glActiveTexture(GL10.GL_TEXTURE0);
 }

 public void onSurfaceCreated(GL10 gl, EGLConfig config) {
 }

 public void setAngle(float angle) {
 mAngle = angle;
 }

 public void setAngle(float x, float y, int height, int width) {
 ax = x;
 ay = y;
 }
 private float ax, ay;

 public float getAngle() {
 return mAngle;
 }

 private GLWorld mWorld;
 private AnimationCallback mCallback;
 private float mAngle;
 }*/
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView;
import android.util.DisplayMetrics;

class KubeRenderer implements GLSurfaceView.Renderer {
	MainActivity A;

	public interface AnimationCallback {
		void animate();

		// public void OnRotation(KubeRenderer kubeRenderer);

	}

	public KubeRenderer(GLWorld world, AnimationCallback callback) {
		mWorld = world;
		mCallback = callback;

	}

	public void onDrawFrame(GL10 gl) {
		if (mCallback != null) {
			mCallback.animate();
		}
		gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();

		gl.glTranslatef(0, 0, -3.0f);
		gl.glScalef(0.5f, 0.5f, 0.5f);
		if (MainActivity.getStopani() == 0) {

			gl.glRotatef(mAngle, 0, 1, 0);
			gl.glRotatef(mAngle * 0.25f, 1, 0, 0);
		} else {
			gl.glRotatef(-ax, 1, 0, 0);
			gl.glRotatef(ay, 0, 1, 0);
		}

		// gl.glRotatef(mAngle, 1, 1, 1);
		// gl.glRotatef(mAngle*0.25f, 1, 0, 0);
		gl.glColor4f(0.7f, 0.7f, 0.7f, 1.0f);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glEnable(GL10.GL_DEPTH_TEST);

		mWorld.draw(gl);
	}

	public void onSurfaceChanged(GL10 gl, int width, int height) {
		gl.glViewport(0, 0, width, height);
		float ratio = (float) width / height;
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glFrustumf(-ratio, ratio, -1, 1, 2, 12);
		gl.glDisable(GL10.GL_DITHER);
		gl.glActiveTexture(GL10.GL_TEXTURE0);
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
	}

	public void setAngle(float x, float y, int height, int width) {
		ax = x;
		ay = y;
	}

	public void setAngle(float angle) {
		mAngle = angle;
	}

	public float getAngle() {
		return mAngle;
	}

	public float getAngle(float ox, float oy, float x, float y) {
		// float A;
		return 1 / (ox - oy) * (x * ox + y * (-oy));

		// return ax;
	}

	final DisplayMetrics displayMetrics = new DisplayMetrics();
	float density = displayMetrics.density;
	private GLWorld mWorld;
	private float mAngle;

	private AnimationCallback mCallback;
	// private float mAngle;
	private float ax, ay;
}