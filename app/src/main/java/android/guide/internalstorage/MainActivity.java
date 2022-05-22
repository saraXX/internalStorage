package android.guide.internalstorage;

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


public class MainActivity extends AppCompatActivity {
Button mNormalBtn, mCacheBtn, mShowBtn, mOpenBtn;
EditText mFileNameET, mFileContentET, mTextBox;
String mFileName, mFileContent, TAG;
String[] allFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TAG = " msg";
        mNormalBtn = findViewById(R.id.nSaveBtn);
        mCacheBtn = findViewById(R.id.cSaveBtn);
        mShowBtn = findViewById(R.id.showBtn);
        mOpenBtn = findViewById(R.id.openBtn);

        mFileContentET = findViewById(R.id.cTextBox);
        mFileNameET = findViewById(R.id.nTextBox);
        mTextBox = findViewById(R.id.shTextBox);

//        TODO 1 : create and save a file. using FileOutputStream
        mNormalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mFileNameET.getText()!=null&&mFileContentET!=null){
                    mFileName = mFileNameET.getText().toString();
                    mFileContent = mFileContentET.getText().toString();
                    saveFile(mFileName,mFileContent);
                    mFileContentET.setText("");
                    mFileNameET.setText("");
                }
                else
                    Toast.makeText(MainActivity.this, "write the file name and content", Toast.LENGTH_SHORT).show();
            }
        });

        mOpenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mFileNameET.getText()!=null){
                    mFileName = mFileNameET.getText().toString();
                    mTextBox.setText(openFile(mFileName));
                }
                else
                    Toast.makeText(getApplicationContext(), "write file name, or file not exist", Toast.LENGTH_SHORT).show();
            }
        });

        mShowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allFiles=showAllfiles();
                if(allFiles.length>0){
                    mTextBox.setText(Arrays.toString(allFiles));
                }
                else
                    Toast.makeText(getApplicationContext(), "there are no files", Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void saveFile(String fName, String fContent){
        try(FileOutputStream file = this.openFileOutput(fName,Context.MODE_PRIVATE)) {
            file.write(fContent.getBytes());
            Log.d(TAG, "saveFile: ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String openFile(String fileName){
        String content="";
        try {
            FileInputStream file = this.openFileInput(fileName);
            InputStreamReader inputStreamReader =
                    new InputStreamReader(file, StandardCharsets.UTF_8);
            StringBuilder builder = new StringBuilder();
            try(BufferedReader reader=new BufferedReader(inputStreamReader)) {
                String line = reader.readLine();
                while(line!= null){
                    builder.append(line).append('\n');
                    line = reader.readLine();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                content=builder.toString();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return content ;
    }


    public String[] showAllfiles(){
        String[] files =MainActivity.this.fileList();
        return files;
    }
}