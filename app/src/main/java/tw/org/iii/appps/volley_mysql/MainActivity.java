package tw.org.iii.appps.volley_mysql;
//缺點:1.必須包在執行緒 //2.可能要透過handler
//目的:android Volley :是一個HTtp的 可以讓我們的網路在網頁可以執行非常快,執行緒跟ui一次就處理掉
//他會自動幫我們排程
//AJAX: 非同步Javascript的表現方式,自動管理排程把任務丟給他就好

//*Send a simple request:自動管理排程把任務丟給他就好
//*requestQueue:Queue會自動排程一個就好省記憶體,所以創一個appcation全域的變數方法
//MainApp.Queue 一個Queue物件自動排程,省記憶體

//1.先在檔案管理加入int網路權限,跟明碼傳送
//2.build.gradle=>Module,加上 implementation 'com.android.volley:volley:1.1.1'
//3.Send a simple request:幫我們徘程,就是取代之前要用執行緒去排任務的事情,會幫我們自動管理任務的排程
//3-1.Use newRequestQueue:我們會跟Queue來自動排程,對app來說一組一個就好,所以如果跳頁面也用同一個就好,這邊另外寫類別繼承Apiction
//3-2.寫一個class類別,我習慣叫MainApp,去繼承Application,這時去檔案總管Application內容,加上android:name=".MainApp",作為主要的管理動作
//因為頁面頁面之間的傳遞,用這招省去傳參數,比較方便
//3-3.設置on Creat在創建時, 取得Reqquer物件queue = Volley.newRequestQueue(this);//從Volley物件取得RequestQueue(回傳到RequestQueue)
//3-4.回到主程式從applictaion宣告呼叫來玩
//4.appication:usesCleartextTraffic/true :允許名馬傳送

//Json =>先看是物件還是陣列決定用JsonArrayRquest還是JsonObjectRequest
//如果是Post一定要用第二招
//如果是get可用這招,這招給的是JasonArray,省一層

//最大寬度,如果不指定就0,0
//Bitmap.Config.ARGB_8888,//影像要如何做組態解碼
// null //錯誤訊息


//getApplication()://取得application (回傳到Application )
//RequestQueue.add(Request<T> request)://將回呼的資料結構新增上去(回傳值Request<T>)
//Volley.newRequestQueue(Context context):從Volley物件取得RequestQueue(回傳到RequestQueue)
//Volley.StringRequest:(
//            int method,////1.使用的方法post/get
//            String url,//2.要連接的網址
//            Listener<String> listener,//3.傳呼回來的Rseponse回應監聽者
//            @Nullable ErrorListener errorListener)//4.這邊回傳犯行設定的String訊息
//Volley.newRequestQueue(Context context)://取得請求Queue物件(回傳值RequestQueue)

//Volley.ImageRequest(
//            String url,//1.url網址
//            Response.Listener<Bitmap> listener,//2.回乎回來的圖片Bitmap
//            int maxWidth,//3.最大寬度
//            int maxHeight,//4.最大高度
//            ScaleType scaleType,//5.縮放
//            Config decodeConfig,//6.圖片組太型別
//            @Nullable Response.ErrorListener errorListener)//7.錯誤訊息
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private MainApp mainApp;
    private TextView tv;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainApp = (MainApp)getApplication(); //取得自訂的Application物件可以玩裡面的Queue
        tv = findViewById(R.id.id_tv);
        img = findViewById(R.id.img);
    }

//    StringRequest(
//            int method,
//            String url,
//            Listener<String> listener,
//            @Nullable ErrorListener errorListener)

    //1.取得頁面原始碼呈現在View上
    public void text1(View view) {
        StringRequest request = new StringRequest(
                Request.Method.GET,//1.使用的方法
                "https://kanchengzxdfgcv.blogspot.com/2018/01/json-volley.html",//2.要連接的url網址
                new Response.Listener<String>() {//3.回乎回來的資料結構<這邊是String>
                    @Override
                    public void onResponse(String response) {
                        Log.v("brad","回傳回來的Response:" + response);
                        tv.setText(response);
                    }
                },
                null//4.錯誤訊息:通常是開發者錯誤造成的訊息
        );
        mainApp.queue.add(request);//queue把任務新增給他,自動排程去抓回respsone資料結構

    }
    //2.Open Data 抓取Json資料呈現
    public void text2(View view) {
        String url = "http://data.coa.gov.tw/Service/OpenData/ODwsv/ODwsvTravelFood.aspx";
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseJSON(response);
                    }
                },
                null
        );
        mainApp.queue.add(request);
    }
    //2.解析Json方法
    //JSONArray(String json):解JsonArray(要解的json)
    //JSONArray.getJSONObject(int index):解物件(index)(回傳JSONObject)
    private void parseJSON(String response) {
        try {
            JSONArray root = new JSONArray(response); //解第一層陣列
            for(int i=0; i<root.length(); i++){//從裡面的第0開始尋訪
                JSONObject row  = root.getJSONObject(i);//從陣列物件裡面.解開物件第(0)筆
                Log.v("brad","Name:" + row.getString("Name") + "Address:"+ row.getString("Address"));//印出第0筆物件的key:value
            }
        }catch (Exception e){
            Log.v("brad", "" +e.toString());
        }
    }
    //4.拿到JsonArray去解物件
    private void parseJSON2(JSONArray response) {
        for(int i=0; i<response.length(); i++){
            try {
                JSONObject row = response.getJSONObject(i);
                Log.v("brad","Name:" + row.getString("Name") + "Address:"+ row.getString("Address"));//印出第0筆物件的key:value
            } catch (JSONException e) {
               Log.v("brad","" + e.toString());
            }
        }
    }

    //3.取得uri圖片
//    ImageRequest(
//            String url,//1.url網址
//            Response.Listener<Bitmap> listener,//2.回乎回來的圖片Bitmap
//            int maxWidth,//3.最大寬度
//            int maxHeight,//4.最大高度
//            ScaleType scaleType,//5.縮放
//            Config decodeConfig,//6.圖片組太型別
//            @Nullable Response.ErrorListener errorListener)//7.錯誤訊息
    public void text3(View view) {
        String url = "https://ezgo.coa.gov.tw/Uploads/opendata/TainmaMain01/APPLY_D/20151007173924.jpg";
        ImageRequest request = new ImageRequest(
                url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        img.setImageBitmap(response);
                    }
                },
                0,0,
                null,
                Bitmap.Config.ARGB_8888,
                null
        );
        mainApp.queue.add(request);
    }
    //4. JsonArrayRequest/JsonObjectRequest:介紹方法先看第一層是陣列還是物件,少一個步驟,解析Json資料
    public void text4(View view) {
        String url = "http://data.coa.gov.tw/Service/OpenData/ODwsv/ODwsvTravelFood.aspx";

        JsonArrayRequest request = new JsonArrayRequest(
                url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        parseJSON2(response);
                    }
                },
                null
        );
        mainApp.queue.add(request);
    }

    //5.自己定義的Rqueset<byte[]>資料結構,方便串流使用
    //*繼承Request<你要call back回來的資料結構>
    //*建構式,實作方法
    //*其實最主要的參數是Response.Listner所以加進去

    private  class MyInputStreamReuqest extends Request<byte[]>{
        private final Response.Listener<byte[]> listener;
        public MyInputStreamReuqest(int method,
                                    Response.Listener<byte[]> listen,//自己家的回乎參數
                                    String url,
                                    @Nullable Response.ErrorListener listener) {
            super(method, url, listener);
            this.listener = listen;
        }
        //要實作的方法一
        @Override
        protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {
            return null;
        }
        //要實作的方法二,回乎回來的資料結構放入影響結果
      @Override
        protected void deliverResponse(byte[] response) {
            listener.onResponse(response);
        }
    }

}
