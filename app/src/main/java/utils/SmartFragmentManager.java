package utils;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.app.DialogFragment;

import com.example.matan.hw1app.R;

import controller.EditDialog;

public class SmartFragmentManager {
	private static SmartFragmentManager instance = null;
	private FragmentManager fManager = null;
	private android.support.v4.app.FragmentManager fragManager = null;
	public static final int NONE = 0;
	public static final int SLIDE_LEFT = 1;
	public static final int SLIDE_UP = 2;

	public static SmartFragmentManager getInstance() {
		if (instance == null) {
			instance = new SmartFragmentManager();
		}
		return instance;
	}

	public static void releaseInstance() {
		if (instance != null) {
			instance.clean();
			instance = null;
		}
	}

	private void clean() {

	}
	
	public void ReplaceToFragment(Fragment newfragment, int containerViewId,String fragTag) {
		ReplaceToFragment(newfragment, NONE, containerViewId, fragTag);
	}

	public void ReplaceToFragment(Fragment newfragment, int animationtype, int containerViewId, String fragTag) {
		if (newfragment != null && fManager != null) {
			FragmentTransaction ft = fManager.beginTransaction();
			switch (animationtype) {
			case NONE:

				break;
			case SLIDE_LEFT:
				ft.setCustomAnimations(R.animator.slide_in_left,
						R.animator.slide_out_right);
				break;
			case SLIDE_UP:
				ft.setCustomAnimations(R.animator.slide_in_up,
						R.animator.slide_out_down);
				break;

			default:
				break;
			}

			ft.replace( containerViewId, newfragment, fragTag);
			ft.addToBackStack(null);
			ft.commit();

		}

	}

	public void setfManager(FragmentManager fManager) {
		this.fManager = fManager;
	}

	public FragmentManager getfManager() {
		return fManager;
	}
}
