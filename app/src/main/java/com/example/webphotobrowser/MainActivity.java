package com.example.webphotobrowser;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.renderscript.ScriptGroup;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
//14到23行导入需要的包
import android.widget.ImageView;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.InputStream;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.ByteArrayOutputStream;
import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
//    26行定义ImageView对象imgPic，然后定义Bitmap对象bitmap，定义String类型变量res，下面定义String类型常量PIC_URL
    private ImageView imgPic;//define ImageView obj imgPic
    private Bitmap bitmap;//define Bitmap obj bitmap
    private String res;//define string type variable res
    //define String type constant var  PIC_URL
    private final static String PIC_URL = "https://static1.bcjiaoyu.com/androidHighLesson.json";
//    private final static String PIC_URL = "https://github.com/mayujie/Resource/blob/master/photosResource.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//40行调用getImage()方法，方法中请求图片数据
        getImage();//request photos data

    }
//    这是getImage()方法的代码，45行开辟一个子线程进行网络请求，95行启动线程执行47行run方法，执行48行try catch语句，然后我们再看try语句中的代码
    public void getImage(){//define a method to get photos data from web
        new Thread(){// network request is not allow run in main thread, so we need to open a subThread to do
            public void run(){
                try{
                    URL url = new URL(PIC_URL);// pass PIC_URL, create url 传入PIC_URL，初始化创建url
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();// get the connection, url.openConnection()获得网络连接
                    conn.setConnectTimeout(5000);// set connection time limit as 5s, 然后设置连接超时为5秒
                    conn.setRequestMethod("GET");// set request type GET 设置请求类型为Get类型
                    if (conn.getResponseCode() != 200)//check if the request successful 判断请求Url是否成功
                    {
                        System.out.println("-------Request Failed ! -------");//conn.getResponseCode()返回值不等于200，打印“请求失败”
                    }else{
                        System.out.println("-------Request Successful ! -------");//否则执行else代码块
                        InputStream inStream = conn.getInputStream();//get inputStream of connection 获取连接的输入流，然后创建一个输出流baos来读取输入流
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();//create a outputStream to read the inputStream
                        // read the data (read and get data at same time) 61到68行是对数据的读取
                        int len = 0;//定义int类型变量len为0
                        byte[] buf = new byte[1024];//define byte array, length is 1024 定义字节数组buf，长度为1024
//put input data store in corresponding byte array, check if return value != -1 then pass to len
//将输入的数据存放在指定的字节数组buf中，判断返回值!=(不等于)-1的话就赋给len, 只要满足while条件执行59行将字节数组buf中从0位置开始的、长度为len个字节的数据写到输出流baos中
                        while((len = inStream.read(buf)) != -1){
                            //write data into outputStream baos, the bytes start from pos 0 length as len in byte array buf
                            baos.write(buf, 0, len);
                        }
//toByteArray()将输出流baos返回字节数组，utf-8设置编码格式，这行代码将获得的输出流变成String类型的字符串，用于最后的返回，然后关闭数据流
                        //get the outputStream which become String type string, will be use in final return
                        String result = new String(baos.toByteArray(), "utf-8");
                        inStream.close();//close dataStream
                        //the process of get data 75行开始是数据的获取过程
                        JSONArray array = new JSONArray(result);//get json array 传入result获得json数组，然后遍历数组array，length()是获取数组长度的方法
                        for (int i = 0; i < array.length(); i++){//work through whole array
//数组数据是通过getJSONObject根据索引值获取，我们获取数组中索引为i的数据，得到json对象obj，下面getString方法获取obj中key为image的键值再赋值给res
                            JSONObject obj = array.getJSONObject(i);//get data from array based on index value
                            res = obj.getString("image");//getString() to get value which key is "image" from obj
/*switch语句，传入i，i等于0时，找到id为imgone的控件赋给imgPic，调用returnBitMap方法，传入res和imgpic两个参数，
i等于1时，找到id为imgtwo的控件赋给imgPic，调用returnBitMap方法，传入res和imgpic两个参数
i等于2时，找到id为imgthree的控件赋给imgPic，调用returnBitMap方法，传入res和imgpic两个参数
i等于3时，找到id为imgfour的控件赋给imgPic，调用returnBitMap方法，传入res和imgpic两个参数，其余情况打印“The other photo will not display”*/
                            switch (i){
                                case 0:
                                    imgPic = (ImageView) findViewById(R.id.imgone);//find widget id is imgone and pass to imgPic
                                    returnBitMap(res, imgPic);//call returnBitMap, pass res and imgPic two arguments
                                    break;
                                case 1:
                                    imgPic = (ImageView) findViewById(R.id.imgtwo);//find widget id is imgtwo and pass to imgPic
                                    returnBitMap(res, imgPic);//call returnBitMap, pass res and imgPic two arguments
                                    break;
                                case 2:
                                    imgPic = (ImageView) findViewById(R.id.imgthree);//find widget id is imgthree and pass to imgPic
                                    returnBitMap(res, imgPic);//call returnBitMap, pass res and imgPic two arguments
                                    break;
                                case 3:
                                    imgPic = (ImageView) findViewById(R.id.imgfour);//find widget id is imgfour and pass to imgPic
                                    returnBitMap(res, imgPic);//call returnBitMap, pass res and imgPic two arguments
                                    break;
                                default:
                                    System.out.println("The other photo will not display!");
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.start();
    }
/*returnBitMap()方法，接收两个参数，url和imgPic，在这个方法里将网络图片转换成bitmap，并将图片显示出来，115行定义URL对象myFileUrl为null，116行try catch语句，
第117行传入url初始化myFileUrl，然后下面获得网络连接，设置连接超时为5秒*/
    private void returnBitMap(final String url, final ImageView imgPic){//convert internet image to bitmap
        URL myFileUrl = null;//define URL object myFileUrl as null
        try{
            myFileUrl = new URL(url);// pass url initialize myFileUrl
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();// get connection
            conn.setConnectTimeout(5000);// set connection limit time as 5s
            if (conn.getResponseCode() != 200){// check if request url successful
                System.out.println("Request Failed !!!!!!");//判断请求Url是否成功，conn.getResponseCode()返回值不等于200，打印“请求失败”，否则执行else代码块
            }else {
                System.out.println("Request Successful !!!!!!");
                InputStream is = conn.getInputStream();//get inputStream from connection 获取连接的输入流
                bitmap = BitmapFactory.decodeStream(is);// load bitmap from inputStream by using decodeStream
                is.close();// close data Stream 通过decodeStream 从输入流加载bitmap，然后关闭数据流，
                imgPic.post(new Runnable(){//post(new Runnable) the method of update UI
                    public void run(){//post(new Runnable()是更新UI的方法，我们在这里更新UI将图片显示出来
                        imgPic.setImageBitmap(bitmap);//setImageBitmap() to set the display of photo, pass in bitmap
                    }//执行128行run方法，setImageBitmap设置图片显示，传入bitmap，到此网络图片浏览器全部完成
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
//#############################################################################
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
}
