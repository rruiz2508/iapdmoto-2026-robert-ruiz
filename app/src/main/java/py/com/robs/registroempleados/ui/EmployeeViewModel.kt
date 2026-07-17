package py.com.robs.registroempleados.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import py.com.robs.registroempleados.model.Employee

class EmployeeViewModel : ViewModel() {
    private val _employees = mutableStateListOf<Employee>()
    private var nextId = 1L

    val employees: List<Employee>
        get() = _employees

    fun addEmployee(
        fullName: String,
        position: String,
        department: String,
        salary: Double,
        hireDate: String,
    ) {
        _employees.add(
            index = 0,
            element = Employee(
                id = nextId++,
                fullName = fullName.trim(),
                position = position.trim(),
                department = department.trim(),
                salary = salary,
                hireDate = hireDate,
            ),
        )
    }

    fun deleteEmployee(employee: Employee) {
        _employees.remove(employee)
    }
}

