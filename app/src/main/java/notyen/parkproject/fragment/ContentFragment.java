package notyen.parkproject.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import notyen.parkproject.ParkApplication;
import notyen.parkproject.adapter.ParkAdapter;
import notyen.parkproject.api.Api;
import notyen.parkproject.api.OnApiResponse;
import notyen.parkproject.apidata.OpenDataList;
import notyen.parkproject.utils.AppProgressDialog;
import notyen.parkproject.utils.Develop;
import notyen.practiceproject.R;

public class ContentFragment extends Fragment implements OnApiResponse<OpenDataList>, Response.ErrorListener {
    private static final String TAG = ParkApplication.class.getSimpleName();
    private Context mContext;
    private RecyclerView mParkRecycleView;
    private ParkAdapter mParkAdapter;
    private int lastVisibleItem, totalItemCount;
    private boolean isLoading;
    private AppProgressDialog mDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, null);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity().getApplicationContext();
        mDialog = new AppProgressDialog(getContext());
        initView();
    }

    private void initView() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mParkRecycleView = (RecyclerView) getActivity().findViewById(R.id.park_recyclerView);
        mParkRecycleView.setLayoutManager(layoutManager);
        mParkRecycleView.setHasFixedSize(true);
        mParkRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = layoutManager.getItemCount();
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();

                if (!isLoading && lastVisibleItem == (totalItemCount - 1)) {
                    //执行加载操作
                    isLoading = true;
                    loadApi();
                    Toast.makeText(mContext, "讀取更多！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadApi() {
        if(mDialog!=null) {
            mDialog.show("Loading....");
        }
        Api.getParkOpenData(this, this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
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

    @Override
    public void onApiResonse(OpenDataList data) {
        mDialog.dismiss();
        mParkAdapter = new ParkAdapter(mContext, data.getReturnData());
        mParkRecycleView.setAdapter(mParkAdapter);
        mParkAdapter.notifyDataSetChanged();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Develop.e(TAG + "_onErrorResponse", error.getMessage());
    }

    //不讓Viewpager預加載
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            loadApi();
        }
    }
}