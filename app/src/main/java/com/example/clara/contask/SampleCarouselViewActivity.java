package com.example.clara.contask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.clara.contask.model.Tarefa;
import com.google.gson.Gson;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ViewListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SampleCarouselViewActivity extends AppCompatActivity {

    private BroadcastReceiver broadcastReceiver;

    CarouselView customCarouselView;

    TextView customCarouselLabel;
    TextView text_wait;

    int[] sampleImages = {R.drawable.image_1, R.drawable.image_2, R.drawable.image_3, R.drawable.image_4, R.drawable.image_5};
    String[] sampleTitles = {"For an application having multiple activities , cant we implement this callback class in the Main Application class so it would know that which activity is currently being created/resumed/stopped etc ? \n", "Grapes \n", "Strawberry \n", "Cherry \n", "Apricot \n"};
    List<Integer> taskIds = new ArrayList<Integer>();
    List<String> taskQuestion = new ArrayList<String>();
    List<Integer> taskAnswer = new ArrayList<Integer>();
    List<Integer> taskContext = new ArrayList<Integer>();
    private List<Tarefa> allTasks;

    public ImageView mDialog;
    public Integer genderSelected = 0;

    public static final Integer FEMALE = 0;
    public static final Integer MALE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_scrolling_tasks);
        customCarouselView = (CarouselView) findViewById(R.id.customCarouselView);
        customCarouselLabel = (TextView) findViewById(R.id.customCarouselLabel);
        text_wait = (TextView) findViewById(R.id.waiting);

        mDialog = (ImageView) findViewById(R.id.your_image);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Gson gson = new Gson();
                allTasks = Arrays.asList(gson.fromJson(intent.getStringExtra("allTasks"), Tarefa[].class));

                if (allTasks.size() != 0) {
                    for (Tarefa tarefa : allTasks) {
                        Integer contexto = Integer.parseInt(tarefa.getId());
                        String question = tarefa.getTitulo();
                        Integer answer = Integer.parseInt(tarefa.getTipo());
                        String distance = "";
                        String horaStr = "";
                        if (answer!= null && contexto != null) {
                            taskQuestion.add(tarefa.getId() + " - " + question);
                            taskAnswer.add(answer);
                            taskContext.add(contexto);
                            if (taskIds.isEmpty()) { //primeira vez que encontra
                                taskIds.add(Integer.parseInt(tarefa.getId())); //ve se ta vazia e add, e faz os comandos especificos pra qd ta vazio
                                customCarouselView.setSlideInterval(4000);
                                customCarouselView.reSetSlideInterval(0);
                                text_wait.setVisibility(View.INVISIBLE);
                            } else {
                                taskIds.add(Integer.parseInt(tarefa.getId())); //add e seta numero novo de pagina
                                Log.i("MainActivity2", "Worker Distance (meters) to Task " + tarefa.getId() + ": " + distance);
                            }
                        }
                    }

                }
                customCarouselView.setViewListener(viewListener);
                customCarouselView.setPageCount(allTasks.size());

            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((broadcastReceiver),
                new IntentFilter(ServiceTask.SERVICE_RESULT));
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        super.onStop();
    }

    // To set custom views
    ViewListener viewListener = new ViewListener() {
        @Override
        public View setViewForPosition(final int position) {

            View customView = getLayoutInflater().inflate(R.layout.task_view, null);

            TextView labelTextView = (TextView) customView.findViewById(R.id.taskText);

            if (taskIds.get(position) != null) {
                labelTextView.setText(taskQuestion.get(position));

                if (taskAnswer.get(position).equals(2)) {
                    final TextView textSB = (TextView) customView.findViewById(R.id.initialvaluetextID);
                    final SeekBar seekBar = (SeekBar) customView.findViewById(R.id.seekBarID);
                    final RelativeLayout rl = (RelativeLayout) customView.findViewById(R.id.relativeLayoutNivel);
                    rl.setVisibility(View.VISIBLE);
                    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {
                            // TODO Auto-generated method stub
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {
                            // TODO Auto-generated method stub
                        }

                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            textSB.setText(String.valueOf(progress));
                        }
                    });
                }

                final RadioGroup rb = (RadioGroup) customView.findViewById(R.id.radioSex);

                if (taskAnswer.get(position).equals(1)) {
                    rb.setVisibility(View.VISIBLE);
                }

                Button sendAnswer = (Button) customView.findViewById(R.id.send_task);

                sendAnswer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int selectedRadioButtonID = rb.getCheckedRadioButtonId();

                        // If nothing is selected from Radio Group, then it return -1
                        if (selectedRadioButtonID != -1) {

                            RadioButton selectedRadioButton = (RadioButton) findViewById(selectedRadioButtonID);
                            String selectedRadioButtonText = selectedRadioButton.getText().toString();
                            Tarefa currentTask = allTasks.get(position);
                            Log.i("tarefa enviada", currentTask.getTitulo());
                            // Criar a resposta e enviar abaixo


                            allTasks = allTasks.subList(1, allTasks.size());
                            taskQuestion = taskQuestion.subList(1, taskQuestion.size());
                            taskAnswer = taskAnswer.subList(1, taskAnswer.size());
                            taskContext = taskContext.subList(1, taskContext.size());
                            taskIds =  taskIds.subList(1, taskIds.size());

                            customView.setVisibility(View.INVISIBLE);
                            customCarouselView.setCurrentItem(position + 1);
                            customCarouselView.setPageCount(customCarouselView.getPageCount() - 1 );

                        }
                    }
                });


            }
            return customView;
        }
    };

    View.OnClickListener pauseOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            customCarouselView.reSetSlideInterval(0);

        }
    };

}