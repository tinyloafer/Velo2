package zzw.velo;

/**
 * Created by zzw on 2015/12/20.
 */
import zzw.velo.*;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

public class LoadActivity extends Activity{
    private static final int Load_Display_Time=3000;
    @SuppressWarnings("deprecation")
    public void onCreate(Bundle saveInstanceState)
    {
        super.onCreate(saveInstanceState);
        getWindow().setFormat(PixelFormat.RGBA_8888);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);

        setContentView(R.layout.load);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent  mainintent=new Intent(LoadActivity.this, MainTabActivity.class);
                LoadActivity.this.startActivity(mainintent);
                LoadActivity.this.finish();
            }
        }, Load_Display_Time);

    }
}

