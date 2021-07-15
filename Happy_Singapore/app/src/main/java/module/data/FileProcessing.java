package module.data;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.channels.AcceptPendingException;

import static android.content.Context.MODE_PRIVATE;

public class FileProcessing {
    private Activity mActivity;
    private static final String TAG = FileProcessing.class.toString();

    public FileProcessing(Activity activity){
        mActivity = activity;
    }

    public void save (String text, String name, int mode) throws IOException {
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            out = mActivity.openFileOutput(name, mode);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.i(TAG, "Successfully saved in " + name);
    }

    public String read (String name) throws IOException {
        FileInputStream input = null;
        String result = null;
        try {
            File file = new File(mActivity.getCacheDir() + File.separator + name);
            input = new FileInputStream(file);
            int size = input.available();
            byte[] result_bytes = new byte[size];
            input.read(result_bytes);
            result = new String(result_bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(input != null){
                input.close();
            }
        }
        return result;
    }
}
