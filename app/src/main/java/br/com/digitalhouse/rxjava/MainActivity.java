package br.com.digitalhouse.rxjava;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.digitalhouse.rxjava.adapters.UsuarioRecyclerViewAdapter;
import br.com.digitalhouse.rxjava.model.Usuario;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private UsuarioRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new UsuarioRecyclerViewAdapter(new ArrayList<Usuario>());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(view -> {
            Snackbar.make(view, "Você clicou no botão", Snackbar.LENGTH_SHORT).show();
        });

        Observable.range(0, 100)
                .map(numero -> numero*2)
                .filter(numero -> numero == 4)
                .subscribe(integer -> {
                    System.out.println(integer);
                },throwable -> {
                    System.out.println("Error: " + throwable.getMessage());
                });

//        exemplo de código para lista de nomes/strings
        Observable.fromIterable(Arrays.asList("Cecilia", "Patricia"))
                .filter(nome -> nome.equals("Cecilia"))
                .subscribe(nome -> {
                    System.out.println(nome);
                },throwable -> {
                    System.out.println("Error: " + throwable.getMessage());
                });

        Observable.just("Cecilia")
                .subscribe(nome -> {
                    System.out.println(nome);
                },throwable -> {
                    System.out.println("Error: " + throwable.getMessage());
                });

        Observable<String> stringObservable = Observable.create(emitter -> {
            emitter.onNext("Cecilia");
            emitter.onNext("Patricia");
            emitter.onComplete();
        });

        stringObservable
                .map(nome -> {
                    if (nome.equals("Patricia")){
                        nome = "Cecilia";
                    }
                    return nome;
                })
                .filter(nome -> nome.equals("Cecilia"))
                .subscribe(s -> {
//                    sucesso
                }, throwable -> {
//                    erro
                }, () -> {
//                    completo
                });

        getUsuarios()
                .subscribeOn(Schedulers.io())
//                .subscribeOn(Schedulers.computation())
//                a opção acima serve para manipulações mais pesadas
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(usuarios -> {
//                    sucesso
                    adapter.update(usuarios);
                }, throwable -> {
//                    erro
                    Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private Observable<List<Usuario>> getUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        usuarios.add(new Usuario("Cecilia", 29));
        usuarios.add(new Usuario("Patricia", 27));
        usuarios.add(new Usuario("Kadu", 31));
        usuarios.add(new Usuario("Silvia", 50));
        usuarios.add(new Usuario("Sergio", 54));

        return Observable.just(usuarios);
    }
}
