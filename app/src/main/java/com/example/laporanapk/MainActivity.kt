package com.example.laporanapk

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.laporanapk.databinding.ActivityMainBinding
import com.example.laporanapk.fragment.HomeFragment
import com.example.laporanapk.pref.PrefManager

import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding : ActivityMainBinding
    private lateinit var drawerLayout : DrawerLayout
    private  val requestPermission = 1
    private lateinit var pref: PrefManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        pref = PrefManager(this)
        getPermission()

        drawerLayout = binding.drawerLayout
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        binding.toolbarTitle.text = toolbar.title
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar

        val navigationView: NavigationView = binding.navView
        val headerView: View = navigationView.getHeaderView(0)
        val txtName: TextView = headerView.findViewById(R.id.txtName)
        val txtEmail: TextView = headerView.findViewById(R.id.txtEmail)

        txtName.text = pref.getNama()
        txtEmail.text = pref.getEmail()

        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(this,drawerLayout, toolbar, R.string.open_nav, R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        if (savedInstanceState == null)
        {
            replaceFragment(HomeFragment())
            navigationView.setCheckedItem(R.id.nav_home)

        }

    }

    private fun getPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                requestPermission
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            requestPermission -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, you can proceed with the functionality requiring this permission
                } else {
                    // Permission denied, show a message or handle accordingly
                    Toast.makeText(
                        this,
                        "Permission denied. You may not be able to save files.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId)
        {
            R.id.add_data -> {
                val intent = Intent(this, InsertActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.print_data -> {
                val intent = Intent(this, PrintActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
       when(item.itemId)
       {
           R.id.nav_home -> replaceFragment(HomeFragment())
           R.id.nav_settings -> Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
           R.id.nav_share -> Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show()
           R.id.nav_about -> Toast.makeText(this, "About", Toast.LENGTH_SHORT).show()
           R.id.nav_logout -> Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show()
       }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun replaceFragment(fragment : Fragment)
    {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(binding.fragmentContainer.id, fragment)
        transaction.commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START)
        }else {
            onBackPressedDispatcher.onBackPressed()
        }
    }



}