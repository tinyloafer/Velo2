package zzw.velo;

/**
 * Created by zzw on 2015/12/20.
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;


public class CDialog {
    public static ProgressDialog progressDialog;

    /**
     * ��ʾ���ؽ���,���ǽ�������ʾ������μ�progressDialog�÷�
     * @param context
     * @param load
     */
    @SuppressWarnings("deprecation")
    public static void showDialog(Activity context,int load) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
        }else{
            progressDialog.dismiss();
        }

        progressDialog.setButton(context.getString(R.string.ok), new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismissDialog();
            }
        });

        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(context.getString(load));

        progressDialog.show();
    }

    /**
     * ��ʧ���ؽ���
     */
    public static void dismissDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}