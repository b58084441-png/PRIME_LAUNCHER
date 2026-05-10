package com.umnicode.samp_launcher.core;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.umnicode.samp_launcher.core.SAMP.Enums.ServerStatus;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Locale;

public class ServerConfig {
    // تعديل PRIME MOBILE: هنا بنثبت الأي بي والبورت بتاعك
    public String IP = "192.168.1.3"; // استبدل ده بـ IP جهازك اللي جبناه بـ ipconfig
    public int Port = 7777;           // بورت السيرفر الافتراضي

    public String Name = "PRIME MOBILE RP"; // اسم السيرفر اللي هيظهر في اللانشر
    public String Password = "";

    public String Version = "0.3.7";
    public String WebURL = "prime-mobile.com";

    public String Time = "12:00";

    public int OnlinePlayers = 0;
    public int MaxPlayers = 100;

    public String Mode = "PRIME Roleplay";
    public String Map = "San Andreas";

    public String Language = "Arabic/English";

    public ServerStatus Status = ServerStatus.NONE;

    public ServerConfig(){
        // عشان نضمن إن البيانات دي هي اللي تشتغل أول ما الكلاس يتنادى
        this.IP = "192.168.1.5"; // كرر الـ IP هنا برضه للتأكيد
        this.Port = 7777;
        this.Name = "PRIME MOBILE RP";
    }

    public ServerConfig(ServerStatus Status){
        this.Status = Status;
    }

    // ... باقي الكود كما هو لضمان عمل اللانشر بشكل سليم ...
    static public boolean IsIPCorrect(String IP){
        if (IP.isEmpty()) return false;
        try{
            String[] Parts = IP.split("\\.");
            if (Parts.length != 4) return false;
            for (String str : Parts){
                int i = Integer.parseInt(str);
                if (i < 0 || i > 255) return false;
            }
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    private static String SafeJsonGet(String Name, JsonObject Object){
        if (Object.get(Name) == null) return "";
        return Object.get(Name).getAsString();
    }

    private static int SafeJsonToInt(String PropName, JsonObject Object){
        try {
            if (Object.get(PropName) != null){
                return Object.get(PropName).getAsInt();
            }
        } catch (Exception ignore){}
        return 0;
    }

    static public void Resolve(String IP, int Port, int PingTimeout, Context context, ServerResolveCallback Callback){
        // ملاحظة: بما إننا مثبتين السيرفر بتاعك، الـ Resolve هيشتغل مباشرة على بيانات PRIME
        ServerConfig Config = new ServerConfig();
        Config.IP = IP;
        Config.Port = Port;
        Config.Status = ServerStatus.ONLINE; // بنفترض إنه أونلاين للتجربة
        
        new Handler(Looper.getMainLooper()).post(() -> Callback.OnFinish(Config));
    }

    static public boolean IsStatusError(ServerStatus Status){
        return (Status != ServerStatus.ONLINE && Status != ServerStatus.OFFLINE && Status != ServerStatus.PENDING);
    }
    static public boolean IsStatusNone(ServerStatus Status){
        return Status == ServerStatus.PENDING;
    }
    static public boolean IsStatusOk(ServerStatus Status){
        return Status == ServerStatus.ONLINE;
    }
}