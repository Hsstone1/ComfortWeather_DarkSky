//package com.example.sneek.outdoorindex;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.FragmentManager;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import data.DatabaseHelper;
//
//public class EditDataActivity extends AppCompatActivity {
//
//    private ListView mListView;
//    DatabaseHelper mDatabaseHelper;
//    private static final String TAG = "CREATION";
//    TextView cityTextName_textView;
//    Button   favoriteButton_button;
//    Button   deleteButton_button;
//    Button   exitButton_button;
//    Button   makeDefaultButton_button;
//
//    private String selectedName;
//    private int selectedID;
//    private String selectedLat;
//    private String selectedLon;
//
//
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.edit_data_activity_layout);
//
//        cityTextName_textView = (TextView) findViewById(R.id.cityTextName_textView);
//        favoriteButton_button = (Button)findViewById(R.id.favoriteButton_button);
//        deleteButton_button = (Button)findViewById(R.id.deleteButton_button);
//        exitButton_button = (Button)findViewById(R.id.exitButton_button);
//        makeDefaultButton_button = (Button)findViewById(R.id.makeDefaultButton_button);
//        mDatabaseHelper = new DatabaseHelper(this);
//
//        Intent recievedIntent = getIntent();
//
//        //revieces the extra values sent by list view item
//        selectedID = recievedIntent.getIntExtra("id", -1); //-1 is default value
//        selectedName = recievedIntent.getStringExtra("name");
//        //selectedLat = recievedIntent.getStringExtra("lat");
//        //selectedLon = recievedIntent.getStringExtra("lon");
//
//
//
//
//        cityTextName_textView.setText(selectedName + "\n(" + selectedLat + "," + selectedLon + ")");
//
//
//        makeDefaultButton_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                mDatabaseHelper.updateDefault(selectedName, selectedID, selectedName);
//                Toast.makeText(EditDataActivity.this, "Changed Default City", Toast.LENGTH_SHORT).show();
//                resetActivityScreen();
//
//
//            }
//        });
//
//        //NEEDS WORK
//        favoriteButton_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                mDatabaseHelper.updateFavorite(selectedName, selectedID, selectedName);
//                Toast.makeText(EditDataActivity.this, "Marked as Favorite", Toast.LENGTH_SHORT).show();
//                resetActivityScreen();
//
//
//            }
//        });
//
//        deleteButton_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mDatabaseHelper.deleteName(selectedID, selectedName);
//                cityTextName_textView.setText("");
//                Toast.makeText(EditDataActivity.this, "City Deleted", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(EditDataActivity.this, MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                startActivity(intent);
//
//            }
//        });
//        exitButton_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(EditDataActivity.this, "Exiting", Toast.LENGTH_SHORT).show();
//
//                resetActivityScreen();
//                final FragmentManager fm = EditDataActivity.this.getSupportFragmentManager();
//                fm.beginTransaction().show(MainActivity.homeFragment).commit();
//                MainActivity.active = MainActivity.homeFragment;
//            }
//        });
//
//    }
//    public void resetActivityScreen(){
//        Intent intent = new Intent(EditDataActivity.this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        startActivity(intent);
//    }
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
