package com.example.developerslife;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    // ---------------------------------------------------------------------------------------------
    /*
        Ссылки на элементы активности.
    */
    private ImageView imageViewGif;
    private TextView textViewDescription;
    private FloatingActionButton floatingActionButtonPrevious;

    private TextView textViewModeTop;
    private TextView textViewModeNew;
    private TextView textViewModeRandom;
    // ---------------------------------------------------------------------------------------------

    private MemeController memeController; // Объект для управления выводом мемов.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Отключение ночного режима
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // -----------------------------------------------------------------------------------------
        /*
            Связывание ссылок на элементы с элементами.
        */
        textViewModeTop = findViewById(R.id.textViewModTop);
        textViewModeNew = findViewById(R.id.textViewModNew);
        textViewModeRandom = findViewById(R.id.textViewModRandom);

        floatingActionButtonPrevious = findViewById(R.id.floatingActionButtonPrevious);
        floatingActionButtonPrevious.setClickable(false);

        imageViewGif = findViewById(R.id.imageViewGif);
        textViewDescription = findViewById(R.id.textViewDescription);
        // -----------------------------------------------------------------------------------------

        /*
            Создаём объект класса MemeController. Передаём в него текущий контекст и ссылки на ImageView и TextView.
            Далее устанавливаем первый мем.
        */
        memeController = new MemeController(this, imageViewGif, textViewDescription);
        memeController.nextMeme();

    }

    // Нажатие на кнопку "далее"
    public void onClickNext(View view) {
        // Устаналиваем следующий мем и делаем кнопку "назад" активной.
        memeController.nextMeme();
        setFloatingActionButtonPreviousMode();
    }

    // Нажатие на кнопку "назад"
    public void onClickPrevious(View view) {
        // Устанавливаем предыдущий мем и проверяем, должна ли кнопка "назад" оставаться активной.
        memeController.previousMeme();
        setFloatingActionButtonPreviousMode();
    }

    // Выбор режима "Лучшее"
    public void onClickSetModeTop(View view) {
        /*
            Если был другой режим, то переключаем на "лучшее",
            меняем дизайн категорий и "обновляем" мем.
        */
        if (memeController.getMode() != memeController.MODE_TOP) {
            memeController.setMode(memeController.MODE_TOP);
            textViewModeTop.setTextColor(getResources().getColor(R.color.chosen));
            textViewModeNew.setTextColor(getResources().getColor(R.color.not_chosen));
            textViewModeRandom.setTextColor(getResources().getColor(R.color.not_chosen));
            memeController.updateMeme();
            setFloatingActionButtonPreviousMode();
        }
    }

    // Выбор режима "Новое"
    public void onClickSetModeNew(View view) {
        /*
            Если был другой режим, то переключаем на "новое",
            меняем дизайн категорий и "обновляем" мем.
        */
        if (memeController.getMode() != memeController.MODE_NEW) {
            memeController.setMode(memeController.MODE_NEW);
            textViewModeTop.setTextColor(getResources().getColor(R.color.not_chosen));
            textViewModeNew.setTextColor(getResources().getColor(R.color.chosen));
            textViewModeRandom.setTextColor(getResources().getColor(R.color.not_chosen));
            memeController.updateMeme();
            setFloatingActionButtonPreviousMode();
        }
    }

    // Выбор режима "Рандом"
    public void onClickSetModeRandom(View view) {
        /*
            Если был другой режим, то переключаем на "рандом",
            меняем дизайн категорий и "обновляем" мем.
        */
        if (memeController.getMode() != memeController.MODE_RANDOM) {
            memeController.setMode(memeController.MODE_RANDOM);
            textViewModeTop.setTextColor(getResources().getColor(R.color.not_chosen));
            textViewModeNew.setTextColor(getResources().getColor(R.color.not_chosen));
            textViewModeRandom.setTextColor(getResources().getColor(R.color.chosen));
            memeController.updateMeme();
            setFloatingActionButtonPreviousMode();
        }
    }

    // Устанавливает дизайн кнопки "назад"
    private void setFloatingActionButtonPreviousMode() {
        /*
            Если предыдущих элементов в кэше нет, то кнопка неактивна.
            Иначе - активна.
        */
        if (memeController.isFirstPosition() || memeController.isNullPosition()) {
            floatingActionButtonPrevious.setClickable(false);
            floatingActionButtonPrevious.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.not_clickable)));
        } else {
            floatingActionButtonPrevious.setClickable(true);
            floatingActionButtonPrevious.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.clickable)));
        }
    }
}