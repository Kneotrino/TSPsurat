package com.clay.tspsurat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.clay.tspsurat.fragment.MainFragment;
import com.clay.tspsurat.model.Node;
import com.clay.tspsurat.utils.SessionHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.clay.tspsurat.fragment.NodeFragment;
import com.clay.tspsurat.fragment.PengunaFragment;
import com.clay.tspsurat.fragment.dummy.DummyContent;
import com.clay.tspsurat.model.Penguna;
import com.orm.SugarContext;
import com.orm.SugarDb;

import java.util.LinkedList;
import java.util.List;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        PengunaFragment.OnListFragmentInteractionListener,
        NodeFragment.OnListFragmentInteractionListener,
        MainFragment.OnFragmentInteractionListener

{


    @Override
    protected void onDestroy() {
        super.onDestroy();
        SugarContext.terminate();
    }

        FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Memulai Pengiriman", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        navUsername = (TextView) headerView.findViewById(R.id.txtNama);
        navUserNip = (TextView) headerView.findViewById(R.id.txtNip);

        if (SessionHelper.getInstance(this).getAppFirstTime()) {
            Log.d("MainApp", "First session");
            SugarDb db = new SugarDb(this);
            db.onCreate(db.getDB());
            SessionHelper.getInstance(this).setAppFirstTime(false);
            InputDataAwal();
        } else {
            Log.d("MainApp", "Not First session");
            List<Node> nodes = Node.listAll(Node.class);
//            for (Node node : nodes ) {
//                System.out.println("node = " + node);
//            }
            System.out.println("nodes = " + nodes.size());

//            String kunci = getResources().getString(R.string.google_maps_key);
//            Places.initialize(this,kunci);
        }

        SugarContext.init(this);

        getPenguna();
        setView();

        FragmentTransaction fragmentTransaction = MenuActivity.this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.FrameFragment,new MainFragment());
        fragmentTransaction.commit();

    }

    private static final int KANTOR = 0,RW =1,RT =2,SIMPANGAN=3;


    private void InputDataAwal() {
        System.out.println("INPUT DATA");
        Node kantor = new Node("NAMA KEPALA","LURAH",KANTOR,1,0d,0d);
        kantor.save();
        for (int i = 1; i <= 10; i++) {
            Node node = new Node("RW",RW,i,0d,0d);
            node.save();
        }
        for (int i = 1; i <= 10; i++) {
            Node node = new Node("RT",RT,i,0d,0d);
            node.save();
        }
        for (int i = 1; i <= 10; i++) {
            Node node = new Node("SIMPANGAN",SIMPANGAN,i,0d,0d);
            node.save();
        }
    }

    long PengunaID;
    TextView navUsername,navUserNip;
    Penguna penguna;
    int levelPenguna;
    String sLevelPenguna;
    private void setView() {
        System.out.println("penguna = " + penguna);
        navUsername.setText(penguna.getNama());
        navUserNip.setText(sLevelPenguna);
        if  (CheckLevelPenguna())
            showFloatingActionButton(fab);
        else
            hideFloatingActionButton(fab);

    }

    private void getPenguna() {
        Bundle b = getIntent().getExtras();
        PengunaID = b.getLong("id");
        penguna = Penguna.findById(Penguna.class, PengunaID);
        levelPenguna = penguna.getLevel();
        if (levelPenguna == 0)
            sLevelPenguna = "Petugas";
        else if (levelPenguna == 1)
            sLevelPenguna = "Admin";
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(MenuActivity.this, LoginActivity.class);
            startActivity(i);
//            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public void onFragmentInteraction(Uri uri) {
//
//    }

    @SuppressWarnings("StatementWithEmptyBody")

    String dataEdit = "";
//    int Pilihan = 0;

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragmentData = null;

        if (id == R.id.nav_data_kantor) {
            dataEdit = "Kantor";
            fragmentData = NodeFragment.newInstance(KANTOR);
            setNodeRt();
            // Handle the camera action
        } else if (id == R.id.nav_data_rt) {
            dataEdit = "RT";
            fragmentData = NodeFragment.newInstance(RT);
            setNodeRt();
        } else if (id == R.id.nav_data_rw) {
            dataEdit = "RW";
            fragmentData = NodeFragment.newInstance(RW);
            setNodeRt();
        } else if (id == R.id.nav_manage) {
            fragmentData = new PengunaFragment();
            setPengunaView();
        } else if (id == R.id.nav_data_simpangan) {
            dataEdit = "Simpangan";
            fragmentData = NodeFragment.newInstance(SIMPANGAN);
            setNodeRt();
        } else if (id == R.id.nav_main) {
            dataEdit = "Main";
            fragmentData = new MainFragment();
            setView();
        }
        setTitle("Data "+ dataEdit);
        FragmentTransaction fragmentTransaction = MenuActivity.this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.FrameFragment,fragmentData);
        fragmentTransaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setNodeRt() {
//        setTitle("Data RT");
        //@android:drawable/ic_menu_add
        fab.setImageResource(android.R.drawable.ic_menu_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeSnakeBar(view,"Berhasil Tambah");
            }
        });

    }

    private void setPengunaView() {
        setTitle("Data Penguna");
        //@android:drawable/ic_menu_add
        fab.setImageResource(android.R.drawable.ic_menu_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckLevelPenguna()) {
                    LayoutInflater li = LayoutInflater.from(MenuActivity.this);
                    View prompt = li.inflate(R.layout.form_penguna, null);

                    final EditText PengunaNama = prompt.findViewById(R.id.PengunaNama);
                    final EditText PengunaUsername = prompt.findViewById(R.id.PengunaUsername);
                    final EditText PengunaPassword = prompt.findViewById(R.id.PengunaPassword);
                    final EditText PengunaNIP = prompt.findViewById(R.id.PengunaNIP);
                    final EditText PengunaKontak = prompt.findViewById(R.id.PengunaKontak);

                    final List<EditText> editTextList = new LinkedList<>();
                    editTextList.add(PengunaNama);
                    editTextList.add(PengunaUsername);
                    editTextList.add(PengunaPassword);
                    editTextList.add(PengunaNIP);
                    editTextList.add(PengunaKontak);


                    final Spinner spinner = prompt.findViewById(R.id.PengunaLevel);


                    final AlertDialog dialog = new AlertDialog.Builder(MenuActivity.this)
                            .setView(prompt)
                            .setTitle("Admin Mode Input Data Penguna")
                            .setMessage("Masukan Data Penguna Baru")
                            .setPositiveButton(android.R.string.ok, null) //Set to null. We override the onclick
                            .setNegativeButton(android.R.string.cancel, null)
                            .create();

                    dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialogInterface) {

                            Button btnSimpan = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                            Button btnBatal = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);

                            btnBatal.setText("Batal");
                            btnSimpan.setText("Tambah");
                            btnSimpan.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View view) {
                                    // TODO Do something
                                    boolean inputCheck = checkEditText(editTextList);
                                    Penguna penguna = new Penguna(
                                            PengunaNama.getText().toString(),
                                            PengunaUsername.getText().toString(),
                                            PengunaPassword.getText().toString(),
                                            PengunaNIP.getText().toString(),
                                            PengunaKontak.getText().toString(),
                                            spinner.getSelectedItemPosition());
                                    System.out.println("penguna = " + penguna);
                                    if (inputCheck) {
                                          penguna.save();
                                          makeSnakeBar(null,"Penguna Berhasil Tersimpan");
                                          dialog.dismiss();
                                        FragmentTransaction fragmentTransaction = MenuActivity.this.getSupportFragmentManager().beginTransaction();
                                        fragmentTransaction.replace(R.id.FrameFragment,new PengunaFragment());
                                        fragmentTransaction.commit();

                                    }
                                }
                            });
                        }
                    });
                    dialog.show();
                }
            }
        });
    }

    private boolean checkEditText(List<EditText> editTextList) {

        for (EditText editText: editTextList) {
            if (TextUtils.isEmpty(editText.getText().toString())) {
                editText.setError(getString(R.string.error_field_required));
                return false;
            }
            if ( editText.getText().toString().length() < 4) {
                editText.setError("Input minimal 4 karakter");
                return false;
            }
        }
        return true;
    }

    private boolean CheckLevelPenguna() {
        if (levelPenguna == 1)
            return true;
//        makeSnakeBar(null,"Khusus Adminstrator");
        return false;
    }

    private void makeSnakeBar(View view,String txt){

        if (view == null)
            view = findViewById(fab.getId());
        Snackbar.make(view, txt, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

    }

    private void hideFloatingActionButton(FloatingActionButton fab) {
        CoordinatorLayout.LayoutParams params =
                (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        FloatingActionButton.Behavior behavior =
                (FloatingActionButton.Behavior) params.getBehavior();

        if (behavior != null) {
            behavior.setAutoHideEnabled(false);
        }

        fab.hide();
    }

    private void showFloatingActionButton(FloatingActionButton fab) {
        fab.show();
        CoordinatorLayout.LayoutParams params =
                (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        FloatingActionButton.Behavior behavior =
                (FloatingActionButton.Behavior) params.getBehavior();

        if (behavior != null) {
            behavior.setAutoHideEnabled(true);
        }
    }


    @Override
    public void onListFragmentInteraction(Penguna penguna) {
        System.out.println("penguna = " + penguna);
        viewPenguna(penguna);
    }

    private void viewPenguna(final Penguna penguna) {

        LayoutInflater li = LayoutInflater.from(MenuActivity.this);
        View prompt = li.inflate(R.layout.form_penguna, null);

        final EditText PengunaNama = prompt.findViewById(R.id.PengunaNama);
        final EditText PengunaUsername = prompt.findViewById(R.id.PengunaUsername);
        final EditText PengunaPassword = prompt.findViewById(R.id.PengunaPassword);
        final EditText PengunaNIP = prompt.findViewById(R.id.PengunaNIP);
        final EditText PengunaKontak = prompt.findViewById(R.id.PengunaKontak);

        final List<EditText> editTextList = new LinkedList<>();
        editTextList.add(PengunaNama);
        editTextList.add(PengunaUsername);
        editTextList.add(PengunaPassword);
        editTextList.add(PengunaNIP);
        editTextList.add(PengunaKontak);

        PengunaNama.setText(penguna.getNama());
        PengunaUsername.setText(penguna.getUsername());
        PengunaPassword.setText(penguna.getUserpass());
        PengunaNIP.setText(penguna.getNip());
        PengunaKontak.setText(penguna.getKontak());

        final Spinner spinner = prompt.findViewById(R.id.PengunaLevel);


        final AlertDialog dialog = new AlertDialog.Builder(MenuActivity.this)
                .setView(prompt)
                .setTitle("Data Penguna")
                .setPositiveButton("Simpan", null) //Set to null. We override the onclick
                .setNegativeButton("Hapus", null)
                .setNeutralButton("Batal", null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button btnSimpan = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                Button btnHapus = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);

                btnHapus.setOnClickListener(
                        new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                Penguna hapuskan = Penguna.findById(Penguna.class,penguna.getId());

                                    boolean delete = hapuskan.delete();
                                    System.out.println("delete = " + delete);
                                    makeSnakeBar(null,"Penguna Berhasil Terhapus");
                                    FragmentTransaction fragmentTransaction = MenuActivity.this.getSupportFragmentManager().beginTransaction();
                                    fragmentTransaction.replace(R.id.FrameFragment,new PengunaFragment());
                                    fragmentTransaction.commit();
                                    dialog.dismiss();
                            }
                        }
                );

                btnSimpan.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // TODO Do something
                        boolean inputCheck = checkEditText(editTextList);

                        Penguna update = Penguna.findById(Penguna.class,penguna.getId());
                        update.setNama(PengunaNama.getText().toString());
                        update.setUsername(PengunaUsername.getText().toString());
                        update.setUserpass(PengunaPassword.getText().toString());
                        update.setNip(PengunaNIP.getText().toString());
                        update.setKontak(PengunaKontak.getText().toString());
                        update.setLevel(spinner.getSelectedItemPosition());
                        System.out.println("update = " + update);
                        if (inputCheck) {
                            update.save();
                            makeSnakeBar(null,"Penguna Berhasil Tersimpan");
                            dialog.dismiss();
                            FragmentTransaction fragmentTransaction = MenuActivity.this.getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.FrameFragment,new PengunaFragment());
                            fragmentTransaction.commit();
                        }
                    }
                });
            }
        });
        dialog.show();

    }

//    private boolean viewKonfirmasi(String txt) {
//        final boolean[] answer = new boolean[1];
//        final AlertDialog dialog = new AlertDialog
//                .Builder(MenuActivity.this)
//                .setMessage(txt)
//                .setPositiveButton(
//                        "Iya",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                                answer[0] = true;
//                            }
//                        })
//                .setNegativeButton(
//                "Tidak",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                        answer[0] = false;
//                    }
//                })
//                .create();
//        dialog.show();
//        return answer[0];
//    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction(final Node item) {
        System.out.println("item = " + item);
        String[] colors = {"Lihat", "Edit", "Hapus", "Batal"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pilih Aksi :"+item.toString());
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on colors[which]
                System.out.println("which = " + which);
                switch (which) {
                    case 0:
                        System.out.println("Lihat");
//                        System.out.println("item = " + item);
//                        startActivity(myIntent);
                        break;
                    case 1:
                        System.out.println("Edit");
//                        EditRumahMakan(item);
                        break;
                    case 2:
                        System.out.println("Hapus");
                        HapusNode(item);
//                        HapusRumahMakan(item);
                        break;
                    case 3:
                        System.out.println("Batal");
                        break;
                }
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void HapusNode(final Node item) {
        System.out.println("Hapus.item = " + item);
        AlertDialog HapusDialog =new AlertDialog.Builder(this)
                .setTitle("Hapus")
                .setMessage("Anda Yakin untuk Hapus")
                .setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code
                        item.delete();
                        dialog.dismiss();
                        ResetDataFragment();
                    }
                })
                .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        HapusDialog.show();
    }

    private void ResetDataFragment() {

        int pilihan = -1;
        switch (dataEdit) {
            case "Kantor":
                pilihan = KANTOR;
                break;
            case "RT":
                pilihan = RT;
                break;
            case "RW":
                pilihan = RW;
                break;
            case "Simpangan":
                pilihan = SIMPANGAN;
                break;
        }

        FragmentTransaction fragmentTransaction = MenuActivity.this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.FrameFragment,NodeFragment.newInstance(pilihan));
        fragmentTransaction.commit();

    }
}
