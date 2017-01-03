package id.sch.smktelkom_mlg.learn.recyclerview3;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import adapter.HotelAdapter;
import model.Hotel;

public class MainActivity extends AppCompatActivity implements HotelAdapter.IHotelAdapter {

    public static final String HOTEL = "hotel";
    private static final int REQUEST_CODE_ADD = 88;
    private static final int REQUEST_CODE_EDIT = 99;
    ArrayList<Hotel> mList = new ArrayList();
    HotelAdapter mAdapter;
    int itemPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new HotelAdapter(this,mList);
        recyclerView.setAdapter(mAdapter);

        fillData();
    }

    private void fillData() {
        Resources resources = getResources();
        String[] arJudul = resources.getStringArray(R.array.places);
        String[] arDeskripsi = resources.getStringArray(R.array.place_desc);
        String[] arDetail = resources.getStringArray(R.array.place_details);
        String[] arLokasi = resources.getStringArray(R.array.place_locations);
        TypedArray a = resources.obtainTypedArray(R.array.places_picture);
        String[] arFoto = new String[a.length()];
        for (int i = 0; i < arFoto.length; i++) {

            int id = a.getResourceId(i,0);
            arFoto[i] = ContentResolver.SCHEME_ANDROID_RESOURCE+"://"
                    +resources.getResourcePackageName(id)+'/'
                    +resources.getResourceTypeName(id)+'/'
                    +resources.getResourceEntryName(id);
        }
        a.recycle();
        for (int i = 0; i < arJudul.length; i++) {
            mList.add(new Hotel(arJudul[i], arDeskripsi[i], arFoto[i],arDetail[i],
                    arLokasi[i]));
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void doClick(int pos) {
        Intent intent = new Intent(this,DetailActivity.class);
        intent.putExtra(HOTEL, mList.get(pos));
        startActivity(intent);
    }
    
    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

    public void setFab(FloatingActionButton fab) {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) 
            {
             goAdd();   
            }
        });
    }

    private void goAdd() 
    {
        startActivityForResult(new Intent(this,InputActivity.class), REQUEST_CODE_ADD);
    }
    
    @Override
    public void doEdit(int pos)
    {
        itemPos = pos;
        Intent intent = new Intent(this, InputActivity.class);
        intent.putExtra(HOTEL, mList.get(pos));
        startActivityForResult(intent, REQUEST_CODE_EDIT);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD && resultCode == RESULT_OK)
        {
            Hotel hotel = (Hotel) data.getSerializableExtra(HOTEL);
            mList.add(hotel);
            mAdapter.notifyDataSetChanged();
        }
        else if (requestCode == REQUEST_CODE_EDIT  && resultCode == RESULT_OK)
        {
            Hotel hotel = (Hotel) data.getSerializableExtra(HOTEL);
            mList.remove(itemPos);
            mList.add(itemPos,hotel);
            mAdapter.notifyDataSetChanged();
        }
    }
    @Override
    public void doDelete(int pos)
    {
        itemPos = pos;
        final Hotel hotel = mList.get(pos);
        mList.remove(itemPos);
        mAdapter.notifyDataSetChanged();

        Snackbar.make(findViewById(R.id.fab),hotel.judul+" Terhapus",Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mList.add(itemPos,hotel);
                        mAdapter.notifyDataSetChanged();
                    }
                }).show();
    }


}