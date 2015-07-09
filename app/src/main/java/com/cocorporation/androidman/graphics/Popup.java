package com.cocorporation.androidman.graphics;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.cocorporation.androidman.R;

/**
 * Created by Corentin on 6/20/2015.
 */
public class Popup {

    public Popup(Context context) {
        Activity parent = (Activity) context;

        // here you can have the instance of the holder from glSurfaceView
        LinearLayout parentLayout = (LinearLayout) parent.findViewById(R.id.popupHolder);

        // inflate your custom popup layout or create it dynamically
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view =  inflater.inflate(R.layout.win_popup, null);

        // Now you should create a PopupWindow
        PopupWindow popupWindow = new PopupWindow(view,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        // you can change the offset values or the Gravity. This will diaply the popup in the center
        // of your glSurfaceView
        popupWindow.showAtLocation(parentLayout, Gravity.CENTER, 0, 0);
    }
}

/*
package com.androidituts.popup;

        import android.app.Activity;
        import android.content.Context;
        import android.os.Bundle;
        import android.view.Gravity;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.view.View.OnClickListener;
        import android.widget.Button;
        import android.widget.PopupWindow;

public class PopupActivity extends Activity {
    Button btnClosePopup;
    Button btnCreatePopup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        btnCreatePopup = (Button) findViewById(R.id.button1);
        btnCreatePopup.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
// TODO Auto-generated method stub
                initiatePopupWindow();
            }
        });

    }

    private PopupWindow pwindo;

    private void initiatePopupWindow() {
        try {
// We need to get the instance of the LayoutInflater
            LayoutInflater inflater = (LayoutInflater) PopupActivity.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.popup,(ViewGroup)

                    findViewById(R.id.popup_element));
            pwindo = new PopupWindow(layout, 350, 350, true);
            pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);

            btnClosePopup = (Button) layout.findViewById(R.id.btn_close_popup);
            btnClosePopup.setOnClickListener(cancel_button_click_listener);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private OnClickListener cancel_button_click_listener = new OnClickListener() {
        public void onClick(View v) {
            pwindo.dismiss();

        }
    };
}
*/
