package com.king.Guess_The_Number;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private int input;
    private int score;
    private int RamdomNum = RamdomNum();
    private ConstraintLayout main_layout;
    private EditText NumInputField;
    private TextView TxtDica;
    private ImageButton BTNSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        main_layout = findViewById(R.id.main);
        NumInputField = findViewById(R.id.numInputField);
        TxtDica = findViewById(R.id.txtDica);
        BTNSend = findViewById(R.id.btnEnviar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void send(View view) {
        TextView TxtScore = findViewById(R.id.Score);

        String inputText = NumInputField.getText().toString();

        // Verifica se o campo não está vazio
        if (!inputText.isEmpty()) {
            try {
                // Converte para int
                input = Integer.parseInt(inputText);
                hideKeyboard(NumInputField);
            } catch (NumberFormatException e) {
                // Tratamento de erro se o texto não for um número válido
                e.printStackTrace();
                Toast.makeText(this, "Por favor, insira um número válido.", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            // Tratamento para o caso de campo vazio
            Toast.makeText(this, "O campo não pode estar vazio. Por favor, insira um número.", Toast.LENGTH_SHORT).show();
            return;
        }

        auth();
        score++;
        TxtScore.setText("Tentativas: " + score);
    }

    public void padrao(){
        ImageView Green_arrow_up = findViewById(R.id.green_arrow_up);
        ImageView Red_arrow_down = findViewById(R.id.red_arrow_down);
        ImageView Black_arrow_up = findViewById(R.id.black_arrow_up);
        ImageView Black_arrow_down = findViewById(R.id.black_arrow_down);

        Green_arrow_up.setVisibility(View.INVISIBLE); // Mostra a seta verde
        Red_arrow_down.setVisibility(View.INVISIBLE); // Mostra a seta vermelha
        Black_arrow_down.setVisibility(View.VISIBLE); // Garante que a seta preta está invisível
        Black_arrow_up.setVisibility(View.VISIBLE); // Garante que a seta preta está invisível
    }

    public void auth(){
        ImageView Green_arrow_up = findViewById(R.id.green_arrow_up);
        ImageView Red_arrow_down = findViewById(R.id.red_arrow_down);
        ImageView Black_arrow_up = findViewById(R.id.black_arrow_up);
        ImageView Black_arrow_down = findViewById(R.id.black_arrow_down);

        padrao();

        if(input == RamdomNum){
            popupLayout(main_layout);
            NumInputField.clearFocus();
        } else{
            NumInputField.setText("");
            if(input > RamdomNum){
                Green_arrow_up.setVisibility(View.VISIBLE); // Mostra a seta verde
                Black_arrow_up.setVisibility(View.INVISIBLE); // Garante que a seta preta está invisível
            }else {
                Red_arrow_down.setVisibility(View.VISIBLE); // Mostra a seta vermelha
                Black_arrow_down.setVisibility(View.INVISIBLE); // Garante que a seta preta está invisível
            }
        }
    }

    public void dica(View view) {
        if (RamdomNum % 2 == 0) {
            // Adiciona texto à TextView
            TxtDica.setText("PAR!");
            System.out.println("O número " + RamdomNum + " é par.");

        } else {
            // Adiciona texto à TextView
            TxtDica.setText("ÍMPAR!");
            System.out.println("O número " + RamdomNum + " é ímpar.");
        }
    }

    public void reset() {
        padrao();
        TextView TxtScore = findViewById(R.id.Score);

        BTNSend.setClickable(true);
        NumInputField.setClickable(true);
        NumInputField.setFocusable(true);
        NumInputField.setFocusableInTouchMode(true);

        TxtDica.setText("");
        NumInputField.setText("");
        RamdomNum = RamdomNum();
        score = 0;

        TxtScore.setText("Tentativas: " + score);
    }

    public void popupLayout(View v){
        // Inflar o layout do PopupWindow
        View popupView = LayoutInflater.from(MainActivity.this).inflate(R.layout.popup_layout, null);

        // Criar o PopupWindow
        final PopupWindow popupWindow = new PopupWindow(popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        // Tornar o PopupWindow focável e impedir interação fora
        popupWindow.setTouchable(true); // Permite que o PopupWindow responda a toques
        popupWindow.setFocusable(false); // Permite foco para o PopupWindow
        popupWindow.setOutsideTouchable(false); // Impede o fechamento ao tocar fora

        int paddingInPx = dpToPx(16);
        popupView.setPadding(paddingInPx, paddingInPx, paddingInPx, 0);

        BTNSend.setClickable(false);
        NumInputField.setClickable(false);
        NumInputField.setFocusable(false);

        // Lógica para fechar o PopupWindow
        Button closeButton = popupView.findViewById(R.id.btnReset);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();  // Fecha o PopupWindow
                reset();
            }
        });

        // Exibir o PopupWindow
        popupWindow.showAtLocation(v.getRootView(), Gravity.CENTER, 0, 0);
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    private int RamdomNum(){
        int num;

        Random random = new Random();
        num = random.nextInt(100) + 1;

        return num;
    }
}