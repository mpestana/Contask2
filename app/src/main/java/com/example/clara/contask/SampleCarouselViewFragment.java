package com.example.clara.contask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.clara.contask.model.Tarefa;
import com.google.gson.Gson;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ViewListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SampleCarouselViewFragment extends Fragment {

    private BroadcastReceiver broadcastReceiver;

    CarouselView customCarouselView;

    TextView customCarouselLabel;
    TextView text_wait;

    List<Integer> taskIds = new ArrayList<Integer>();
    List<String> taskQuestion = new ArrayList<String>();
    List<Integer> taskAnswer = new ArrayList<Integer>();
    List<Integer> taskContext = new ArrayList<Integer>();
    private List<Tarefa> allTasks;

    public ImageView mDialog;
    public Integer genderSelected = 0;

    public static final Integer FEMALE = 0;
    public static final Integer MALE = 1;

    public SampleCarouselViewFragment()  {
        // Required empty public constructor
    }


    public static SampleCarouselViewFragment newInstance(String param1, String param2) {
        SampleCarouselViewFragment fragment = new SampleCarouselViewFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.content_scrolling_tasks, container, false);
    }


    ViewListener viewListener = new ViewListener() {
        @Override
        public View setViewForPosition(final int position) {

            View customView = getLayoutInflater().inflate(R.layout.task_view, null);

            TextView labelTextView = (TextView) customView.findViewById(R.id.taskText);

            if (taskIds.get(position) != null) {
                labelTextView.setText(taskQuestion.get(position));

                final RadioGroup rb = (RadioGroup) customView.findViewById(R.id.radioSex);
                final EditText responseText = (EditText) customView.findViewById(R.id.responseID);
                if(taskAnswer.get(position).equals(1)) {
                    rb.setVisibility(View.VISIBLE);
                    responseText.setVisibility(View.INVISIBLE);
                }
                else if(taskAnswer.get(position).equals(2)) {
                    rb.setVisibility(View.INVISIBLE);
                    responseText.setVisibility(View.VISIBLE);
                }

                Button sendAnswer = (Button) customView.findViewById(R.id.send_task);

                sendAnswer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String deviceID = Settings.Secure.getString(getContext().getContentResolver(),
                                Settings.Secure.ANDROID_ID);
                        int selectedRadioButtonID = rb.getCheckedRadioButtonId();
                        Tarefa currentTask = allTasks.get(position);


                        // If nothing is selected from Radio Group, then it return -1
                        if (selectedRadioButtonID != -1 && currentTask.getTipo().equals("1")) {

                            RadioButton selectedRadioButton = (RadioButton) customView.findViewById(selectedRadioButtonID);
                            String selectedRadioButtonText = selectedRadioButton.getText().toString();
                            String response = selectedRadioButtonText.equals("Yes") ? "true" : "false";
                            ServiceTask.enviarContribuicao(getContext(),currentTask.getId(),
                                    "BOOLEAN",response,
                                    "true", deviceID);

                        }
                        else if(!responseText.getText().equals("") && currentTask.getTipo().equals("2")){
                            // Criar a resposta e enviar abaixo
                            ServiceTask.enviarContribuicao(getContext() ,currentTask.getId(),
                                    "TEXTO",responseText.getText().toString(),
                                    "false", deviceID);
                        }

                        allTasks =removeAtPosition(position, allTasks);
                        taskQuestion =removeAtPosition(position, taskQuestion);
                        taskAnswer =removeAtPosition(position, taskAnswer);
                        taskContext =removeAtPosition(position, taskContext);
                        taskIds =removeAtPosition(position, taskIds);
                        ;

                        customView.setVisibility(View.INVISIBLE);
                        customCarouselView.setCurrentItem(position + 1);
                        customCarouselView.setPageCount(customCarouselView.getPageCount() - 1 );


                    }
                });


            }
            return customView;
        }
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        customCarouselView = (CarouselView) view.findViewById(R.id.customCarouselView);
        customCarouselLabel = (TextView) view.findViewById(R.id.customCarouselLabel);
        text_wait = (TextView) view.findViewById(R.id.waiting);

        mDialog = (ImageView) view.findViewById(R.id.your_image);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Gson gson = new Gson();
                allTasks= new ArrayList<>();
                Tarefa[] listTasks= gson.fromJson(intent.getStringExtra("allTasks"), Tarefa[].class);
                if(listTasks!=null){
                    allTasks = Arrays.asList(listTasks);
                }
                if (allTasks.size() == 0) {
                    for (int i = 0; i <3 ; i++) {
                        Tarefa newTarefa= new Tarefa(Integer.toString(i), "teste", "teste",
                                "teste", "teste", "teste", "teste", "teste",
                                "teste", "teste", "teste", "teste", "0");
                        allTasks.add(newTarefa);
                    }

                }
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
                customCarouselView.setPageCount(3);

            }
        };
    }

    @Override
    public void onStop() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(broadcastReceiver);
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver((broadcastReceiver),
                new IntentFilter(ServiceTask.SERVICE_RESULT));
    }

    private <T> List<T>  removeAtPosition(int position, List<T> lista) {
        List<T> head = lista.subList(0, position);
        List<T> tail = lista.subList(position + 1, lista.size());
        return  new ArrayList<T>() { { addAll(head); addAll(tail); } };
    }
}