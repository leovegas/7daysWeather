package com.leo.weather7days;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import com.leo.weather7days.jsonclasses.Daily;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CustomAdapter extends BaseAdapter {
    Context context;
    List<Daily> countryList;
    LayoutInflater inflter;
    StringBuilder stringBuilder = new StringBuilder();
    StringBuilder stringBuilder2 = new StringBuilder();
    Date d;


    public CustomAdapter(Context applicationContext, List<Daily> countryList) {
        this.context = context;
        this.countryList = countryList;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return countryList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public String getRound(double num, int commaPos) {
        double i = 0;
        if (commaPos==0) i=1;
        if (commaPos==1) i=10.0;
        if (commaPos==2) i=100.0;
        if (commaPos==3) i=1000.0;
        if (commaPos==4) i=10000.0;
        if (commaPos==5) i=100000.0;
        if (commaPos==6) i=1000000.0;

        String res = String.valueOf((Math.round(num * i) / i));
        return res;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.listcontent, null);

        TextView date = (TextView) view.findViewById(R.id.listdate);
        TextView temp = (TextView) view.findViewById(R.id.listtemp);
        TextView tempmin = (TextView) view.findViewById(R.id.listtempmin);
        TextView rain = (TextView) view.findViewById(R.id.listrain);
        TextView wind = (TextView) view.findViewById(R.id.listwind);
        ImageView icon = (ImageView) view.findViewById(R.id.icon);

        try {
            d = MainActivity.FormattingDate.StringToDate(String.valueOf(countryList.get(i).time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        stringBuilder = new StringBuilder();
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append(getRound(countryList.get(i).rain,1)+"\n");
        stringBuilder2.append(getRound(countryList.get(i).snow,1));
        stringBuilder.append(Math.round(countryList.get(i).temperature)+"°\n");
        stringBuilder.append(Math.round(countryList.get(i).temperatureMin)+"°");
        date.setText(new SimpleDateFormat("d MMM\nEEE", Locale.getDefault()).format(new Date(d.getTime())));
        temp.setText(countryList.get(i).type.replace("-","\n"));
        tempmin.setText(stringBuilder);
        wind.setText(Math.round(countryList.get(i).windSpeed)+" m/s");
        rain.setText(stringBuilder2);

        return view;
    }
}
