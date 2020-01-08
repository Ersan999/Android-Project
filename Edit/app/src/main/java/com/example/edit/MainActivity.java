package com.example.edit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView FoodView, RentView, ClothesView;

    EditText FoodTxt, RentTxt, ClothesTxt,FunTxt, BalanceTxt, MonthTxt, AmountTxt;

    Button AutomaticBtn, ManualBtn;

    int TotalNum, BalanceNum, FoodNum, RentNum, ClothesNum, FunNum, MonthNum, AmountNum;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BalanceTxt = (EditText)findViewById(R.id.BalanceTxt);
        RentTxt = (EditText)findViewById(R.id.RentTxt);
        FoodTxt = (EditText)findViewById(R.id.FoodTxt);
        ClothesTxt = (EditText)findViewById(R.id.ClothesTxt);
        FunTxt = (EditText)findViewById(R.id.FunTxt);
        MonthTxt = (EditText)findViewById(R.id.MonthTxt);
        AmountTxt = (EditText)findViewById(R.id.AmountTxt);

        AutomaticBtn = (Button)findViewById(R.id.AutomaticBtn);
        ManualBtn = (Button)findViewById(R.id.ManualBtn);

        ManualBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                BalanceNum = Integer.parseInt(BalanceTxt.getText().toString());
                RentNum = Integer.parseInt(RentTxt.getText().toString());
                FoodNum = Integer.parseInt(FoodTxt.getText().toString());
                ClothesNum = Integer.parseInt (ClothesTxt.getText().toString());
                FunNum = Integer.parseInt(FunTxt.getText().toString());

                TotalNum = RentNum + FoodNum + ClothesNum + FunNum;
                MonthTxt.setText(String.valueOf(TotalNum));

                AmountNum = BalanceNum - TotalNum;
                AmountTxt.setText(String.valueOf(AmountNum));

            }

        });


        AutomaticBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                RentTxt.setText("550");
                FoodTxt.setText("150");
                ClothesTxt.setText("35");
                FunTxt.setText("125");

                BalanceNum = Integer.parseInt(BalanceTxt.getText().toString());
                RentNum = Integer.parseInt(RentTxt.getText().toString());
                FoodNum = Integer.parseInt(FoodTxt.getText().toString());
                ClothesNum = Integer.parseInt (ClothesTxt.getText().toString());
                FunNum = Integer.parseInt(FunTxt.getText().toString());

                TotalNum = RentNum + FoodNum + ClothesNum + FunNum;
                MonthTxt.setText(String.valueOf(TotalNum));

                AmountNum = BalanceNum - TotalNum;
                AmountTxt.setText(String.valueOf(AmountNum));




            }
        });
        }
    }


