package module.data;

import android.app.Activity;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.channels.AcceptPendingException;

public class FileProcessing {
    private Activity mActivity;
    private static final String TAG = FileProcessing.class.toString();

    public FileProcessing(Activity activity){
        mActivity = activity;
    }

    public void save (String text, String name) throws IOException {
        BufferedWriter writer = null;
        FileOutputStream output = null;
        try {
            File file = new File(mActivity.getCacheDir() + File.separator + name);
            output = new FileOutputStream(file);
            writer = new BufferedWriter(new OutputStreamWriter(output));
            writer.write(text);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(writer != null){
                writer.close();
            }
            if(output != null){
                output.close();
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
