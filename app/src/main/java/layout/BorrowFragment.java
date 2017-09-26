package layout;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.jin.communitymanagement.Activity_InfoActivity;
import com.example.jin.communitymanagement.AssociationActivity;
import com.example.jin.communitymanagement.AssociationActivityAdapter;
import com.example.jin.communitymanagement.BaseActivity;
import com.example.jin.communitymanagement.BorrowAdapter;
import com.example.jin.communitymanagement.BorrowEditActivity;
import com.example.jin.communitymanagement.BorrowItem;
import com.example.jin.communitymanagement.Borrow_Info_Activity;
import com.example.jin.communitymanagement.HeaderAdapter;
import com.example.jin.communitymanagement.MainActivity;
import com.example.jin.communitymanagement.R;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;


public class BorrowFragment extends Fragment {
    private FloatingActionButton fab_add;
    private  View view;
    private MaterialSearchView searchView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<BorrowItem> BorrowList=new ArrayList<>();

    private BorrowAdapter borrow_adapter;
    //    private AssociationActivity[] associationActivities={new AssociationActivity("热舞社ABC","hiphop","下午两点",R.drawable.letsdance),new AssociationActivity("热舞社","hiphop","下午两点",R.drawable.letsdance),new AssociationActivity("热舞社","hiphop","下午两点",R.drawable.letsdance),new AssociationActivity("热舞社","hiphop","下午两点",R.drawable.letsdance)};
    private RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_borrow, container, false);
        initFab();
        initBorrowFragment();
        initSearchView();

        return  view;
    }

    private void initSearchView() {
        searchView=(MaterialSearchView)getActivity().findViewById(R.id.search_view);
        searchView.setEllipsize(true);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                final List<BorrowItem> filteredModelList=filter(BorrowList,newText);
                borrow_adapter.setFilter(filteredModelList);
                borrow_adapter.animateTo(filteredModelList);
                recyclerView.scrollToPosition(0);
                return true;

            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                borrow_adapter.setFilter(BorrowList);
            }


        });
        searchView.setVoiceSearch(false); //or false

    }
    private List<BorrowItem> filter(List<BorrowItem> borrowItems, String query) {
        query = query.toLowerCase();

        final List<BorrowItem> filteredModelList = new ArrayList<>();
        for (BorrowItem borrowItem : borrowItems) {

            final String nameEn = borrowItem.getBorrowName().toLowerCase();
            final String AcEn = borrowItem.getStart_time().toLowerCase();
            final String name = borrowItem.getEnd_time();
            final String Ac = borrowItem.getIntroduction();

            if (name.contains(query) || Ac.contains(query) || nameEn.contains(query) || AcEn.contains(query)) {

                filteredModelList.add(borrowItem);
            }
        }
        return filteredModelList;
    }


    private void initFab() {
        fab_add=(FloatingActionButton) view.findViewById(R.id.fab_borrow_function);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), BorrowEditActivity.class);
                getContext().startActivity(intent);
            }
        });
    }
    private void initBorrowFragment() {
        initRecyclerView();
        initSwipeRefresh();


    }
    private void initSwipeRefresh()
    {


        swipeRefreshLayout=(SwipeRefreshLayout) view.findViewById(R.id.borrow_swipe_refresh);
        if(swipeRefreshLayout==null)
        {
            Log.d(TAG, "initSwipeRefresh: swipeRe出问题啦");
        }
        swipeRefreshLayout.setColorSchemeResources(R.color.colorText);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshDisplay();
            }
        });


    }

    private void refreshDisplay()
    {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(10);
                }catch (InterruptedException e)
                {
                    e.printStackTrace();
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initBorrowList();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }

        }).start();
    }
    private void initRecyclerView()
    {
        initBorrowList();
        recyclerView=(RecyclerView) view.findViewById(R.id.borrow_recycler_view);
        GridLayoutManager layoutManager=new GridLayoutManager(getContext(),1);

       recyclerView.setLayoutManager(layoutManager);
        borrow_adapter =new BorrowAdapter(BorrowList);
       borrow_adapter.setItemOnClickListener(new BorrowAdapter.onMyItClickListener() {
           @Override
           public void onItemClick(View v, int position) {

              BorrowItem borrowItem=BorrowList.get(position);
               Intent intent=new Intent(getContext(),Borrow_Info_Activity.class);
               intent.putExtra(Borrow_Info_Activity.INTRODUCTION,borrowItem.getIntroduction());
               intent.putExtra(Borrow_Info_Activity.START_TIME,borrowItem.getStart_time());
               intent.putExtra(Borrow_Info_Activity.END_TIME,borrowItem.getEnd_time());
               intent.putExtra(Borrow_Info_Activity.BR_NAME, borrowItem.getBorrowName());
               intent.putExtra(Borrow_Info_Activity.CONNECTION,borrowItem.getConnection());
               Bitmap newBit=  BaseActivity.zoomImg(borrowItem.getBitmap(),400,400);
               byte[] bytes=get_bit_image(newBit);

               intent.putExtra(Borrow_Info_Activity.IMAGE,bytes);
               startActivity(intent);
           }
       });




       recyclerView.setAdapter(borrow_adapter);

    }
    public byte[] get_bit_image(Bitmap bitmap)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
    private void initBorrowList()
    {
        BorrowList.clear();
        SQLiteDatabase db=((MainActivity)getActivity()).getDbHelper().getWritableDatabase();
        if(db.isOpen()) {
            Cursor cursor2 = db.query( "BorrowTable", null, null, null, null, null, null );
            if (cursor2.moveToFirst()) {

                do {
                    String time_start = cursor2.getString( cursor2.getColumnIndex( "time_start" ) );
                    String time_end= cursor2.getString( cursor2.getColumnIndex( "time_end" ) );
                    String borrow_name = cursor2.getString( cursor2.getColumnIndex( "borrow_name" ) );
                    String connection = cursor2.getString( cursor2.getColumnIndex( "connection" ) );
                    String introduction = cursor2.getString( cursor2.getColumnIndex( "introduction" ) );
                    byte[] in = cursor2.getBlob(cursor2.getColumnIndex("image"));
                    Bitmap bitmap=getBmp(in);
                    BorrowItem borrowItem=new BorrowItem(time_start,time_end,introduction,bitmap,connection,borrow_name);
                    BorrowList.add( borrowItem);
                    Log.d( TAG, "添加到这里啦" );
                } while (cursor2.moveToNext());
            }
            cursor2.close();
        }
        if(borrow_adapter!=null)
            borrow_adapter.notifyItemRangeChanged(0,BorrowList.size());

    }
    public Bitmap getBmp(byte[] in)
    {


        Bitmap bmpout = BitmapFactory.decodeByteArray(in, 0, in.length);
        return bmpout;
    }
}
