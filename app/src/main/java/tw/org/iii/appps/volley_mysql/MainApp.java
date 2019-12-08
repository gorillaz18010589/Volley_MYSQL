package tw.org.iii.appps.volley_mysql;
//1.繼承Application
//2.要到檔案總館application加入 name:".MainApp" 告訴他這是主要的,以我自訂的為主
//Volley.newRequestQueue(Context context)://取得請求Queue物件(回傳值RequestQueue)
//RequestQueue.add(Request<T> request)://將回呼的資料結構新增上去(回傳值Request<T>)
import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MainApp extends Application {
    public RequestQueue queue;
    @Override
    public void onCreate() {
        super.onCreate();
        queue = Volley.newRequestQueue(this);//取得請求Queue物件
    }
}
