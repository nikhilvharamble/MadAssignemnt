package com.example.madsassignment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.udojava.evalex.Expression;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.edt_input) EditText edt_input;
    @BindView(R.id.rec_result) RecyclerView rec_result;
    String user_input="";
    private ArrayList<Result> resultList = new ArrayList<>();
    ResultDisplayAdapter resultDisplayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        edt_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                    if(checkUserinput(s.toString()))
                    {
                        user_input=s.toString();
                    }
                    else
                    {
                        s.replace(0, s.length(), user_input);
                    }

            }
        });
        edt_input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                  Double result_double=eval(user_input);
                   Result result=new Result();
                   result.setExpression(user_input);
                   result.setResult("="+result_double);
                    resultList.add(result);
                     resultDisplayAdapter=new ResultDisplayAdapter(MainActivity.this,resultList);
                    LinearLayoutManager llm = new LinearLayoutManager(MainActivity.this);
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    rec_result.setLayoutManager(llm);
                     rec_result.setAdapter(resultDisplayAdapter);
                    handled = true;
                }
                return handled;
            }
        });
    }

    private boolean checkUserinput(String user_input)
    {
        System.out.println("****************************"+user_input);
        Pattern pattern = Pattern.compile("^[0-9+\\-*.\\/]*$");
        Pattern pattern1 = Pattern.compile("[0-9]");
        Pattern pattern2 = Pattern.compile("^[0-9]");
        Matcher matcher = pattern.matcher(user_input);
        Matcher matcher1 = pattern1.matcher(user_input);
        Matcher matcher2 = pattern2.matcher(user_input);
        boolean isStringContainsOperationCharacter = matcher.find();
        boolean isNumber = matcher1.find();
        boolean isstartNumber = matcher2.find();
        System.out.println("**************************"+isStringContainsOperationCharacter);


            if(isStringContainsOperationCharacter/*||isNumber||isstartNumber*/){


                        return true;


            }


        return false;

    }

    public static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)` | number
            //        | functionName `(` expression `)` | functionName factor
            //        | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('/')) x /= parseTerm(); // division
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }

            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('+')) x += parseFactor(); // addition
                    else return x;
                }

            }

            double parseFactor() {
                if (eat('+')) return +parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    if (!eat(')')) throw new RuntimeException("Missing ')'");
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    if (eat('(')) {
                        x = parseExpression();
                        if (!eat(')')) throw new RuntimeException("Missing ')' after argument to " + func);
                    } else {
                        x = parseFactor();
                    }
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }
}