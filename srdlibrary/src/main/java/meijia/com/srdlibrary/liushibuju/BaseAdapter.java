package meijia.com.srdlibrary.liushibuju;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseAdapter {

    // 1.有多少个条目
    public abstract int getCount();

    // 2.getView通过position
    public abstract View getView(int position,ViewGroup parent);


    // 3.观察者模式及时通知更新
    public void unregisterDataSetObserver(DataSetObserver observer){

    }

    public void registerDataSetObserver(DataSetObserver observer){

    }
}
