package bk.vinhdo.taxiads.fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import bk.vinhdo.taxiads.MapsActivity;
import bk.vinhdo.taxiads.R;
import bk.vinhdo.taxiads.activitis.SlideMenuActivity;
import bk.vinhdo.taxiads.utils.view.CustomTextView;
import bk.vinhdo.taxiads.utils.view.SAutoBgImageButton;

/**
 * Created by Vinh on 2/4/15.
 */
public class MainFragment extends Fragment implements View.OnClickListener {

    // view header
    private View mNavigateView;
    private CustomTextView mLeftText;
    private CustomTextView mRightText;
    private CustomTextView mTitleText;
    private SAutoBgImageButton mLeftImage;
    private SAutoBgImageButton mRightImage;
    private RelativeLayout mLeftBarLayout;
    private RelativeLayout mRightBarLayout;
    private ImageView mTitleImage;

    //Content
    SAutoBgImageButton addr_nearby, addr_shop, addr_restaurant, addr_cafe, addr_repair, addr_heath;
    private LinearLayout mLayoutWeather;
//    public LinearLayout mLLBG;

    public interface MainClickListener {
        public void MenuClick();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, null, false);
        initHeaderView(view);
        return view;
    }

    protected void initHeaderView(View view) {
        mNavigateView = view.findViewById(R.id.navigate_bar);
//        mLLBG = (LinearLayout)view.findViewById(R.id.bg_ll);
//        Bitmap mBitmapBg = BitmapFactory.decodeResource(getActivity().getResources(),R.drawable.hn_bg);
//        BitmapDrawable drawableBg = new BitmapDrawable(BitmapUtil.fastblur(getActivity(),mBitmapBg,16));
//        mLLBG.setBackgroundDrawable(drawableBg);
        final Drawable cd = getResources().getDrawable(R.drawable.ab_background_light);
        mNavigateView.setBackgroundDrawable(cd);
        cd.setAlpha(0);
        mLeftText = (CustomTextView) view.findViewById(R.id.left_text);
        mLeftText.setVisibility(View.INVISIBLE);

        mLeftImage = (SAutoBgImageButton) view.findViewById(R.id.left_image);
        mLeftImage.setVisibility(View.VISIBLE);
        mLeftImage.setOnClickListener(this);
        mLeftImage.setBackgroundResource(R.drawable.pink_ic_navigation_drawer);
        mRightText = (CustomTextView) view.findViewById(R.id.right_text);
        mRightText.setVisibility(View.INVISIBLE);

        mRightImage = (SAutoBgImageButton) view.findViewById(R.id.right_image);
        mRightImage.setVisibility(View.VISIBLE);
        mRightImage.setBackgroundResource(R.drawable.topic_map_ic);
        mRightImage.setOnClickListener(this);

        mTitleText = (CustomTextView) view.findViewById(R.id.title_text);
        mTitleText.setOnClickListener(this);
        mTitleText.setTextSize(getActivity().getResources().getDimension(R.dimen.text_size_very_small));
        mTitleText.setText("Search e.g Cafe..");
        mTitleText.setVisibility(View.VISIBLE);
        mTitleText.setBackgroundResource(R.drawable.bg_search_selector);

        mTitleImage = (ImageView) view.findViewById(R.id.logo_image);
        mTitleImage.setOnClickListener(this);
        mTitleImage.setVisibility(View.GONE);

        mLeftBarLayout = (RelativeLayout) view.findViewById(R.id.left_layout);

        mRightBarLayout = (RelativeLayout) view.findViewById(R.id.right_layout);
        mLayoutWeather = (LinearLayout) view.findViewById(R.id.weather_main);
        mLayoutWeather.setOnClickListener(this);
        addr_nearby = (SAutoBgImageButton) view.findViewById(R.id.main_addr_nearby);
        addr_nearby.setOnClickListener(this);
        addr_shop = (SAutoBgImageButton) view.findViewById(R.id.main_addr_shop);
        addr_shop.setOnClickListener(this);
        addr_restaurant = (SAutoBgImageButton) view.findViewById(R.id.main_addr_restaurant);
        addr_restaurant.setOnClickListener(this);
        addr_cafe = (SAutoBgImageButton) view.findViewById(R.id.main_addr_cafe);
        addr_cafe.setOnClickListener(this);
        addr_repair = (SAutoBgImageButton) view.findViewById(R.id.main_addr_repair);
        addr_repair.setOnClickListener(this);
        addr_heath = (SAutoBgImageButton) view.findViewById(R.id.main_addr_heath);
        addr_heath.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_image:
                ((SlideMenuActivity) getActivity()).openMenu();
                break;
            case R.id.right_image:
                Intent i = new Intent(getActivity(), MapsActivity.class);
                startActivity(i);
                break;
            case R.id.title_text:
                /**
                * Click Search Add action here
                * */
                break;
            case R.id.weather_main:
//                break;
            case R.id.main_addr_nearby:
//                break;
            case R.id.main_addr_shop:
//                break;
            case R.id.main_addr_restaurant:
//                break;
            case R.id.main_addr_cafe:
//                break;
            case R.id.main_addr_repair:
//                break;
            case R.id.main_addr_heath:
                Intent ii= new Intent(getActivity(), MapsActivity.class);
                startActivity(ii);
                break;
        }
    }
}
