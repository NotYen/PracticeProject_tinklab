package notyen.parkproject.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

import notyen.parkproject.ParkApplication;
import notyen.parkproject.adapter.MyFragmentAdapter;
import notyen.parkproject.fragment.ContentFragment;
import notyen.parkproject.fragment.TestAFragment;
import notyen.parkproject.fragment.TestBFragment;
import notyen.practiceproject.R;

public class MainActivity extends FragmentActivity {

    private static final String TAG = ParkApplication.class.getSimpleName();
    private TabLayout mTablayout;
    private ViewPager mViewPager;
    private List<Fragment> mFragmentlist = new ArrayList<Fragment>();
    public FragmentManager mFragmentManager;
    private ContentFragment mContentFragment;
    private TestAFragment mTestAFragment;
    private TestBFragment mTestBFragment;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        mFragmentManager = getSupportFragmentManager();
        initView();
    }

    private void initView() {
        mTablayout = (TabLayout) findViewById(R.id.tabs);
        mTablayout.addTab(mTablayout.newTab().setText("Page one"));
        mTablayout.addTab(mTablayout.newTab().setText("Page two"));
        mTablayout.addTab(mTablayout.newTab().setText("Page three"));
        mViewPager = (ViewPager) findViewById(R.id.pager);

        initFragment();
        initListener();
    }

    private void initFragment() {
        mContentFragment = new ContentFragment();
        mTestAFragment = new TestAFragment();
        mTestBFragment = new TestBFragment();

        mFragmentlist.add(mContentFragment);
        mFragmentlist.add(mTestAFragment);
        mFragmentlist.add(mTestBFragment);
        mViewPager.setAdapter(new MyFragmentAdapter(mFragmentManager, mFragmentlist));

    }

    private void initListener() {
        mTablayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTablayout));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
