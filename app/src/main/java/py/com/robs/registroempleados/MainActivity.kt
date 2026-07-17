package py.com.robs.registroempleados

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import py.com.robs.registroempleados.ui.EmployeeScreen
import py.com.robs.registroempleados.ui.EmployeeViewModel
import py.com.robs.registroempleados.ui.theme.RegistroEmpleadosTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val systemDarkTheme = isSystemInDarkTheme()
            var darkTheme by rememberSaveable { mutableStateOf(systemDarkTheme) }
            val employeeViewModel: EmployeeViewModel = viewModel()

            RegistroEmpleadosTheme(darkTheme = darkTheme) {
                EmployeeScreen(
                    employees = employeeViewModel.employees,
                    darkTheme = darkTheme,
                    onThemeChange = { darkTheme = !darkTheme },
                    onAddEmployee = employeeViewModel::addEmployee,
                    onDeleteEmployee = employeeViewModel::deleteEmployee,
                )
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "onStart")
    }

    override fun onStop() {
        Log.i(TAG, "onStop")
        super.onStop()
    }

    override fun onDestroy() {
        Log.i(TAG, "onDestroy")
        super.onDestroy()
    }

    private companion object {
        const val TAG = "MainActivity"
    }
}

