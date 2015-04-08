package com.asmith.decidrr;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Toast;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.os.Bundle;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    //@Override
    //public void onChosenDir(String chosenDir) {
      //  m_chosen = chosenDir;
       // Toast.makeText(MainActivity.this, "Chosen FileOpenDialog File: " + m_chosen, Toast.LENGTH_LONG).show();

    //}//

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_main, container, false);



            //Button1
            Button dirChooserButton1 = (Button) rootView.findViewById(R.id.button1);
            dirChooserButton1.setOnClickListener(new OnClickListener(){
                String m_chosen;
                @Override
                public void onClick(View v) {
                    /////////////////////////////////////////////////////////////////////////////////////////////////
                    //Create FileOpenDialog and register a callback
                    /////////////////////////////////////////////////////////////////////////////////////////////////
                    final SimpleFileDialog FileOpenDialogTrain =  new SimpleFileDialog(getActivity(), "FileOpen",
                            new SimpleFileDialog.SimpleFileDialogListener()
                            {
                                @Override
                                public void onChosenDir(String chosenDir)
                                {
                                    // The code in this function will be executed when the dialog OK button is pushed
                                    m_chosen = chosenDir;
                                    Toast.makeText(getActivity(), "Chosen FileOpenDialog File: " +
                                            m_chosen, Toast.LENGTH_LONG).show();
                                }
                            });

                    //You can change the default filename using the public variable "Default_File_Name"
                    FileOpenDialogTrain.default_file_name = "";
                    FileOpenDialogTrain.chooseFile_or_Dir();

                    /////////////////////////////////////////////////////////////////////////////////////////////////

                }
            });

            //Button2
            Button dirChooserButton2 = (Button) rootView.findViewById(R.id.button2);
            dirChooserButton2.setOnClickListener(new OnClickListener()
            {
                String m_chosen;
                @Override
                public void onClick(View v) {
                    /////////////////////////////////////////////////////////////////////////////////////////////////
                    //Create FileSaveDialog and register a callback
                    /////////////////////////////////////////////////////////////////////////////////////////////////
                    SimpleFileDialog FileOpenDialogTest =  new SimpleFileDialog(getActivity(), "FileOpen",
                            new SimpleFileDialog.SimpleFileDialogListener()
                            {
                                @Override
                                public void onChosenDir(String chosenDir)
                                {
                                    // The code in this function will be executed when the dialog OK button is pushed
                                    m_chosen = chosenDir;
                                    Toast.makeText(getActivity(), "Chosen FileOpenDialog File: " +
                                            m_chosen, Toast.LENGTH_LONG).show();
                                }
                            });

                    //You can change the default filename using the public variable "Default_File_Name"
                    FileOpenDialogTest.default_file_name = "my_default.txt";
                    FileOpenDialogTest.chooseFile_or_Dir();

                    /////////////////////////////////////////////////////////////////////////////////////////////////

                }
            });

            //Button3
            //Button dirChooserButton3 = (Button) findViewById(R.id.button3);
            //dirChooserButton3.setOnClickListener(new OnClickListener()
            //{
            //String m_chosen;
            //  @Override
            //public void onClick(View v) {
            //redirect to new activity
            //run the tree
            //}
            //});


            return rootView;
        }
    }
}
