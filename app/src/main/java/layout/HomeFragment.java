package layout;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jin.communitymanagement.Activity_InfoActivity;
import com.example.jin.communitymanagement.AssociationActivity;
import com.example.jin.communitymanagement.AssociationActivityAdapter;
import com.example.jin.communitymanagement.EditAssociationActivityActivity;
import com.example.jin.communitymanagement.HeaderAdapter;
import com.example.jin.communitymanagement.HomeFlag;
import com.example.jin.communitymanagement.HomeFlagAdapter;
import com.example.jin.communitymanagement.MainActivity;
import com.example.jin.communitymanagement.MainViewPagerAdapter;
import com.example.jin.communitymanagement.R;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import info.hoang8f.android.segmented.SegmentedGroup;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;


public class HomeFragment extends Fragment {


    private static View view;
    public  static final String GET_THE_VOICE="get the voice";

    private VoiceReceiver voiceReceiver;

    private MaterialSearchView searchView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<AssociationActivity> associationActivityList=new ArrayList<>();

    private AssociationActivityAdapter association_ac_adapter;
//    private AssociationActivity[] associationActivities={new AssociationActivity("热舞社ABC","hiphop","下午两点",R.drawable.letsdance),new AssociationActivity("热舞社","hiphop","下午两点",R.drawable.letsdance),new AssociationActivity("热舞社","hiphop","下午两点",R.drawable.letsdance),new AssociationActivity("热舞社","hiphop","下午两点",R.drawable.letsdance)};
    private   RecyclerView homeRecyclerView;

    //属性都在这里
    private boolean AcOrAs=true;
     private    HomeFlagAdapter adapterFlag;

    private List<HomeFlag> headerList=new ArrayList<>();

    //广播看这里
    private RefreshReceiver refreshReceiver;


    //控件在这里
    private FloatingActionButton fab_function;
    private FloatingActionButton fabAdd;
    private FloatingActionButton fabFilter;
    private TextView textViewAdd;
    private TextView textViewFilter;
    private TextView backPaper;
    private boolean fabIsOpen=false;
    //动画在这里呀
    private AnimatorSet animatorSetClose;
    private boolean animatorCloseStart=false;

    //重载函数
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

       view= inflater.inflate(R.layout.fragment_home, container, false);


        initHomeFragment();
        initSearchView();
        initFabFunciton(view);
        return view;
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

                final List<AssociationActivity> filteredModelList=filter(associationActivityList,newText);
                association_ac_adapter.setFilter(filteredModelList);
                association_ac_adapter.animateTo(filteredModelList);
                homeRecyclerView.scrollToPosition(0);
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
                association_ac_adapter.setFilter(associationActivityList);
            }


        });
        searchView.setVoiceSearch(false); //or false

    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("com.example.jin.communitymanagement.GET_THE_VOICE");
        voiceReceiver=new VoiceReceiver();
        getActivity().registerReceiver(voiceReceiver,intentFilter);

        IntentFilter intentFilterRefresh=new IntentFilter();
        intentFilterRefresh.addAction("com.example.jin.communitymanagement.RefreshReceiver");
        refreshReceiver=new RefreshReceiver();
        getActivity().registerReceiver(refreshReceiver,intentFilterRefresh);




    }

    @Override
    public void onPause() {
        super.onPause();
        if(voiceReceiver!=null)
        {
            getActivity().unregisterReceiver(voiceReceiver);
            voiceReceiver=null;
        }
        if(refreshReceiver!=null)
        {
            getActivity().unregisterReceiver(refreshReceiver);;
            refreshReceiver=null;
        }
    }

    private void initHomeFragment() {
        initRecyclerView();
        initSwipeRefresh();


    }
    private void initSwipeRefresh()
    {


        swipeRefreshLayout=(SwipeRefreshLayout) view.findViewById(R.id.home_swipe_refresh);
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
                        initAssociationActivity();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }

        }).start();
    }
    private void initRecyclerView()
    {
        initAssociationActivity();


            homeRecyclerView=(RecyclerView) view.findViewById(R.id.home_recycler_view) ;


        GridLayoutManager layoutManager=new GridLayoutManager(getContext(),1);

        homeRecyclerView.setLayoutManager(layoutManager);
        association_ac_adapter =new AssociationActivityAdapter(associationActivityList);

        homeRecyclerView.setAdapter(association_ac_adapter);
        HeaderAdapter headerAdapter=new HeaderAdapter(association_ac_adapter);
        LayoutInflater inflater_header=LayoutInflater.from(getContext());
        View view=inflater_header.inflate(R.layout.home_header_cardview,null);
        initRecycHeader(view);
        headerAdapter.addHeaderView(view);
        homeRecyclerView.setAdapter(headerAdapter);

    }

    private void initRecycHeader(View view) {
        initHomeFlagList(view);


        SegmentedGroup segmented= (SegmentedGroup)view.findViewById(R.id.group_home_segmented);
       segmented.setTintColor(Color.parseColor("#FFcc00"));
        RadioButton radioActivity= (RadioButton)view.findViewById(R.id.btn_home_activity);
        radioActivity.setChecked(true);
        segmented.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId)
                {
                    case R.id.btn_home_activity:
                        Toast.makeText(getActivity(), "你选择了活动", Toast.LENGTH_SHORT).show();
                        AcOrAs=true;
                        break;
                    case R.id.btn_home_association:
                        Toast.makeText(getActivity(), "你选择了社团", Toast.LENGTH_SHORT).show();
                        AcOrAs=false;
                        break;
                }
                Intent intentRefresh=new Intent("com.example.jin.communitymanagement.RefreshReceiver");
                getActivity().sendBroadcast(intentRefresh);
            }
        });

    }

    private void initHomeFlagList(View view) {
        initActivityFlagList();
        initAssociationFlagList();
        RecyclerView recyclerViewFlag=(RecyclerView)view.findViewById(R.id.recycler_header_home);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewFlag.setLayoutManager(layoutManager);
        headerList.clear();
        headerList.addAll(activityFlagList);
       adapterFlag =new HomeFlagAdapter(headerList);
        recyclerViewFlag.setAdapter(adapterFlag);
    }

    private List<HomeFlag> associationFlagList=new ArrayList<>();
    private void initAssociationFlagList() {

        HomeFlag dance=new HomeFlag(false,"舞蹈");
        associationFlagList.add(dance);
        HomeFlag opera=new HomeFlag(false,"话剧");
        associationFlagList.add(opera);
        HomeFlag song=new HomeFlag(false,"音乐");
        associationFlagList.add(song);
        HomeFlag literal=new HomeFlag(false,"文艺");
        associationFlagList.add(literal);
        HomeFlag outdoor=new HomeFlag(false,"户外");
        associationFlagList.add(outdoor);
        HomeFlag little=new HomeFlag(false,"小众");
        associationFlagList.add(little);
        HomeFlag technology=new HomeFlag(false,"科技");
        associationFlagList.add(technology);
        HomeFlag practice=new HomeFlag(false,"实践");
        associationFlagList.add(practice);
        HomeFlag literature=new HomeFlag(false,"学术");
        associationFlagList.add(literature);

    }

    private List<HomeFlag> activityFlagList=new ArrayList<>();
    private void initActivityFlagList() {
        HomeFlag literal=new HomeFlag(false,"文艺");
        activityFlagList.add(literal);
        HomeFlag quiet=new HomeFlag(false,"恬静");
        activityFlagList.add(quiet);
        HomeFlag crazy=new HomeFlag(false,"疯狂");
        activityFlagList.add(crazy);
        HomeFlag sexy=new HomeFlag(false,"骚气");
        activityFlagList.add(sexy);
        HomeFlag ghost=new HomeFlag(false,"诡异");
        activityFlagList.add(ghost);
        HomeFlag technology=new HomeFlag(false,"科技");
        activityFlagList.add(technology);
        HomeFlag practice=new HomeFlag(false,"实践");
        activityFlagList.add(practice);

    }

    private void initAssociationActivity()
    {
        associationActivityList.clear();
        SQLiteDatabase db=((MainActivity)getActivity()).getDbHelper().getWritableDatabase();
        if(db.isOpen()) {
            Cursor cursor2 = db.query( "ActivityTable", null, null, null, null, null, null );
            if (cursor2.moveToFirst()) {

                do {
                    String activity_name = cursor2.getString( cursor2.getColumnIndex( "activity_name" ) );
                    String association = cursor2.getString( cursor2.getColumnIndex( "association" ) );
                    String introduction = cursor2.getString( cursor2.getColumnIndex( "introduction" ) );
                    String time_start = cursor2.getString( cursor2.getColumnIndex( "time_start" ) );
                    String time_end = cursor2.getString( cursor2.getColumnIndex( "time_end" ) );
                    byte[] in = cursor2.getBlob(cursor2.getColumnIndex("image"));
                    Bitmap bitmap=getBmp(in);
                    int inNeedMoney=cursor2.getInt(  cursor2.getColumnIndex( "inNeedMoney" ) );
                   AssociationActivity associationActivity=new AssociationActivity(association,activity_name,time_start,time_end,
                           bitmap,
                           introduction,inNeedMoney);
                    associationActivityList.add( associationActivity );
                    Log.d( TAG, "设备添加成功" );
                } while (cursor2.moveToNext());
            }
            cursor2.close();
        }
        if(association_ac_adapter!=null)
        association_ac_adapter.notifyItemRangeChanged(0,associationActivityList.size());

    }
    public Bitmap getBmp(byte[] in)
    {


        Bitmap bmpout = BitmapFactory.decodeByteArray(in, 0, in.length);
        return bmpout;
    }

    private List<AssociationActivity> filter(List<AssociationActivity> associationActivities, String query) {
        query = query.toLowerCase();

        final List<AssociationActivity> filteredModelList = new ArrayList<>();
        for (AssociationActivity associationAc : associationActivities) {

            final String nameEn = associationAc.getActivityName().toLowerCase();
            final String AcEn = associationAc.getAssociationName().toLowerCase();
            final String name = associationAc.getActivityName();
            final String Ac = associationAc.getAssociationName();

            if (name.contains(query) || Ac.contains(query) || nameEn.contains(query) || AcEn.contains(query)) {

                filteredModelList.add(associationAc);
            }
        }
        return filteredModelList;
    }

    private void initFabFunciton(View view)
    {
         fabAdd=(FloatingActionButton)view.findViewById(R.id.fab_home_add_activity);
        initFabAdd(fabAdd);
          fabFilter=(FloatingActionButton)view.findViewById(R.id.fab_home_filter);
        textViewAdd=(TextView)view.findViewById(R.id.text_home_add);
        textViewFilter=(TextView)view.findViewById(R.id.text_home_filer);
        textViewAdd.setVisibility(View.GONE);
        textViewFilter.setVisibility(View.GONE);
        fabAdd.setVisibility(View.GONE);
        fabFilter.setVisibility(View.GONE);
      backPaper =(TextView)view.findViewById(R.id.text_home_backPaper);
        backPaper.setVisibility(View.GONE);
        fab_function=(FloatingActionButton)view.findViewById(R.id.fab_home_function);
        fab_function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!fabIsOpen)
                {
                    fabIsOpen=true;
                    animateABCOpen(v,fabAdd,fabFilter,textViewAdd,textViewFilter,backPaper);
                }else
                {
                    fabIsOpen=false;
                    animateABCClose(v,fabAdd,fabFilter,textViewAdd,textViewFilter,backPaper);
                }
            }
        });


    }

    private void initFabAdd(FloatingActionButton fabAdd) {
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), EditAssociationActivityActivity.class);
                startActivity(intent);
                animateABCClose(fab_function,v,fabFilter,textViewAdd,textViewFilter,backPaper);
                fabIsOpen=false;

            }
        });
    }

    class VoiceReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent) {
          String searchWrd=  intent.getStringExtra(GET_THE_VOICE);
            searchView.setQuery(searchWrd,false);

        }
    }
    class  RefreshReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            getActivity().runOnUiThread( new Runnable() {
                @Override
                public void run() {
                    headerList.clear();
                    if(AcOrAs)
                    {
                        headerList.addAll(activityFlagList);

                    }else
                    {
                        headerList.addAll(associationFlagList);
                    }
                    adapterFlag.notifyItemRangeChanged(0,headerList.size());

                   initAssociationActivity();
                }
            } );
        }
    }
    private void animateABCOpen(View fabHead,View fabB,View fabC,View textB,View textC,View paper)
    {
        fabB.setVisibility(View.VISIBLE);
        fabC.setVisibility(View.VISIBLE);

        paper.setVisibility(View.VISIBLE);
        textB.setVisibility(View.VISIBLE);
        textC.setVisibility(View.VISIBLE);
        ObjectAnimator animatorHead= ObjectAnimator.ofFloat(fabHead,"rotationX",0,270,0);
        ObjectAnimator animatorB1=ObjectAnimator.ofFloat(fabB,"alpha",0,1);
        ObjectAnimator animatorBt=ObjectAnimator.ofFloat(textB,"alpha",0,1);
        ObjectAnimator animatorP=ObjectAnimator.ofFloat(paper,"alpha",0,0.7f);
        ObjectAnimator animatorB2=ObjectAnimator.ofFloat(fabB,"rotationY",0,270,0);
        ObjectAnimator animatorC1=ObjectAnimator.ofFloat(fabC,"alpha",0,1);
        ObjectAnimator animatorCt=ObjectAnimator.ofFloat(textC,"alpha",0,1);
        ObjectAnimator animatorC2=ObjectAnimator.ofFloat(fabC,"rotationY",0,270,0);

        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.playTogether(animatorHead,animatorB1,animatorB2,animatorC1,animatorC2,animatorBt,animatorCt,animatorP);
        animatorSet.setDuration(800);
        animatorSet.start();

    }
    private void animateABCClose(View fabHead,View fabB,View fabC,View textB,View textC,View paper)
    {
        ObjectAnimator animatorHead= ObjectAnimator.ofFloat(fabHead,"rotationX",0,270,0);
        ObjectAnimator animatorB1=ObjectAnimator.ofFloat(fabB,"alpha",1,0);
        ObjectAnimator animatorB2=ObjectAnimator.ofFloat(fabB,"rotationY",0,270,0);
        ObjectAnimator animatorC1=ObjectAnimator.ofFloat(fabC,"alpha",1,0);
        ObjectAnimator animatorC2=ObjectAnimator.ofFloat(fabC,"rotationY",0,270,0);
        ObjectAnimator animatorP=ObjectAnimator.ofFloat(paper,"alpha",0.7f,0);
        ObjectAnimator animatorCt=ObjectAnimator.ofFloat(textC,"alpha",1,0);
        ObjectAnimator animatorBt=ObjectAnimator.ofFloat(textB,"alpha",1,0);
       animatorSetClose=new AnimatorSet();
        animatorSetClose.playTogether(animatorHead,animatorB1,animatorB2,animatorC1,animatorC2,animatorBt,animatorCt,animatorP);
        animatorSetClose.setDuration(800);
        animatorSetClose.start();
        animatorBt.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                if(fabAdd!=null)
                    fabAdd.setVisibility(View.GONE);
                if(fabFilter!=null)
                    fabFilter.setVisibility(View.GONE);
                if(textViewAdd!=null)
                    textViewAdd.setVisibility(View.GONE);
                if(textViewFilter!=null)
                    textViewFilter.setVisibility(View.GONE);
                if(backPaper!=null)
                    backPaper.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });


    }


}
