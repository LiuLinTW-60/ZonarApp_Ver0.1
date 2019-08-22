package com.james.easyanimation;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

public class EasyAnimation {
	private static EasyAnimation anim;
	private AlphaAnimation alphaIn;
	private AlphaAnimation alphaOut;
	private TranslateAnimation topIn;
	private TranslateAnimation topOut;
	private TranslateAnimation bottomIn;
	private TranslateAnimation bottomOut;
	private TranslateAnimation rightIn;
	private TranslateAnimation rightOut;
	private TranslateAnimation leftIn;
	private TranslateAnimation leftOut;
	private ScaleAnimation nearIn;
	private ScaleAnimation nearOut;
	private ScaleAnimation farIn;
	private ScaleAnimation farOut;

	private AnimationSet rotateRightIn;
	private AnimationSet rotateLeftIn;
	private AnimationSet rotateRightOut;
	private AnimationSet rotateLeftOut;
	private AnimationSet alphaRightIn;
	private AnimationSet alphaRightOut;
	private AnimationSet alphaLeftIn;
	private AnimationSet alphaLeftOut;
	
	private RotateAnimation rotate;
	
	private ScaleAnimation slideDownIn;
	private ScaleAnimation slideUpOut;
	
	public static EasyAnimation getInstance(){
		if(anim == null){
			anim = new EasyAnimation();
		}
		return anim;
	}
	
	public AlphaAnimation getAlphaIn(int duration){
		if(alphaIn == null){
			alphaIn = new AlphaAnimation(0, 1);
		}
		alphaIn.setDuration(duration);
		alphaIn.setAnimationListener(null);
		
		return alphaIn;
	}
	
	public AlphaAnimation getAlphaOut(int duration){
		if(alphaOut == null){
			alphaOut = new AlphaAnimation(1, 0);
		}
		alphaOut.setDuration(duration);
		alphaOut.setAnimationListener(null);
		
		return alphaOut;
	}
	
	public TranslateAnimation getTopIn(int duration){
		if(topIn == null){
			topIn = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 0,
					Animation.RELATIVE_TO_SELF, 0,
					Animation.RELATIVE_TO_SELF, -1,
					Animation.RELATIVE_TO_SELF, 0);
		}
		topIn.setDuration(duration);
		topIn.setAnimationListener(null);
		
		return topIn;
	}
	
	public TranslateAnimation getTopOut(int duration){
		if(topOut == null){
			topOut = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 0,
					Animation.RELATIVE_TO_SELF, 0,
					Animation.RELATIVE_TO_SELF, 0,
					Animation.RELATIVE_TO_SELF, -1);
		}
		topOut.setDuration(duration);
		topOut.setAnimationListener(null);
		
		return topOut;
	}
	
	public TranslateAnimation getBottomIn(int duration){
		if(bottomIn == null){
			bottomIn = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 0,
					Animation.RELATIVE_TO_SELF, 0,
					Animation.RELATIVE_TO_SELF, 1,
					Animation.RELATIVE_TO_SELF, 0);
		}
		bottomIn.setDuration(duration);
		bottomIn.setAnimationListener(null);
		
		return bottomIn;
	}
	
	public TranslateAnimation getBottomOut(int duration){
		if(bottomOut == null){
			bottomOut = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 0,
					Animation.RELATIVE_TO_SELF, 0,
					Animation.RELATIVE_TO_SELF, 0,
					Animation.RELATIVE_TO_SELF, 1);
		}
		bottomOut.setDuration(duration);
		bottomOut.setAnimationListener(null);
		
		return bottomOut;
	}
	
	public TranslateAnimation getRightIn(int duration){
		if(rightIn == null){
			rightIn = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 1,
					Animation.RELATIVE_TO_SELF, 0,
					Animation.RELATIVE_TO_SELF, 0,
					Animation.RELATIVE_TO_SELF, 0);
		}
		rightIn.setDuration(duration);
		rightIn.setAnimationListener(null);
		
		return rightIn;
	}
	
	public TranslateAnimation getRightOut(int duration){
		if(rightOut == null){
			rightOut = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 0,
					Animation.RELATIVE_TO_SELF, 1,
					Animation.RELATIVE_TO_SELF, 0,
					Animation.RELATIVE_TO_SELF, 0);
		}
		rightOut.setDuration(duration);
		rightOut.setAnimationListener(null);
		
		return rightOut;
	}
	
	public TranslateAnimation getLeftIn(int duration){
		if(leftIn == null){
			leftIn = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, -1,
					Animation.RELATIVE_TO_SELF, 0,
					Animation.RELATIVE_TO_SELF, 0,
					Animation.RELATIVE_TO_SELF, 0);
		}
		leftIn.setDuration(duration);
		leftIn.setAnimationListener(null);
		
		return leftIn;
	}
	
	public TranslateAnimation getLeftOut(int duration){
		if(leftOut == null){
			leftOut = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 0,
					Animation.RELATIVE_TO_SELF, -1,
					Animation.RELATIVE_TO_SELF, 0,
					Animation.RELATIVE_TO_SELF, 0);
		}
		leftOut.setDuration(duration);
		leftOut.setAnimationListener(null);
		
		return leftOut;
	}
	
	public ScaleAnimation getNearIn(int duration){
		if(nearIn == null){
			nearIn = new ScaleAnimation(
					3, 1,
					3, 1,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
		}
		nearIn.setDuration(duration);
		nearIn.setAnimationListener(null);
		
		return nearIn;
	}
	
	public ScaleAnimation getNearOut(int duration){
		if(nearOut == null){
			nearOut = new ScaleAnimation(
					1, 3,
					1, 3,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
		}
		nearOut.setDuration(duration);
		nearOut.setAnimationListener(null);
		
		return nearOut;
	}
	
	public ScaleAnimation getFarIn(int duration){
		if(farIn == null){
			farIn = new ScaleAnimation(
					0.3f, 1,
					0.3f, 1,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
		}
		farIn.setDuration(duration);
		farIn.setAnimationListener(null);
		
		return farIn;
	}
	
	public ScaleAnimation getFarOut(int duration){
		if(farOut == null){
			farOut = new ScaleAnimation(
					1, 0.3f,
					1, 0.3f,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
		}
		farOut.setDuration(duration);
		farOut.setAnimationListener(null);
		
		return farOut;
	}
	
	public AnimationSet getRotateRightIn(int duration){
		if(rotateRightIn == null){
			rotateRightIn = new AnimationSet(true);

			TranslateAnimation in1 = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 1,
					Animation.RELATIVE_TO_SELF, 0,
					Animation.RELATIVE_TO_SELF, 0,
					Animation.RELATIVE_TO_SELF, 0);
			in1.setDuration(duration);
			rotateRightIn.addAnimation(in1);
			
			ScaleAnimation in2 = new ScaleAnimation(
					0.3f, 1,
					0.3f, 1,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			in2.setDuration(duration);
			rotateRightIn.addAnimation(in2);
		}
		rotateRightIn.setDuration(duration);
		rotateRightIn.setAnimationListener(null);
		
		return rotateRightIn;
	}
	
	public AnimationSet getRotateLeftIn(int duration){
		if(rotateLeftIn == null){
			rotateLeftIn = new AnimationSet(true);

			TranslateAnimation in1 = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, -1,
					Animation.RELATIVE_TO_SELF, 0,
					Animation.RELATIVE_TO_SELF, 0,
					Animation.RELATIVE_TO_SELF, 0);
			in1.setDuration(duration);
			rotateLeftIn.addAnimation(in1);
			
			ScaleAnimation in2 = new ScaleAnimation(
					0.3f, 1,
					0.3f, 1,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			in2.setDuration(duration);
			rotateLeftIn.addAnimation(in2);
		}
		rotateLeftIn.setDuration(duration);
		rotateLeftIn.setAnimationListener(null);
		
		return rotateLeftIn;
	}
	
	public AnimationSet getRotateRightOut(int duration){
		if(rotateRightOut == null){
			rotateRightOut = new AnimationSet(true);

			TranslateAnimation in1 = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 0,
					Animation.RELATIVE_TO_SELF, 1,
					Animation.RELATIVE_TO_SELF, 0,
					Animation.RELATIVE_TO_SELF, 0);
			in1.setDuration(duration);
			rotateRightOut.addAnimation(in1);
			
			ScaleAnimation out2 = new ScaleAnimation(
					1, 0.3f,
					1, 0.3f,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			out2.setDuration(duration);
			rotateRightOut.addAnimation(out2);
		}
		rotateRightOut.setDuration(duration);
		rotateRightOut.setAnimationListener(null);
		
		return rotateRightOut;
	}
	
	public AnimationSet getRotateLeftOut(int duration){
		if(rotateLeftOut == null){
			rotateLeftOut = new AnimationSet(true);

			TranslateAnimation out1 = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 0,
					Animation.RELATIVE_TO_SELF, -1,
					Animation.RELATIVE_TO_SELF, 0,
					Animation.RELATIVE_TO_SELF, 0);
			out1.setDuration(duration);
			rotateLeftOut.addAnimation(out1);
			
			ScaleAnimation out2 = new ScaleAnimation(
					1, 0.3f,
					1, 0.3f,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			out2.setDuration(duration);
			rotateLeftOut.addAnimation(out2);
		}
		rotateLeftOut.setDuration(duration);
		rotateLeftOut.setAnimationListener(null);
		
		return rotateLeftOut;
	}
	
	public AnimationSet getAlphaRightIn(int duration){
		if(alphaRightIn == null){
			alphaRightIn = new AnimationSet(true);

			TranslateAnimation in1 = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 1,
					Animation.RELATIVE_TO_SELF, 0,
					Animation.RELATIVE_TO_SELF, 0,
					Animation.RELATIVE_TO_SELF, 0);
			in1.setDuration(duration);
			alphaRightIn.addAnimation(in1);
			
			AlphaAnimation in2 = new AlphaAnimation(
					0, 1);
			in2.setDuration(duration);
			alphaRightIn.addAnimation(in2);
		}
		alphaRightIn.setDuration(duration);
		alphaRightIn.setAnimationListener(null);
		
		return alphaRightIn;
	}
	
	public AnimationSet getAlphaLeftIn(int duration){
		if(alphaLeftIn == null){
			alphaLeftIn = new AnimationSet(true);

			TranslateAnimation in1 = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, -1,
					Animation.RELATIVE_TO_SELF, 0,
					Animation.RELATIVE_TO_SELF, 0,
					Animation.RELATIVE_TO_SELF, 0);
			in1.setDuration(duration);
			alphaLeftIn.addAnimation(in1);
			
			AlphaAnimation in2 = new AlphaAnimation(
					0, 1);
			in2.setDuration(duration);
			alphaLeftIn.addAnimation(in2);
		}
		alphaLeftIn.setDuration(duration);
		alphaLeftIn.setAnimationListener(null);
		
		return alphaLeftIn;
	}
	
	public AnimationSet getAlphaRightOut(int duration){
		if(alphaRightOut == null){
			alphaRightOut = new AnimationSet(true);

			TranslateAnimation out1 = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 0,
					Animation.RELATIVE_TO_SELF, 1,
					Animation.RELATIVE_TO_SELF, 0,
					Animation.RELATIVE_TO_SELF, 0);
			out1.setDuration(duration);
			alphaRightOut.addAnimation(out1);
			
			AlphaAnimation out2 = new AlphaAnimation(
					1, 0);
			out2.setDuration(duration);
			alphaRightOut.addAnimation(out2);
		}
		alphaRightOut.setDuration(duration);
		alphaRightOut.setAnimationListener(null);
		
		return alphaRightOut;
	}
	
	public AnimationSet getAlphaLeftOut(int duration){
		if(alphaLeftOut == null){
			alphaLeftOut = new AnimationSet(true);

			TranslateAnimation out1 = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 0,
					Animation.RELATIVE_TO_SELF, -1,
					Animation.RELATIVE_TO_SELF, 0,
					Animation.RELATIVE_TO_SELF, 0);
			out1.setDuration(duration);
			alphaLeftOut.addAnimation(out1);
			
			AlphaAnimation out2 = new AlphaAnimation(
					1, 0);
			out2.setDuration(duration);
			alphaLeftOut.addAnimation(out2);
		}
		alphaLeftOut.setDuration(duration);
		alphaLeftOut.setAnimationListener(null);
		
		return alphaLeftOut;
	}
	
	public RotateAnimation getRotate(int duration){
		if(rotate == null){
			rotate = new RotateAnimation(
					0, 360,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
		}
		rotate.setDuration(duration);
		rotate.setAnimationListener(null);
		
		return rotate;
	}
	
	public ScaleAnimation getSlideDownIn(int duration){
		if(slideDownIn == null){
			slideDownIn = new ScaleAnimation(1, 1,
					0, 1,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0);
		}
		slideDownIn.setDuration(duration);
		slideDownIn.setAnimationListener(null);
		
		return slideDownIn;
	}
	
	public ScaleAnimation getSlideUpOut(int duration){
		if(slideUpOut == null){
			slideUpOut = new ScaleAnimation(1, 1,
					1, 0,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0);
		}
		slideUpOut.setDuration(duration);
		slideUpOut.setAnimationListener(null);
		
		return slideUpOut;
	}
}
