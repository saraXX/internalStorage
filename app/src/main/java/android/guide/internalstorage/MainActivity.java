package android.guide.internalstorage;

import static java.lang.System.exit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/* todo 0 this app doesn't need any permission .
    also the data will be saved in the device locally in place called internal storage
    system create a directory for every app installed called internal storage
    u can remove these files using delete button, or it will be deleted when u uninstall the app from your app.
    the internal storage is very helpful to store sensitive important small size data that you app depends on
    other apps can't access this directory at all
    .
    .
    .
    more info see (Access app-specific files guide)
    .
    .
    https://developer.android.com/training/data-storage/app-specific
 */


public class MainActivity extends AppCompatActivity {
    Button mNormalSaveBtn, mShowAllBtn, mOpenBtn, mDeleteBtn;
    EditText mFileNameET, mFileContentET, mTextBox;
    String mFileName, mFileContent, TAG;
    String[] allFiles;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TAG = " msg";
        mNormalSaveBtn = findViewById(R.id.nSaveBtn);
        mShowAllBtn = findViewById(R.id.showBtn);
        mOpenBtn = findViewById(R.id.openBtn);
        mDeleteBtn = findViewById(R.id.nDeleteBtn);

        mFileContentET = findViewById(R.id.cTextBox);
        mFileNameET = findViewById(R.id.nTextBox);
        mTextBox = findViewById(R.id.shTextBox);

        //    TODO 0.2 make the context
        context = MainActivity.this;

        mNormalSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFileNameET.getText() != null && mFileContentET != null) {
                    mFileName = mFileNameET.getText().toString();
                    mFileContent = mFileContentET.getText().toString();
                    saveFile(mFileName, mFileContent);
//                    make edit texts empty after saving a file
                    mFileContentET.setText("");
                    mFileNameET.setText("");
                } else
                    Toast.makeText(context, "write the file name and content", Toast.LENGTH_SHORT).show();
            }
        });

        mOpenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: " + mFileNameET.getText().toString().trim());
                if (mFileNameET.getText().toString().isEmpty()) {
                    Toast.makeText(context, "make sure to write the file name", Toast.LENGTH_SHORT).show();
                } else {
                    mFileName = mFileNameET.getText().toString();
                    mFileContent = openFile(mFileName);
                    mFileContentET.setText(mFileContent);
                }
            }
        });

        mShowAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allFiles = showAllfiles();
                if (allFiles.length > 0) {
                    mTextBox.setText(Arrays.toString(allFiles));
                } else
                    Toast.makeText(context, "there are no files", Toast.LENGTH_SHORT).show();

            }
        });

        mDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFileName = mFileNameET.getText().toString();
                if (delFile(mFileName) == false) {
                    Toast.makeText(context, "no file ", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(context,mFileName+" has deleted", Toast.LENGTH_SHORT).show();
                mFileContentET.setText("");
                mTextBox.setText("");
            }
        });

    }

    //        TODO 1 : create and save a file. using FileOutputStream,
//             file will be saved inside internal storage| storage/android/app/data
    public void saveFile(String fName, String fContent) {
        try (FileOutputStream file = this.openFileOutput(fName, Context.MODE_PRIVATE)) {
//            must convert string to byte[] array
            file.write(fContent.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //    TODO 2 : Access a file by its name, and return its content as a string
    public String openFile(String fileName) {
        String content = "";
        FileInputStream file = null;
        try {
            file = this.openFileInput(fileName);
        } catch (FileNotFoundException e) {
            Toast.makeText(context,"there is no file with this name",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
//            if the file not exist just return nothing
            return "";
        }
        InputStreamReader inputStreamReader =
                new InputStreamReader(file, StandardCharsets.UTF_8);
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
            String line = reader.readLine();
            while (line != null) {
                builder.append(line).append('\n');
                line = reader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return content = builder.toString();
        }
    }

    // todo 3 : show all file inside internal storage as an array of string
    public String[] showAllfiles() {
        String[] files = context.fileList();
        return files;
    }
// TODO 4 : delete file by passing file name
    public Boolean delFile(String fName) {
        return context.deleteFile(fName);
    }


}