package bk.vinhdo.taxiads.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import bk.vinhdo.taxiads.fragments.ItemAddressFragment;
import bk.vinhdo.taxiads.models.Address;

/**
 * Created by Vinh on 1/29/15.
 */
public class ItemAddressListViewAdapter extends FragmentStatePagerAdapter {

    private Context context;
    private ArrayList<Address> data;

    public ItemAddressListViewAdapter(FragmentManager fm,Context context, ArrayList<Address> data){
        super(fm);
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Fragment getItem(int position) {
        return ItemAddressFragment.getInstance(data.get(position));
    }

    @Override
    public int getItemPosition(Object object) {
        return data.indexOf(object);
    }
}
