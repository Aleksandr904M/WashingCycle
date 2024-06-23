package com.example.washingcycle;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;



public class MainActivity extends AppCompatActivity {

    private TextView textMessage, textFinal;
    private EditText casinglength, casingDiameter, lenghtLBT, outerDiameterLBT, innerDiameterLBT,
            lenghtTBPK, outerDiameterTBPK, innerDiameterTBPK, solutionConsumption, diameterChisel, cavernous;
    private Button washingCycle, gasLag, save;

    private double casinglengthDouble, casingDiameterDouble, lenghtLBTDouble, outerDiameterLBTDouble,
            innerDiameterLBTDouble, lenghtTBPKDouble, outerDiameterTBPKDouble, innerDiameterTBPKDouble,
            solutionConsumptionDouble, diameterChiselDouble, cavernoushDouble;

    SharedPreferences sPref;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //запрет смены ориентации для портретного режима

        textMessage = findViewById(R.id.textMessage); //надписи
        textFinal = findViewById(R.id.textFinal);

        casinglength = findViewById(R.id.editCasingLength); //поля
        casingDiameter = findViewById(R.id.editCasingDiameter);
        lenghtLBT = findViewById(R.id.editLengthLBT);
        outerDiameterLBT = findViewById(R.id.editOuterDiameterLBT);
        innerDiameterLBT = findViewById(R.id.editInnerDiameterLBT);
        lenghtTBPK = findViewById(R.id.editLengthTBPK);
        outerDiameterTBPK = findViewById(R.id.editOuterDiameterTBPK);
        innerDiameterTBPK = findViewById(R.id.editInnerDiameterTBPK);
        solutionConsumption = findViewById(R.id.editSolutionConsumption);
        diameterChisel = findViewById(R.id.editDiameterChisel);
        cavernous = findViewById(R.id.editCavernous);

        washingCycle = findViewById(R.id.buttonWashingCycle); //кнопки
        gasLag = findViewById(R.id.buttonGasLag);
        save = findViewById(R.id.buttonSave);

        loadText();                            //загрузка сохраненных данных пользователя

        washingCycle.setOnClickListener(new View.OnClickListener() { //обработчик события
            @Override
            public void onClick(View v) {             //при нажатии не кнопку

                gettingDataDouble();         //получение данных

                double totalLenght = lenghtLBTDouble + lenghtTBPKDouble;
                double innerRes = (Math.PI * Math.pow(innerDiameterLBTDouble / 2 , 2) * lenghtLBTDouble)
                        + (Math.PI * Math.pow(innerDiameterTBPKDouble / 2, 2) * lenghtTBPKDouble); //внутри инструмента
                double outerRes = (Math.PI * Math.pow(outerDiameterLBTDouble / 2 , 2) * lenghtLBTDouble)
                        + (Math.PI * Math.pow(outerDiameterTBPKDouble / 2, 2) * lenghtTBPKDouble); // объем по внешнему диаметру инструмента
                double fullRes = ((Math.PI * Math.pow(casingDiameterDouble /2, 2)) * casinglengthDouble) +
                        ((Math.PI * Math.pow((diameterChiselDouble / 2) * cavernoushDouble, 2)) *
                                                     (totalLenght - casinglengthDouble));//полный объем
                /*double zatrubX = (Math.PI * Math.pow(diameterChiselDouble * cavernoushDouble, 2) * (totalLenght - casinglengthDouble)) / 4 +
                        (Math.PI * Math.pow(casingDiameterDouble,2) * casinglengthDouble) / 4;
                double obyemInstrum = outerRes - innerRes;*/  //для проверки подсчета оставил
                double zatrub = fullRes - outerRes;
                int resultTmp = (int)((((innerRes + (zatrub)) / solutionConsumptionDouble) / 60) * 10);
                double result = resultTmp / 10.0;
                textMessage.setText("Один цикл промывки скважины равен");
                textFinal.setText(String.valueOf(result) + " мин");
            }
        });

        gasLag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gettingDataDouble();         //получение данных

                double totalLenght = lenghtLBTDouble + lenghtTBPKDouble;
                double outerRes = (Math.PI * Math.pow(outerDiameterLBTDouble / 2 , 2) * lenghtLBTDouble)
                        + (Math.PI * Math.pow(outerDiameterTBPKDouble / 2, 2) * lenghtTBPKDouble); // объем по внешнему диаметру инструмента
                double fullRes = ((Math.PI * Math.pow(casingDiameterDouble /2, 2)) * casinglengthDouble) +
                        ((Math.PI * Math.pow((diameterChiselDouble / 2) * cavernoushDouble, 2)) *
                                (totalLenght - casinglengthDouble));//полный объем
                double zatrub = fullRes - outerRes;
                int resultTmp = (int)(((zatrub / solutionConsumptionDouble) / 60) * 10);
                double result = resultTmp / 10.0;

                textMessage.setText("Время выхода газа с указанной глубины");
                textFinal.setText(String.valueOf(result) + " мин");
            }
        });

        save.setOnClickListener(new View.OnClickListener() { //кнопка сохраняет введенные данные
            @Override
            public void onClick(View v) {

                sPref = getSharedPreferences("setting",MODE_PRIVATE);
                SharedPreferences.Editor editor = sPref.edit();
                editor.putString("casinglength", casinglength.getText().toString());
                editor.putString("casingDiameter", casingDiameter.getText().toString());
                editor.putString("lenghtLBT", lenghtLBT.getText().toString());
                editor.putString("outerDiameterLBT", outerDiameterLBT.getText().toString());
                editor.putString("innerDiameterLBT", innerDiameterLBT.getText().toString());
                editor.putString("lenghtTBPK", lenghtTBPK.getText().toString());
                editor.putString("outerDiameterTBPK", outerDiameterTBPK.getText().toString());
                editor.putString("innerDiameterTBPK", innerDiameterTBPK.getText().toString());
                editor.putString("diameterChisel", diameterChisel.getText().toString());
                editor.putString("cavernous", cavernous.getText().toString());
                editor.commit();

                textMessage.setText("Данные сохранены");
                textFinal.setText("");
            }
        });
    }

    private void loadText () {        //метод для считывания сохраненных настроек
            sPref = getSharedPreferences("setting",MODE_PRIVATE);

            casinglength.setText(sPref.getString("casinglength", "800"));
            casingDiameter.setText(sPref.getString("casingDiameter", "229"));
            lenghtLBT.setText(sPref.getString("lenghtLBT", "0"));
            outerDiameterLBT.setText(sPref.getString("outerDiameterLBT", "147"));
            innerDiameterLBT.setText(sPref.getString("innerDiameterLBT", "125"));
            lenghtTBPK.setText(sPref.getString("lenghtTBPK", "0"));
            outerDiameterTBPK.setText(sPref.getString("outerDiameterTBPK", "127"));
            innerDiameterTBPK.setText(sPref.getString("innerDiameterTBPK", "108.6"));
            solutionConsumption.setText(sPref.getString("solutionConsumption", "32"));
            diameterChisel.setText(sPref.getString("diameterChisel", "220.7"));
            cavernous.setText(sPref.getString("cavernous", "1.10"));
    }

    private void gettingDataDouble () {     //метод для получения данных из полей приложения
        if (casinglength.getText().toString().equals("")) {casinglengthDouble = 0;}
        else {casinglengthDouble = Double.parseDouble(casinglength.getText().toString());}

        if (casingDiameter.getText().toString().equals("")) {casingDiameterDouble = 0;}
        else {casingDiameterDouble = (Double.parseDouble(casingDiameter.getText().toString()))/1000;}

        if (lenghtLBT.getText().toString().equals("")) {lenghtLBTDouble = 0;}
        else {lenghtLBTDouble = Double.parseDouble(lenghtLBT.getText().toString());}

        if (outerDiameterLBT.getText().toString().equals("")) {outerDiameterLBTDouble = 0;}
        else {outerDiameterLBTDouble = (Double.parseDouble(outerDiameterLBT.getText().toString()))/1000;}

        if (innerDiameterLBT.getText().toString().equals("")) {innerDiameterLBTDouble = 0;}
        else {innerDiameterLBTDouble = (Double.parseDouble(innerDiameterLBT.getText().toString()))/1000;}

        if (lenghtTBPK.getText().toString().equals("")) {lenghtTBPKDouble = 0;}
        else {lenghtTBPKDouble = Double.parseDouble(lenghtTBPK.getText().toString());}

        if (outerDiameterTBPK.getText().toString().equals("")) {outerDiameterTBPKDouble = 0;}
        else {outerDiameterTBPKDouble = (Double.parseDouble(outerDiameterTBPK.getText().toString()))/1000;}

        if (innerDiameterTBPK.getText().toString().equals("")) {innerDiameterTBPKDouble = 0;}
        else {innerDiameterTBPKDouble = (Double.parseDouble(innerDiameterTBPK.getText().toString()))/1000;}

        if (solutionConsumption.getText().toString().equals("")) {solutionConsumptionDouble = 0;}
        else {solutionConsumptionDouble = (Double.parseDouble(solutionConsumption.getText().toString()))/1000;}

        if (diameterChisel.getText().toString().equals("")) {diameterChiselDouble = 0;}
        else {diameterChiselDouble = (Double.parseDouble(diameterChisel.getText().toString()))/1000;}

        if (cavernous.getText().toString().equals("")) {cavernoushDouble = 0;}
        else {cavernoushDouble = Double.parseDouble(cavernous.getText().toString());}
    }
}