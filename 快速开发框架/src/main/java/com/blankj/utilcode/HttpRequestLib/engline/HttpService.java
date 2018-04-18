package com.blankj.utilcode.HttpRequestLib.engline;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.HttpRequestLib.HttpRequestType;
import com.blankj.utilcode.HttpRequestLib.constant.ErrorCodeConstant;
import com.blankj.utilcode.HttpRequestLib.dao.IHttpListener;
import com.blankj.utilcode.HttpRequestLib.dao.IHttpService;
import com.blankj.utilcode.HttpRequestLib.util.HttpParseResponse;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

/**
 * Post请求专用类
 * Created by 61642 on 2018/4/16.
 */

@SuppressWarnings("ALL")
public class HttpService implements IHttpService {
    /**
     * Http连接
     */
    HttpURLConnection httpURLConnection;
    /**
     * 请求数据
     */
    private Map<String ,String> requestData_KeyValue;
    /**
     * 请求地址
     */
    private String url;
    /**
     * 请求数据
     */
    private String requestData;
    /**
     * 响应回调
     */
    private IHttpListener iHttpListener;

    private long timeOut = 30000;

    @Override
    public void setTimeOut(long time) {
        this.timeOut = time;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public void setRequestData(String requestData) {
        this.requestData = requestData;
    }

    @Override
    public void setRequestData(Map<String, String> requestData) {

    }

    @Override
    public void setIHttpListener(IHttpListener iHttpListener) {
        this.iHttpListener = iHttpListener;
    }

    @Override
    public void execute(int httpRequestType) {
        if (HttpRequestType.Post_String==httpRequestType){
            httpPostString();
        }else  if (HttpRequestType.Get==httpRequestType){
            httpGet();
        }else  if (HttpRequestType.Post_Key_Value==httpRequestType){
            postKeyValue();
        }

    }

    /**
     * 发起POST键值对请求
     */
    private void postKeyValue( ){
        try {
            URL url = new URL(this.url);//创建一个URL
            HttpURLConnection connection  = (HttpURLConnection)url.openConnection();//通过该url获得与服务器的连接
            httpURLConnection.setConnectTimeout((int) timeOut);//连接超时时间
            httpURLConnection.setUseCaches(false);//是否使用缓存
            httpURLConnection.setInstanceFollowRedirects(false);//是否系统自动处理跳转
            httpURLConnection.setReadTimeout((int) (timeOut / 2));//响应的超时时间
            httpURLConnection.setDoInput(true);//是否可以读入数据
            httpURLConnection.setDoOutput(true);//是否可以写出数据
            httpURLConnection.setRequestMethod("POST");//请求方式
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //post方法要传送的键值对
            for (String key : requestData_KeyValue.keySet()) {
                connection.setRequestProperty(key, requestData_KeyValue.get(key));
            }
            connection.connect();
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String response= HttpParseResponse.getContent(httpURLConnection.getInputStream(),iHttpListener);
                if (iHttpListener != null) {
                    iHttpListener.onSuccess(response);
                }
            } else {
                if (iHttpListener != null) {
                    iHttpListener.onFail(httpURLConnection.getResponseCode(), httpURLConnection.getResponseMessage());
                }
            }
        } catch (IOException  e) {
            e.printStackTrace();
            if (iHttpListener != null) {
                iHttpListener.onFail(ErrorCodeConstant.IOException, e.getMessage());
            }
        }
    }


    /**
     * 发起POST String请求
     */
    private void httpPostString( ){
        String response=null;
        try {
            URL url = new URL(this.url);
            httpURLConnection = (HttpURLConnection) url.openConnection();//打开连接
            httpURLConnection.setConnectTimeout((int) timeOut);//连接超时时间
            httpURLConnection.setUseCaches(false);//是否使用缓存
            httpURLConnection.setInstanceFollowRedirects(false);//是否系统自动处理跳转
            httpURLConnection.setReadTimeout((int) (timeOut / 2));//响应的超时时间
            httpURLConnection.setDoInput(true);//是否可以读入数据
            httpURLConnection.setDoOutput(true);//是否可以写出数据
            httpURLConnection.setRequestMethod("POST");//请求方式
            httpURLConnection.setRequestProperty("Content-type", "application/x-java-serialized-object"); // 设定传送的内容类型是可序列化的java对象
            httpURLConnection.connect();//连接
            //字节流发送数据
            OutputStream ops = httpURLConnection.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(ops);
            if (requestData!=null){
                bos.write(requestData.getBytes());
            }else {
                bos.write("".getBytes());
            }
            bos.flush();
            bos.close();
            bos=null;
            ops.close();
            ops=null;
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                response= HttpParseResponse.getContent(httpURLConnection.getInputStream(),iHttpListener);
                if (iHttpListener != null) {
                    iHttpListener.onSuccess(response);
                }
            } else {
                if (iHttpListener != null) {
                    iHttpListener.onFail(httpURLConnection.getResponseCode(), httpURLConnection.getResponseMessage());
                }
            }
            LogUtils.e(url+"\n"+new String(requestData)+"\n"+httpURLConnection.getResponseCode()+"\n"+response+"\n"+httpURLConnection.getResponseMessage());

        } catch (MalformedURLException e) {
            e.printStackTrace();
            if (iHttpListener != null) {
                iHttpListener.onFail(ErrorCodeConstant.MalformedURLException, e.getMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (iHttpListener != null) {
                iHttpListener.onFail(ErrorCodeConstant.IOException, e.getMessage());
            }
        }finally {
            if (httpURLConnection!=null) {
                httpURLConnection.disconnect();
            }
            httpURLConnection=null;
        }
    }
    /**
     * 发起get请求
     */
    public void httpGet() {
        StringBuffer buffer = new StringBuffer();
        try {
            URL url = new URL(this.url);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout((int) timeOut);//连接超时时间
            httpURLConnection.setUseCaches(false);//是否使用缓存
            httpURLConnection.setInstanceFollowRedirects(false);//是否系统自动处理跳转
            httpURLConnection.setReadTimeout((int) (timeOut / 2));//响应的超时时间
            httpURLConnection.setDoInput(true);//是否可以读入数据
            httpURLConnection.setDoOutput(true);//是否可以写出数据
            httpURLConnection.setRequestMethod("GET");//请求方式
            httpURLConnection.connect();

            // 将返回的输入流转换成字符串
            InputStream inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                if (iHttpListener != null) {
                    iHttpListener.onSuccess(buffer.toString());
                }
            } else {
                if (iHttpListener != null) {
                    iHttpListener.onFail(httpURLConnection.getResponseCode(), httpURLConnection.getResponseMessage());
                }
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            bufferedReader = null;
            inputStreamReader = null;
            inputStream = null;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            if (iHttpListener != null) {
                iHttpListener.onFail(ErrorCodeConstant.MalformedURLException, e.getMessage());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            if (iHttpListener != null) {
                iHttpListener.onFail(ErrorCodeConstant.UnsupportedEncodingException, e.getMessage());
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
            if (iHttpListener != null) {
                iHttpListener.onFail(ErrorCodeConstant.ProtocolException, e.getMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (iHttpListener != null) {
                iHttpListener.onFail(ErrorCodeConstant.IOException, e.getMessage());
            }
        } finally {
            httpURLConnection.disconnect();
        }
    }





    /**
     * 多文件上传的方法
     *
     * @param actionUrl：上传的路径
     * @param uploadFilePaths：需要上传的文件路径，数组
     * @return
     */
    private String uploadFile(String actionUrl, String[] uploadFilePaths) {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        DataOutputStream ds = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer resultBuffer = new StringBuffer();
        String tempLine = null;

        try {
            // 统一资源
            URL url = new URL(actionUrl);
            // 连接类的父类，抽象类
            URLConnection urlConnection = url.openConnection();
            // http的连接类
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;

            // 设置是否从httpUrlConnection读入，默认情况下是true;
            httpURLConnection.setDoInput(true);
            // 设置是否向httpUrlConnection输出
            httpURLConnection.setDoOutput(true);
            // Post 请求不能使用缓存
            httpURLConnection.setUseCaches(false);
            // 设定请求的方法，默认是GET
            httpURLConnection.setRequestMethod("POST");
            // 设置字符编码连接参数
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            // 设置字符编码
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            // 设置请求内容类型
            httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            // 设置DataOutputStream
            ds = new DataOutputStream(httpURLConnection.getOutputStream());
            for (int i = 0; i < uploadFilePaths.length; i++) {
                String uploadFile = uploadFilePaths[i];
                String filename = uploadFile.substring(uploadFile.lastIndexOf("//") + 1);
                ds.writeBytes(twoHyphens + boundary + end);
                ds.writeBytes("Content-Disposition: form-data; " + "name=\"file" + i + "\";filename=\"" + filename
                        + "\"" + end);
                ds.writeBytes(end);
                FileInputStream fStream = new FileInputStream(uploadFile);
                int bufferSize = 1024;
                byte[] buffer = new byte[bufferSize];
                int length = -1;
                while ((length = fStream.read(buffer)) != -1) {
                    ds.write(buffer, 0, length);
                }
                ds.writeBytes(end);
                /* close streams */
                fStream.close();
            }
            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
            /* close streams */
            ds.flush();
            if (httpURLConnection.getResponseCode() >= 300) {
                throw new Exception(
                        "HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
            }

            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                reader = new BufferedReader(inputStreamReader);
                tempLine = null;
                resultBuffer = new StringBuffer();
                while ((tempLine = reader.readLine()) != null) {
                    resultBuffer.append(tempLine);
                    resultBuffer.append("\n");
                }
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (ds != null) {
                try {
                    ds.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            return resultBuffer.toString();
        }
    }



    /**
     *
     * @param urlPath
     *            下载路径
     * @param downloadDir
     *            下载存放目录
     * @return 返回下载文件
     */
    private File downloadFile(String urlPath, String downloadDir) {
        File file = null;
        try {
            // 统一资源
            URL url = new URL(urlPath);
            // 连接类的父类，抽象类
            URLConnection urlConnection = url.openConnection();
            // http的连接类
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            // 设定请求的方法，默认是GET
            httpURLConnection.setRequestMethod("POST");
            // 设置字符编码
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            // 打开到此 URL 引用的资源的通信链接（如果尚未建立这样的连接）。
            httpURLConnection.connect();

            // 文件大小
            int fileLength = httpURLConnection.getContentLength();

            // 文件名
            String filePathUrl = httpURLConnection.getURL().getFile();
            String fileFullName = filePathUrl.substring(filePathUrl.lastIndexOf(File.separatorChar) + 1);

            System.out.println("file length---->" + fileLength);

            URLConnection con = url.openConnection();

            BufferedInputStream bin = new BufferedInputStream(httpURLConnection.getInputStream());

            String path = downloadDir + File.separatorChar + fileFullName;
            file = new File(path);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            OutputStream out = new FileOutputStream(file);
            int size = 0;
            int len = 0;
            byte[] buf = new byte[1024];
            while ((size = bin.read(buf)) != -1) {
                len += size;
                out.write(buf, 0, size);
                // 打印下载百分比
                // System.out.println("下载了-------> " + len * 100 / fileLength +
                // "%\n");
            }
            bin.close();
            out.close();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            return file;
        }

    }


}
