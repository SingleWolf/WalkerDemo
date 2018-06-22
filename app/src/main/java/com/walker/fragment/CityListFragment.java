package com.walker.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bsx.baolib.view.CircleTextView;
import com.bsx.baolib.view.KeySlideView;
import com.github.promeg.pinyinhelper.Pinyin;
import com.walker.R;
import com.walker.WalkerApplication;
import com.walker.adapter.CityAdapter;
import com.walker.base.BaseFragment;
import com.walker.data.sqlite.WalkerDB;
import com.walker.entity.City;
import com.walker.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * summary :城市列表
 * time    :2016/8/15 14:21
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */
public class CityListFragment extends BaseFragment implements KeySlideView.onTouchListener, CityAdapter.onItemClickListener {
    private List<City> cityList = new ArrayList<>();
    private Set<String> firstPinYin = new LinkedHashSet<>();
    public static List<String> pinyinList = new ArrayList<>();
    private PinyinComparator pinyinComparator;

    private KeySlideView keySlideView;
    private CircleTextView circleTxt;


    private RecyclerView recyclerView;
    private TextView tvStickyHeaderView;
    private CityAdapter adapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        WalkerDB db = new WalkerDB(WalkerApplication.getContext());
        ArrayList<String> countyList = db.getAllCounty();
        for (int i = 0; i < countyList.size(); i++) {
            City city = new City();
            String county = countyList.get(i);
            city.setCityName(county);
            city.setCityPinyin(transformPinYin(county));
            cityList.add(city);
        }
    }

    @Override
    protected void buildView(View baseView, Bundle savedInst) {
        //cityList.clear();
        firstPinYin.clear();
        pinyinList.clear();

        keySlideView = (KeySlideView) baseView.findViewById(R.id.my_slide_view);
        circleTxt = (CircleTextView) baseView.findViewById(R.id.my_circle_view);
        pinyinComparator = new PinyinComparator();
        tvStickyHeaderView = (TextView) baseView.findViewById(R.id.tv_sticky_header_view);

        Collections.sort(cityList, pinyinComparator);
        for (City city : cityList) {
            firstPinYin.add(city.getCityPinyin().substring(0, 1));
        }
        for (String string : firstPinYin) {
            pinyinList.add(string);
        }

        keySlideView.setListener(this);
        keySlideView.setPinyingData(pinyinList);

        recyclerView = (RecyclerView) baseView.findViewById(R.id.rv_sticky_example);
        adapter = new CityAdapter(cityList);
        adapter.setListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


                View stickyInfoView = recyclerView.findChildViewUnder(
                        tvStickyHeaderView.getMeasuredWidth() / 2, 5);

                if (stickyInfoView != null && stickyInfoView.getContentDescription() != null) {
                    tvStickyHeaderView.setText(String.valueOf(stickyInfoView.getContentDescription()));
                }

                View transInfoView = recyclerView.findChildViewUnder(
                        tvStickyHeaderView.getMeasuredWidth() / 2, tvStickyHeaderView.getMeasuredHeight() + 1);

                if (transInfoView != null && transInfoView.getTag() != null) {
                    int transViewStatus = (int) transInfoView.getTag();
                    int dealtY = transInfoView.getTop() - tvStickyHeaderView.getMeasuredHeight();
                    if (transViewStatus == CityAdapter.HAS_STICKY_VIEW) {
                        if (transInfoView.getTop() > 0) {
                            tvStickyHeaderView.setTranslationY(dealtY);
                        } else {
                            tvStickyHeaderView.setTranslationY(0);
                        }
                    } else if (transViewStatus == CityAdapter.NONE_STICKY_VIEW) {
                        tvStickyHeaderView.setTranslationY(0);
                    }
                }


            }
        });

    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_city;
    }

    @Override
    protected void bindListener() {

    }

    @Override
    protected void unbindListener() {

    }

    public String transformPinYin(String character) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < character.length(); i++) {
            buffer.append(Pinyin.toPinyin(character.charAt(i)));
        }
        return buffer.toString();
    }

    @Override
    public void itemClick(int position) {
        ToastUtil.showCenterShort("你选择了:" + cityList.get(position).getCityName());
    }

    @Override
    public void showTextView(String textView, boolean dismiss) {
        if (dismiss) {
            circleTxt.setVisibility(View.GONE);
        } else {
            circleTxt.setVisibility(View.VISIBLE);
            circleTxt.setText(textView);
        }

        int selectPosition = 0;
        for (int i = 0; i < cityList.size(); i++) {
            if (cityList.get(i).getFirstPinYin().equals(textView)) {
                selectPosition = i;
                break;
            }
        }
        recyclerView.scrollToPosition(selectPosition);
    }

    public class PinyinComparator implements Comparator<City> {
        @Override
        public int compare(City cityFirst, City citySecond) {
            return cityFirst.getCityPinyin().compareTo(citySecond.getCityPinyin());
        }
    }
}
