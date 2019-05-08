package com.clay.tspsurat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.clay.tspsurat.fragment.NodeFragment;
import com.clay.tspsurat.fragment.PengunaFragment;
import com.clay.tspsurat.fragment.dummy.DummyContent;
import com.clay.tspsurat.model.Penguna;
import com.orm.SugarContext;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        PengunaFragment.OnListFragmentInteractionListener,
        NodeFragment.OnListFragmentInteractionListener

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

        SugarContext.init(this);

        getPenguna();
        setView();
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

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragmentData = null;

        if (id == R.id.nav_data_kantor) {
            dataEdit = "Kantor";
            fragmentData = new NodeFragment();
            setNodeRt();
            // Handle the camera action
        } else if (id == R.id.nav_data_rt) {
            dataEdit = "RT";
            fragmentData = new NodeFragment();
            setNodeRt();
        } else if (id == R.id.nav_data_rw) {
            dataEdit = "RW";
            fragmentData = new NodeFragment();
            setNodeRt();
        } else if (id == R.id.nav_manage) {
            fragmentData = new PengunaFragment();
            setPengunaView();
        } else if (id == R.id.nav_data_simpangan) {
            dataEdit = "Simpangan";
            fragmentData = new NodeFragment();
            setNodeRt();
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
        setTitle("Data RT");
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
                    final Spinner spinner = prompt.findViewById(R.id.PengunaLevel);


                    final AlertDialog.Builder alertDialogBuilder =
                            new AlertDialog.Builder(MenuActivity.this);
                    alertDialogBuilder.setTitle("Admin Mode");
                    alertDialogBuilder.setMessage("Masukan Data Penguna Baru");
                    alertDialogBuilder.setView(prompt);
                    alertDialogBuilder.setPositiveButton("Simpan",
                            new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int id)
                                {

                                    Penguna penguna = new Penguna(
                                            PengunaNama.getText().toString(),
                                            PengunaUsername.getText().toString(),
                                            PengunaPassword.getText().toString(),
                                            PengunaNIP.getText().toString(),
                                            PengunaKontak.getText().toString(),
                                            spinner.getSelectedItemPosition());
                                    System.out.println("penguna = " + penguna);
                                    penguna.save();
                                    makeSnakeBar(null,"Penguna Berhasil Tersimpan");

                                    FragmentTransaction fragmentTransaction = MenuActivity.this.getSupportFragmentManager().beginTransaction();
                                    fragmentTransaction.replace(R.id.FrameFragment,new PengunaFragment());
                                    fragmentTransaction.commit();


                                    dialog.cancel();
                                }
                            });
                    alertDialogBuilder.setNegativeButton("Batal",
                            new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int id)
                                {
                                    dialog.cancel();
                                }
                            });
                    alertDialogBuilder.create().show();


                }
            }
        });
    }

    private boolean CheckLevelPenguna() {
        if (levelPenguna == 1)
            return true;
        makeSnakeBar(null,"Khusus Adminstrator");
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
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {
        System.out.println("item = " + item);
    }
}
